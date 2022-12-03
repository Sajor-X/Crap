package work.sajor.crap.core.security.util;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import work.sajor.crap.core.security.dto.WebUser;

/**
 * <p>
 * 会话工具
 * </p>
 *
 * @author Sajor
 * @since 2022-12-03
 */
@Component
public class SecurityUtil {

    /**
     * 获取当前基础用户
     */
    public static WebUser getPrinciple() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        return principal == null || principal.equals("anonymousUser") ? null : (WebUser) principal;
    }

}

