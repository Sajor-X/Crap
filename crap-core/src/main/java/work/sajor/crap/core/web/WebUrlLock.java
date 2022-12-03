package work.sajor.crap.core.web;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import work.sajor.crap.core.session.SessionUtil;
import work.sajor.crap.core.util.LockUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * URL 请求锁
 * 主要用于防止前端访问接口时, 未锁定按钮等情况下, 用户多次点击并发造成的异常
 * - 防止并发访问新增接口时, 产生多条新数据
 * - 防止并发访问更新接口时, 同时开启的多个事务, 可能造成覆盖更新
 * - 不支持队列, 加锁失败即返回
 * </p>
 *
 * @author Sajor
 * @since 2022-12-03
 */
@Component
@Slf4j
public class WebUrlLock {

    private static final String KEY_PREFIX = "crap:url-lock:";

    @Autowired
    private HttpServletRequest request;

    @SneakyThrows
    public <T> T call(Callable<T> callable, boolean session) {
        String key = getKey(session);
        AtomicReference<T> result = new AtomicReference<>();

        boolean lockStatus = LockUtil.run(key, () -> {

            log.info("URL LOCK 加锁 : " + key);

            try {
                result.set(callable.call());
            } catch (Throwable e) {
                throw new RuntimeException(e.getMessage(), e);
            }

            log.info("URL LOCK 解锁 : " + key);

        }, 1);

        if (!lockStatus) {
            throw new WebException("当前数据正在处理中, 请稍后再试");
        }

        return result.get();
    }

    /**
     * 获取锁标识
     */
    private String getKey(boolean session) {
        String sessionKey = "";
        if (session) {
            sessionKey = ":" + SessionUtil.getUserId();
        }
        return KEY_PREFIX + request.getRequestURI() + sessionKey;
    }

    /**
     * 原接口抛出的是 Exception, 这里改成 Throwable, 与 aop 兼容
     */
    public interface Callable<V> {

        V call() throws Throwable;
    }
}
