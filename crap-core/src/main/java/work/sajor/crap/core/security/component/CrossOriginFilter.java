package work.sajor.crap.core.security.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 跨域过滤器
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Component
public class CrossOriginFilter extends OncePerRequestFilter {

    @Value("${crap.security.cross-origin}")
    private String origin;

    /**
     * 解决 Access-Control-Allow-Origin 值为 * 时, 不能携带 cookie 的问题
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin", origin.equals("*") ? request.getHeader("Origin") : origin);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "86400");              // 降低预检频率
        response.setHeader("Access-Control-Allow-Credentials", "true");     // 允许提交跨域 cookie
        response.setHeader("XDomainRequestAllowed", "1");
        response.setHeader("Access-Control-Allow-Headers", "*");

        if (!request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
        }
    }
}
