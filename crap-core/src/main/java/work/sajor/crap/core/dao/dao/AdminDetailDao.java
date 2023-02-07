package work.sajor.crap.core.dao.dao;

import work.sajor.crap.core.mybatis.dao.BaseDao;
import work.sajor.crap.core.dao.entity.AdminDetail;
import work.sajor.crap.core.dao.mapper.AdminDetailMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

/**
 *  Dao
 *
 * @author Sajor
 * @since 2023-02-07
 */
@Repository
@CacheConfig(cacheNames = "AdminDetail", keyGenerator = "cacheKeyGenerator")
public class AdminDetailDao extends BaseDao<AdminDetailMapper, AdminDetail> {

}
