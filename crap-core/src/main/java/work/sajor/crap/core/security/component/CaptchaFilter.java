package work.sajor.crap.core.security.component;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import work.sajor.crap.core.redis.RedisUtil;
import work.sajor.crap.core.security.config.SecurityConfig;
import work.sajor.crap.core.web.WebUtil;
import work.sajor.crap.core.web.response.ResponseBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 验证码过滤
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Component
@Slf4j
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    SecurityConfig securityConfig;

    /**
     * 验证码缓存前缀
     */
    private static final String codeStoreKey = "cache:verify::";

    /**
     * Ticket query key
     * 验证码绑定到客户端提交的 ticket 上, 不使用 session
     */
    @Getter
    private static final String requestTicketKey = "ticket";

    /**
     * 验证码 query key
     */
    @Getter
    private static final String requestCodeKey = "verify";

    /**
     * 验证码字符集
     * 排除易混淆字符
     */
    private static final String charset = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 验证码 url
        if (request.getRequestURI().equals(request.getContextPath() + securityConfig.getCaptchaUrl()) && request.getMethod().equals("GET")) {
            printCaptcha(request, response);
            return;
        } else if (request.getRequestURI().equals(request.getContextPath() + securityConfig.getCaptchaUrl()) && request.getMethod().equals("POST")) {
            jsonCaptcha(request, response);
            return;
        }

        // 登录 url
        if (request.getRequestURI().equals(request.getContextPath() + securityConfig.getLoginUrl()) && request.getMethod().equals("POST")) {
            verify(request, response, filterChain);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void jsonCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO : 外部获取图片尺寸, 限制宽高范围
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(Integer.parseInt(WebUtil.getQueryParam("width"))
                , Integer.parseInt(WebUtil.getQueryParam("height")));
        lineCaptcha.setGenerator(new RandomGenerator(charset, 4));
        // 验证码有效期 10 分钟
        String ticket = WebUtil.getQueryParam(requestTicketKey);
        if (StrUtil.isNotEmpty(ticket)) {
            RedisUtil.set(codeStoreKey + "" + ticket, lineCaptcha.getCode(), 600);
            log.info("验证码 {}->{}", ticket, lineCaptcha.getCode());
        }
        WebUtil.sendJson(response, ResponseBuilder.success(lineCaptcha.getImageBase64Data()));
    }

    /**
     * 输出验证码图片
     *
     * @param request
     * @param response
     */
    private void printCaptcha(HttpServletRequest request, HttpServletResponse response) {
        try {

            // TODO : 外部获取图片尺寸, 限制宽高范围
            LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(240, 92);
            lineCaptcha.setGenerator(new RandomGenerator(charset, 4));

            // 验证码有效期 10 分钟
            String ticket = WebUtil.getQueryParam(requestTicketKey);
            if (StrUtil.isNotEmpty(ticket)) {
                RedisUtil.set(codeStoreKey + "" + ticket, lineCaptcha.getCode(), 600);
                log.info("验证码 {}->{}", ticket, lineCaptcha.getCode());
            }

            // 打印
            lineCaptcha.write(response.getOutputStream());
        } catch (IOException ignore) {
        }
    }

    /**
     * 校验验证码
     * 不支持 json body
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws IOException
     */
    private void verify(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // ticket 对应的验证码
        String redisKey = codeStoreKey + "" + WebUtil.getQueryParam(requestTicketKey);
        String sessionCode = RedisUtil.get(redisKey);

        // 验证码只能使用一次
        if (!StrUtil.isEmpty(sessionCode)) {
            RedisUtil.del(redisKey);
        }

        // 提交的值
        String requestCode = WebUtil.getQueryParam(requestCodeKey);

        // 没开验证码直接跳过
        if (securityConfig.isVerifyEnable() && (StrUtil.isEmpty(requestCode) || !requestCode.equalsIgnoreCase(sessionCode))) {
            WebUtil.sendJson(response, ResponseBuilder.error("验证码错误"));
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
