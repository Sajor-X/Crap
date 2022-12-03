package work.sajor.crap.core.dao.dao;

import work.sajor.crap.core.mybatis.dao.BaseDao;
import work.sajor.crap.core.dao.entity.OpLog;
import work.sajor.crap.core.dao.mapper.OpLogMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

/**
 * 数据变更日志 Dao
 *
 * @author Sajor
 * @since 2022-12-04
 */
@Repository
@CacheConfig(cacheNames = "OpLog", keyGenerator = "cacheKeyGenerator")
public class OpLogDao extends BaseDao<OpLogMapper, OpLog> {

}
