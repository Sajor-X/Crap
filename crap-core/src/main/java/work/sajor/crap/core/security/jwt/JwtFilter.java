package work.sajor.crap.core.security.jwt;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import work.sajor.crap.core.security.config.SecurityConfig;
import work.sajor.crap.core.security.dto.UriResource;
import work.sajor.crap.core.security.dto.WebUser;
import work.sajor.crap.core.security.facade.WebUserService;
import work.sajor.crap.core.security.util.UriUtil;
import work.sajor.crap.core.web.WebUtil;
import work.sajor.crap.core.web.dto.JsonResponse;
import work.sajor.crap.core.web.response.ResponseBuilder;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * jwt 过滤
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    SecurityConfig securityConfig;

    @Autowired
    JwtManager jwtManager;

    @Autowired
    WebUserService webUserService;

    /**
     * 允许匿名访问的规则
     */
    List<AntPathRequestMatcher> matchers;

    @PostConstruct
    protected void construct() {
        matchers = new ArrayList<>();
        matchers.add(new AntPathRequestMatcher("/**/*.*"));             // 静态文件
        for (String anonymousUrl : securityConfig.getAnonymousUrls()) {
            matchers.add(new AntPathRequestMatcher(anonymousUrl));
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // if (checkAnonymousUrl(request)) {
        //     filterChain.doFilter(request, response);
        //     return;
        // }

        try {
            Long userId = jwtManager.load(request);
            if (userId != null) {
                // 注册当前用户
                UriResource uriResource = UriUtil.getUriResource();
                WebUser webUser = webUserService.get(userId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(webUser, null, webUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (JwtException e) {

            if (checkAnonymousUrl(request)) {                                   // 公开的地址也尝试加载用户信息
                filterChain.doFilter(request, response);                        // webUserService.get(userId) 用户 id 无效时会抛出异常, 无法访问公开地址
                return;
            }

            WebUtil.sendJson(response, ResponseBuilder.error(e.getMessage(), JsonResponse.Status.UNAUTHORIZED));
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.sendJson(response, ResponseBuilder.error("系统异常"));
        }
    }

    /**
     * 检查当前请求是否允许匿名访问
     */
    protected boolean checkAnonymousUrl(HttpServletRequest request) {
        for (AntPathRequestMatcher matcher : matchers) {
            if (matcher.matches(request)) {
                return true;
            }
        }
        return false;
    }
}
