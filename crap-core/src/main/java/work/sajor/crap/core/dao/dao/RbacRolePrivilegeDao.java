package work.sajor.crap.core.dao.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import work.sajor.crap.core.dao.entity.RbacRolePrivilege;
import work.sajor.crap.core.dao.mapper.RbacRolePrivilegeMapper;
import work.sajor.crap.core.mybatis.dao.EventDao;
import work.sajor.crap.core.security.facade.WebUserService;

/**
 * RBAC 角色-权限关联 Dao
 *
 * @author Sajor
 * @since 2022-11-28
 */
@Repository
public class RbacRolePrivilegeDao extends EventDao<RbacRolePrivilegeMapper, RbacRolePrivilege> {
    @Autowired
    WebUserService webUserService;

    @Override
    public void onChange(Long id) {
        webUserService.reload();
    }
}
