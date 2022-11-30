package work.sajor.crap.core.dao.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import work.sajor.crap.core.dao.entity.RbacRolePrivilege;
import work.sajor.crap.core.dao.entity.RbacUserRole;
import work.sajor.crap.core.dao.mapper.RbacUserRoleMapper;
import work.sajor.crap.core.mybatis.dao.EventDao;
import work.sajor.crap.core.mybatis.support.Wrapper;
import work.sajor.crap.core.security.facade.WebUserService;
import work.sajor.crap.core.util.ListUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RBAC 用户-角色关联 Dao
 *
 * @author Sajor
 * @since 2022-11-28
 */
@Repository
public class RbacUserRoleDao extends EventDao<RbacUserRoleMapper, RbacUserRole> {


    @Autowired
    WebUserService webUserService;

    @Override
    public void onChange(Long id) {
        webUserService.reload();
    }

    /**
     * 用户权限列表
     */
    public List<Long> getPrivilegeIdList(Long userId) {
        Wrapper<RbacUserRole> wrapper = getWrapper();
        wrapper.setFields(new HashMap<String, String>() {{
            put(RbacRolePrivilege.Alias.privilegeId, "id");
        }});
        wrapper.setJoin("LEFT JOIN " + RbacRolePrivilege.Table + " ON " + RbacUserRole.Alias.roleId + "=" + RbacRolePrivilege.Alias.roleId);
        wrapper.eq(RbacUserRole.Fields.userId, userId);
        List<Map<String, Object>> list = listMap(wrapper);

        return ListUtil.toList(list, "id", Long.class);
    }
}