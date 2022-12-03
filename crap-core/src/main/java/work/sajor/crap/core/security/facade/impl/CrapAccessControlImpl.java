package work.sajor.crap.core.security.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import work.sajor.crap.core.dao.dao.RbacPrivilegeDao;
import work.sajor.crap.core.dao.dao.RbacUserRoleDao;
import work.sajor.crap.core.security.dto.WebUser;
import work.sajor.crap.core.security.facade.ProjectAccessControl;
import work.sajor.crap.core.security.util.UriUtil;

import java.util.List;

/**
 * <p>
 * 权限控制
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Service
public class CrapAccessControlImpl implements ProjectAccessControl {

    @Lazy
    @Autowired
    RbacUserRoleDao rbacUserRoleDao;

    @Lazy
    @Autowired
    private RbacPrivilegeDao rbacPrivilegeDao;

    /**
     * 检查 rbac 权限
     */
    @Override
    public boolean check(Boolean check, WebUser webUser, String uri) {
        List<Long> privilegeIds = webUser.getPrivilegeIds();                    // 用户权限 id 列表
        Long privilegeId = rbacPrivilegeDao.getDict().get(UriUtil.getUriResource(uri).getResource()); // uri 对应的权限 id
        return privilegeId != null && privilegeIds.indexOf(privilegeId) != -1;
    }

    /**
     * 获取用户项目权限 ID 列表
     * 返回的权限与系统配置的权限合并, 构成完整的权限列表
     */
    @Override
    public List<Long> getPrivilegeIds(Long userId) {
        return rbacUserRoleDao.getPrivilegeIdList(userId);
    }
}
