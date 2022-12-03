package work.sajor.crap.core.security.jwt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import work.sajor.crap.core.security.config.JwtConfig;
import work.sajor.crap.core.security.config.SecurityConfig;
import work.sajor.crap.core.security.facade.WebUserService;
import work.sajor.crap.core.web.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * <p>
 * Json Web Token 管理
 * create / load(refresh) / clear
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Component
@Slf4j
public class JwtManager {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtStorage jwtStorage;

    @Autowired
    WebUserService webUserService;


    /**
     * 创建 token
     *
     * @param userId
     * @param rememberMe
     * @return
     */
    public String create(Long userId, Boolean rememberMe) {

        // 允许多客户端同时登录, 复用 token, 同一个用户多次登录, 返回相同的 token
        if (securityConfig.isMultiSession()) {
            String currentToken = jwtStorage.reuse(userId, getFrom());
            if (StrUtil.isNotEmpty(currentToken)) {
                return currentToken;
            }
        }

        Date now = new Date();
        Integer ttl = rememberMe ? jwtConfig.getRememberMeExpire() : jwtConfig.getExpire();

        // -- jjwt JWS
        String token = Jwts.builder()
                .setId(RandomUtil.randomString(RandomUtil.BASE_CHAR, 10))    // 随机数 保证每次生成的token都不同
                .setSubject(userId.toString())                                      // 简化交互, 只存储 user_id
                .setIssuedAt(now)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .setExpiration(DateUtil.offsetMonth(now, 100)) // 永不过期, 有效期在持久层保存, 减少刷新交互
                .compact();

        jwtStorage.save(userId, token, ttl, getFrom());
        webUserService.reload(userId);
        return token;
    }

    /**
     * 获取当前用户 userId
     * <p>
     * todo 抛出异常时的响应前端处理有问题
     *
     * @param request
     * @return
     */
    @Nullable
    public Long load(HttpServletRequest request) {

        String token = getToken(request);
        if (StrUtil.isEmpty(token)) {
            return null;
        }

        // 检查 token 格式
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("token 已过期 : {}", token);
            throw new JwtException("登录状态已过期, 请重新登录 [0]");
        } catch (UnsupportedJwtException e) {
            log.warn("token 格式错误 : {}", token);
            throw new JwtException("登录状态已过期, 请重新登录 [1]");
        } catch (MalformedJwtException e) {
            log.warn("token 无效 : {}", token);
            throw new JwtException("登录状态已过期, 请重新登录 [2]");
        } catch (IllegalArgumentException e) {
            log.warn("token 不存在 : {}", token);
            throw new JwtException("登录状态已过期, 请重新登录 [3]");
        }
//        catch (Exception e) {
//            log.error("token 解析错误 : {}", token);
//            throw new JwtException("登录状态已过期, 请重新登录 [4]");
//        }

        // 检查本地存储
        Long userId = Long.valueOf(claims.getSubject());
        jwtStorage.check(userId, token, getFrom());

        return userId;
    }

    /**
     * 清空当前用户 token
     *
     * @param request
     */
    public Long clear(HttpServletRequest request) {
        Long userId = load(request);
        if (userId != null) {
            jwtStorage.remove(userId, getFrom());
            webUserService.reload(userId);
        }
        return userId;
    }

    private String getFrom() {
        String from = String.valueOf(WebUtil.getQueryParam("from"));
        if (StrUtil.isEmpty(from) || from.equals("null")) {
            from = "web";
        }
        return from;
    }

    private String getToken(HttpServletRequest request) {
        String token = "";

        // --- 优先从 header 里提取 token
        String bearerToken = request.getHeader("Authorization");
        if (StrUtil.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        // --- 从 query 里提取 token
        if (StrUtil.isEmpty(token)) {
            token = WebUtil.getQueryParam("token");
        }

        return token;
    }

    /**
     * 生成Key
     *
     * @return java.security.Key
     */
    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
