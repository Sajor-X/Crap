package work.sajor.crap.core.dao.dao;

import work.sajor.crap.core.mybatis.dao.BaseDao;
import work.sajor.crap.core.dao.entity.RbacToken;
import work.sajor.crap.core.dao.mapper.RbacTokenMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

/**
 * RBAC 登录令牌 Dao
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Repository
@CacheConfig(cacheNames = "RbacToken", keyGenerator = "cacheKeyGenerator")
public class RbacTokenDao extends BaseDao<RbacTokenMapper, RbacToken> {

}
