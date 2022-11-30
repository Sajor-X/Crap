package work.sajor.crap.core.dao.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import work.sajor.crap.core.dao.entity.RbacUser;
import work.sajor.crap.core.dao.entity.base.RbacUserBase;
import work.sajor.crap.core.dao.mapper.RbacUserMapper;
import work.sajor.crap.core.dict.DictUtil;
import work.sajor.crap.core.mybatis.dao.EventDao;
import work.sajor.crap.core.security.facade.WebUserService;

import javax.annotation.PostConstruct;

/**
 * RBAC 用户 Dao
 *
 * @author Sajor
 * @since 2022-11-28
 */
@Repository
public class RbacUserDao extends EventDao<RbacUserMapper, RbacUser> {

    /**
     * 注册字典
     */
    @SuppressWarnings("unchecked")
    @PostConstruct
    protected void construct() {
        DictUtil.register("user", wrapper -> {
            wrapper = wrapper == null ? getWrapper() : wrapper;
            wrapper.orderByAsc(RbacUser.Fields.id);
            wrapper.eq(RbacUserBase.Fields.status, 1);
            return list(wrapper);
        });
    }

    @Lazy
    @Autowired
    WebUserService webUserService;

    @Override
    public void onChange(Long id) {
        webUserService.reload(id);
    }
}
