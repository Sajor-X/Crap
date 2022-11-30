package work.sajor.crap.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import work.sajor.crap.core.json.JacksonConfig;
import work.sajor.crap.core.session.SessionUtil;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * <p>
 * 缓存配置
 * </p>
 *
 * @author Sajor
 * @since 2022-11-28
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching
public class CacheConfig {

    @Value("${crap.cache-ttl}")
    private Duration ttl;

    @Autowired
    JacksonConfig jacksonConfig;

    /**
     * 配置使用注解的时候缓存配置，默认是序列化反序列化的形式，加上此配置则为 json 形式
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {

        // 配置序列化
        RedisCacheConfiguration config = RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(jacksonConfig.getTypeMapper())))
                .computePrefixWith(cacheName -> "cache:" + cacheName + "::")
                .entryTtl(ttl);

        return RedisCacheManager//.builder(factory)
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(factory)) // 非阻塞写入
                .cacheDefaults(config)
                .transactionAware()                             // 在spring事务正常提交时才缓存数据
                .build();
    }

    /**
     *
     * @return
     */
    @Bean
    public KeyGenerator cacheKeyGenerator() {
        return new SimpleKeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                return SessionUtil.getUserTid() + ":" + super.generate(target, method, params);
            }
        };
    }
}
