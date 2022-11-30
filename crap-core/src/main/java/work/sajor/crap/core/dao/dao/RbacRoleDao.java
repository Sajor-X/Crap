package work.sajor.crap.core.dao.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import work.sajor.crap.core.dao.entity.RbacRole;
import work.sajor.crap.core.dao.mapper.RbacRoleMapper;
import work.sajor.crap.core.dict.DictUtil;
import work.sajor.crap.core.mybatis.dao.EventDao;
import work.sajor.crap.core.security.facade.WebUserService;

import javax.annotation.PostConstruct;

/**
 * RBAC 角色 Dao
 *
 * @author Sajor
 * @since 2022-11-28
 */
@Repository
public class RbacRoleDao extends EventDao<RbacRoleMapper, RbacRole> {

    @Autowired
    WebUserService webUserService;

    /**
     * 注册字典
     */
    @SuppressWarnings("unchecked")
    @PostConstruct
    protected void construct() {
        DictUtil.register("role", wrapper -> {
            wrapper = wrapper == null ? getWrapper() : wrapper;
            wrapper.orderByAsc(RbacRole.Fields.id);
            wrapper.eq(RbacRole.Fields.status, 1);
            return list(wrapper);
        });
    }

    /**
     * 数据变更时清空缓存
     */
    @Override
    public void onChange(Long id) {
        webUserService.reload();
    }
}
