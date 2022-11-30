package work.sajor.crap.core.session;

import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import work.sajor.crap.core.dao.dao.RbacPrivilegeDao;
import work.sajor.crap.core.security.dto.WebUser;

import java.util.concurrent.Callable;

/**
 * <p>
 * 系统会话管理
 * 在 http/cli/kafka/cron/socket 环境下处理请求前, 应当先使用 SessionUtil.call 设置会话用户
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Component
public class SessionUtil {

    protected static ThreadLocal<SessionContext> localSessionContext = new ThreadLocal<>();

    protected static RbacPrivilegeDao rbacPrivilegeDao;

    @Autowired
    protected void setRbacPrivilegeDao(RbacPrivilegeDao rbacPrivilegeDao) {
        SessionUtil.rbacPrivilegeDao = rbacPrivilegeDao;
    }

    /**
     * 在指定的 session 环境下运行
     */
    @SneakyThrows
    public static <T> T call(Callable<T> callable, WebUser user) {
        return call(callable, user, null);
    }

    /**
     * 在指定的 session 环境下运行
     */
    @SneakyThrows
    public static <T> T call(Callable<T> callable, WebUser user, String uri) {

        try {
            SessionContext session = new SessionContext();
            if (user == null) {
                user = new WebUser();
                user.setId(0L);
                user.setName("");
                user.setUsername("");
            }
            session.setUser(user);

            if (StrUtil.isNotEmpty(uri)) {
                session.setMenu(rbacPrivilegeDao.getByUri(uri));
            }

            localSessionContext.set(session);
            return callable.call();
        } finally {
            localSessionContext.set(null);
        }
    }

    /**
     * 获取当前用户
     */
    public static WebUser getUser() {
        if (localSessionContext.get() == null) {
            localSessionContext.set(new SessionContext());
        }
        return localSessionContext.get().getUser();
    }

    /**
     * 获取当前用户 id
     */
    public static Long getUserId() {
        WebUser user = getUser();
        return user == null ? 0L : user.getId();
    }

    /**
     * 获取当前用户 tid
     */
    public static Integer getUserTid() {
        WebUser user = getUser();
        return user == null ? 0 : user.getTid();
    }
}
