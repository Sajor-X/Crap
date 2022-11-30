package work.sajor.crap.core.redis;

import org.springframework.cache.annotation.CacheEvict;

/**
 * <p>
 * 标记使用了缓存的类, 用于在后台主动清理缓存
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
public interface CacheFlush {

    /**
     * 刷 缓存
     */
    @CacheEvict(allEntries = true)
    default void flushCache() {}
}
