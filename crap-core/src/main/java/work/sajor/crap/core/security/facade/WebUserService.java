package work.sajor.crap.core.security.facade;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import work.sajor.crap.core.security.dto.WebUser;

/**
 * <p>
 * 用户服务
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
public interface WebUserService extends UserDetailsService {

    /**
     * 通过 id 获取用户
     */
    WebUser get(Long id) throws UsernameNotFoundException;

    /**
     * 刷新用户缓存
     */
    void reload(Long id);

    /**
     * 清空用户缓存
     */
    void reload();

    /**
     * 更新用户最后操作时间
     */
    void updateUserLastOpTime(Long id);
}