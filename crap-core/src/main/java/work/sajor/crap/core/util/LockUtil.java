package work.sajor.crap.core.util;

import cn.hutool.core.thread.ThreadUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.curator.framework.imps.CuratorFrameworkState;
//import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.stereotype.Component;
import work.sajor.crap.core.redis.RedisUtil;

import java.time.LocalDateTime;

/**
 * <p>
 * 在全局锁下执行
 * </p>
 *
 * @author Sajor
 * @since 2022-12-03
 */
@Component
@Slf4j
public class LockUtil {

//    private static final String zookeeperLockPrefix = "/crap/lock/";

    /**
     * redis 锁最长存在时间(s), 防止进程异常退出未释放锁
     * 使用 redisson 开启 watchDog 可以解决这个问题
     * <p>
     * 不可重入
     * <p>
     * 默认使用 zookeeper 锁, redis 只是备用
     */
    private static int maxRedisLockSecond = 600;

    /**
     * 默认阻塞等待时间(s)
     */
    private static int maxWaitSecond = 60;

    /**
     * 执行, 阻塞等待 60s
     */
    public static boolean run(String key, Runnable runnable) {
        return run(key, runnable, maxWaitSecond * 1000);
    }

    /**
     * 执行, 指定等待时间(ms)
     * maxWaitMillionSecond=0 : 不超时
     * 加锁失败返回 false
     */
    @SneakyThrows
    public static boolean run(String key, Runnable runnable, int maxWaitMillionSecond) {
//        CuratorFramework client = ZookeeperUtil.getClient();
//        if (client.getState().equals(CuratorFrameworkState.STARTED)) {
//            return runWithZookeeper(key, runnable, maxWaitMillionSecond);
//        } else {
            return runWithRedis(key, runnable, maxWaitMillionSecond);
//        }
    }

    /**
     * 使用 redis 锁
     * todo: redisson
     */
    public static boolean runWithRedis(String key, Runnable runnable, int maxWaitMillionSecond) {
        boolean lockFlag = false;
        try {
            LocalDateTime maxTime = LocalDateTime.now().plusSeconds(maxWaitMillionSecond / 1000);
            do {
                if (RedisUtil.setnx(key, LocalDateUtil.toString(true), maxRedisLockSecond)) {
                    lockFlag = true;
                    break;
                }
                ThreadUtil.sleep(10);
            } while (maxTime.isAfter(LocalDateTime.now()) || maxWaitMillionSecond == 0);

            if (!lockFlag) {
                return false;
            }

            runnable.run();

            return true;
        } finally {
            if (lockFlag) {                                                     // 释放锁
                RedisUtil.del(key);
            }
        }
    }

//    @SneakyThrows
//    public static boolean runWithZookeeper(String key, Runnable runnable, int maxWaitMillionSecond) {
//        InterProcessMutex lock = null;
//
//        try {
//            lock = ZookeeperUtil.getLock(zookeeperLockPrefix + key, maxWaitMillionSecond);
//            if (lock == null) {
//                return false;
//            }
//
//            runnable.run();
//
//            return true;
//        } finally {
//            if (lock != null) {
//                lock.release();
//            }
//        }
//    }
}
