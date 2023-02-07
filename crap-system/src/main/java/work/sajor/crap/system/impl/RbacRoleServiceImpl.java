package work.sajor.crap.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.sajor.crap.core.dao.dao.RbacRoleDao;
import work.sajor.crap.core.dao.dao.RbacRolePrivilegeDao;
import work.sajor.crap.core.dao.dao.RbacUserRoleDao;
import work.sajor.crap.core.dao.entity.RbacRole;
import work.sajor.crap.core.dao.entity.RbacRolePrivilege;
import work.sajor.crap.core.dao.entity.RbacUserRole;
import work.sajor.crap.core.dao.entity.base.RbacRoleBase;
import work.sajor.crap.core.dao.entity.base.RbacRolePrivilegeBase;
import work.sajor.crap.core.dao.entity.base.RbacUserRoleBase;
import work.sajor.crap.core.mybatis.support.Wrapper;
import work.sajor.crap.core.security.facade.WebUserService;
import work.sajor.crap.core.security.util.UriUtil;
import work.sajor.crap.core.util.ListUtil;
import work.sajor.crap.core.web.WebException;
import work.sajor.crap.system.facade.RbacRoleService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * RBAC 角色
 * 各子项目角色分离
 * </p>
 *
 * @author Sajor
 * @since 2023-02-06
 */
@Service
public class RbacRoleServiceImpl implements RbacRoleService {

    @Resource
    RbacRoleDao rbacRoleDao;

    @Resource
    RbacUserRoleDao rbacUserRoleDao;

    @Resource
    RbacRolePrivilegeDao rbacRolePrivilegeDao;

    @Resource
    WebUserService webUserService;

    @Override
    public List<Long> getIds(Long userId) {
        String project = UriUtil.getUriResource().getModule();
        Wrapper<RbacUserRole> wrapper = rbacUserRoleDao.getWrapper();
        wrapper.eq(RbacUserRole.Fields.userId, userId);
        List<RbacUserRole> list = rbacUserRoleDao.list(wrapper);
        return ListUtil.toList(list, RbacUserRoleBase::getRoleId);
    }

    @Override
    public List<RbacRole> getList() {
        Wrapper<RbacRole> wrapper = rbacRoleDao.getWrapper();
        wrapper.eq(RbacRole.Fields.status, 1);
        wrapper.eq(RbacRole.Fields.project, UriUtil.getUriResource().getModule());
        return rbacRoleDao.list(wrapper);
    }

    @Override
    public boolean remove(Long roleId) {
        Wrapper<RbacUserRole> roleWrapper = rbacUserRoleDao.getWrapper();
        roleWrapper.eq(RbacUserRole.Fields.roleId, roleId);
        List<RbacUserRole> list = rbacUserRoleDao.list(roleWrapper); // 受影响的用户

        Wrapper<RbacRole> wrapper = rbacRoleDao.getWrapper();
        wrapper.eq(RbacRole.Fields.id, roleId);
        wrapper.eq(RbacRole.Fields.project, UriUtil.getUriResource().getModule());
        List<RbacRole> removeList = rbacRoleDao.list(wrapper);
        boolean status = rbacRoleDao.removeByIds(ListUtil.toList(removeList, RbacRoleBase.Fields.id, Long.class));
        if (!status) {
            throw new WebException("删除失败");
        }

        for (RbacUserRole rbacUserRole : list) {                                        // 清除用户缓存
            webUserService.reload(rbacUserRole.getUserId());
        }
        return true;
    }

    @Override
    public boolean remove(Long[] roleIds) {
        for (Long roleId : roleIds) {
            remove(roleId);
        }
        return true;
    }

    @Override
    public void grant(Long roleId, List<Long> privilegeIds) {
        Wrapper<RbacRolePrivilege> privilegeRemoveWrapper = rbacRolePrivilegeDao.getWrapper();
        privilegeRemoveWrapper.eq(RbacRolePrivilegeBase.Fields.roleId, roleId);
        List<RbacRolePrivilege> privilegeRemoveList = rbacRolePrivilegeDao.list(privilegeRemoveWrapper);
        rbacRolePrivilegeDao.removeByIds(ListUtil.toList(privilegeRemoveList, RbacRolePrivilegeBase.Fields.id, Long.class));

        ArrayList<RbacRolePrivilege> rbacRolePrivileges = new ArrayList<>();
        privilegeIds.forEach(privilegeId -> {
            RbacRolePrivilege rbacRolePrivilege = new RbacRolePrivilege();
            rbacRolePrivilege.setRoleId(roleId);
            rbacRolePrivilege.setPrivilegeId(privilegeId);
            rbacRolePrivileges.add(rbacRolePrivilege);
        });
        rbacRolePrivilegeDao.saveBatch(rbacRolePrivileges);

        Wrapper<RbacUserRole> wrapper = rbacUserRoleDao.getWrapper();
        wrapper.eq(RbacUserRole.Fields.roleId, roleId);
        List<RbacUserRole> list = rbacUserRoleDao.list(wrapper); // 清除用户缓存
        for (RbacUserRole rbacUserRole : list) {
            webUserService.reload(rbacUserRole.getUserId());
        }
    }
}
