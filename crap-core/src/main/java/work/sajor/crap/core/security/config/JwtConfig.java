package work.sajor.crap.core.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * jwt配置
 * </p>
 *
 * @author Sajor
 * @since 2022-11-28
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "crap.security.jwt")
public class JwtConfig {

    /**
     * 加密 key
     */
    private String secret = "CRAP=JWT=SECRET=CRAP=JWT=SECRET=CRAP=JWT=SECRET=CRAP=JWT=SECRET";

    /**
     * 有效期
     * 默认 1 小时
     */
    private Integer expire = 3600;

    /**
     * 记住我有效期
     * 默认 30 天
     */
    private Integer rememberMeExpire = 2592000;

    /**
     * db 刷新间隔(s)
     * 默认 10 分钟
     */
    private Integer refresh = 600;
}
