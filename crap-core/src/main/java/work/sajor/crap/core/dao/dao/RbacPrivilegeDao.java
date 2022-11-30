package work.sajor.crap.core.dao.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import work.sajor.crap.core.dao.entity.RbacPrivilege;
import work.sajor.crap.core.dao.mapper.RbacPrivilegeMapper;
import work.sajor.crap.core.dict.Dict;
import work.sajor.crap.core.dict.DictLoader;
import work.sajor.crap.core.dict.DictUtil;
import work.sajor.crap.core.mybatis.dao.EventDao;
import work.sajor.crap.core.mybatis.support.Wrapper;
import work.sajor.crap.core.redis.CacheFlush;
import work.sajor.crap.core.security.facade.WebUserService;
import work.sajor.crap.core.security.util.UriUtil;
import work.sajor.crap.core.util.MapUtil;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * RBAC 权限 Dao
 *
 * @author Sajor
 * @since 2022-11-28
 */
@Repository
@CacheConfig(cacheNames = "RbacPrivilege", keyGenerator = "cacheKeyGenerator")
public class RbacPrivilegeDao extends EventDao<RbacPrivilegeMapper, RbacPrivilege> implements CacheFlush {

    @Autowired
    WebUserService webUserService;

    @Lazy
    @Autowired
    RbacPrivilegeDao self;

    /**
     * 注册字典
     */
    @PostConstruct
    protected void construct() {
        DictUtil.register("menu", new DictLoader() {
            @Override
            public Dict load(Wrapper wrapper) {
                Dict dict = new Dict(self.getMenuList());
                dict.setAllFields(true);
                return dict;
            }
        });
    }

    @Override
    @CacheEvict(allEntries = true)
    public void onChange(Long id) {
        webUserService.reload();
    }

    /**
     * uri -> id
     */
    public Map<String, Long> getDict() {
        List<RbacPrivilege> list = self.getMenuList();
        return MapUtil.toMap(list, RbacPrivilege.Fields.resource, String.class, RbacPrivilege.Fields.id, Long.class);
    }

    /**
     * 获取菜单列表
     */
    @Cacheable
    public List<RbacPrivilege> getMenuList() {
        Wrapper<RbacPrivilege> wrapper = getWrapper();
        wrapper.eq(RbacPrivilege.Fields.status, 1);
        wrapper.orderByDesc(RbacPrivilege.Fields.sort);
        wrapper.orderByAsc(RbacPrivilege.Fields.id);
        return list(wrapper);
    }

    @Cacheable
    public RbacPrivilege getByUri(String uri) {
        Long privilegeId = getDict().get(UriUtil.getUriResource(uri).getResource());
        return privilegeId == null ? null : getById(privilegeId);
    }


}
