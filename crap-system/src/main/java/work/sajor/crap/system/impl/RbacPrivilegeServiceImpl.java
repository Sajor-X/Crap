package work.sajor.crap.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.sajor.crap.core.dao.dao.RbacPrivilegeDao;
import work.sajor.crap.core.dao.dao.RbacRolePrivilegeDao;
import work.sajor.crap.core.dao.entity.RbacPrivilege;
import work.sajor.crap.core.dao.entity.RbacRolePrivilege;
import work.sajor.crap.core.dao.entity.base.RbacRolePrivilegeBase;
import work.sajor.crap.core.dict.DictUtil;
import work.sajor.crap.core.mybatis.support.Wrapper;
import work.sajor.crap.core.security.util.UriUtil;
import work.sajor.crap.core.util.ListUtil;
import work.sajor.crap.system.facade.RbacPrivilegeService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 权限服务
 * </p>
 *
 * @author Sajor
 * @since 2023-02-05
 */
@Service
public class RbacPrivilegeServiceImpl implements RbacPrivilegeService {
    @Resource
    RbacPrivilegeDao privilegeDao;

    @Resource
    RbacRolePrivilegeDao rolePrivilegeDao;

    @PostConstruct
    protected void construct() {
        DictUtil.register("privilege", wrapper -> getList());
    }

    /**
     * 通过角色ID 获取权限ID列表
     *
     * @param roleId 角色ID
     * @return 权限ID List
     */
    @Override
    public List<Long> getRolePrivilegeIds(Long roleId) {
        Wrapper<RbacRolePrivilege> wrapper = rolePrivilegeDao.getWrapper();
        wrapper.eq(RbacRolePrivilege.Fields.roleId, roleId);
        List<RbacRolePrivilege> list = rolePrivilegeDao.list(wrapper);
        return ListUtil.toList(list, RbacRolePrivilegeBase::getPrivilegeId);
    }

    /**
     * 获取项目权限列表
     *
     * @return
     */
    @Override
    public List<RbacPrivilege> getList() {
        Wrapper<RbacPrivilege> wrapper = privilegeDao.getWrapper();
        wrapper.eq(RbacPrivilege.Fields.project, UriUtil.getUriResource().getModule());
        return privilegeDao.list(wrapper);
    }
}
