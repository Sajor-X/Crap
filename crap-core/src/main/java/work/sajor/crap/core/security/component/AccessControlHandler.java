package work.sajor.crap.core.security.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import work.sajor.crap.core.security.config.SecurityConfig;
import work.sajor.crap.core.security.dto.WebUser;
import work.sajor.crap.core.security.facade.ProjectAccessControl;
import work.sajor.crap.core.security.facade.WebUserService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限验证处理器
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Component
@Slf4j
public class AccessControlHandler {

    @Autowired
    private WebUserService webUserService;

    @Autowired
    SecurityConfig securityConfig;

    /**
     * 登录即可访问的规则, 免授权
     */
    List<AntPathRequestMatcher> matchers;

    /**
     * 权限验证回调
     */
    @Autowired
    private ProjectAccessControl[] accessHandler;

    @PostConstruct
    void construct() {
        matchers = new ArrayList<>();
        for (String anonymousUrl : securityConfig.getPublicUrls()) {
            matchers.add(new AntPathRequestMatcher(anonymousUrl));
        }
    }

    /**
     * 权限验证
     */
    @SuppressWarnings("ConstantConditions")
    public boolean check(Authentication authentication, HttpServletRequest request) {
        if (!securityConfig.isRolePermissionEnable()) {
            // 如果未开启角色权限功能，则直接放行
            return true;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof String && principal.equals("anonymousUser")) { // 未登录
            return false;
        }

        boolean status = false;
        for (AntPathRequestMatcher matcher : matchers) {                        // 免授权
            if (matcher.matches(request)) {
                status = true;
            }
        }

        return status || check(((WebUser) principal).getId(), request.getRequestURI());
    }

    /**
     * 检查用户是否有 uri 的访问权限
     */
    public boolean check(Long userId, String uri) {

        WebUser webUser = webUserService.get(userId);

        if (webUser.getUsername().equals(securityConfig.getDevUser())) {        // 开发者账号
            return true;
        }

        boolean status = false;
        for (ProjectAccessControl projectAccessControl : accessHandler) {       // 回调
            status = projectAccessControl.check(status, webUser, uri);
        }

        return status;
    }
}