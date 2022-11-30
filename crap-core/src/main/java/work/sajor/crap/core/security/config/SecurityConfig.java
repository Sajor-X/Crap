package work.sajor.crap.core.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 权限配置
 * </p>
 *
 * @author Sajor
 * @since 2022-11-28
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "crap.security")
public class SecurityConfig {
    private boolean enable = true;

    /**
     * 是否开启验证码功能
     */
    private boolean verifyEnable = true;

    /**
     * 允许跨域的 Host, 多个用逗号分隔
     */
    private String crossOrigin = "*";

    /**
     * 允许多客户端登录, 测试环境开启, 共用 sys 账号
     */
    private boolean multiSession = false;

    /**
     * 开发者账号
     */
    private String devUser = "sys";

    /**
     * 登录 url
     */
    private String loginUrl = "/crap/auth/login";

    /**
     * 图形验证码 url
     */
    private String captchaUrl = "/crap/auth/captcha";

    /**
     * 退出 url
     */
    private String logoutUrl = "/crap/auth/logout";

    /**
     * 保持登录参数名
     */
    private String rememberMeKey = "remember";

    /**
     * 不需要登录的地址
     */
    private List<String> anonymousUrls = Collections.emptyList();

    /**
     * 不需要授权的地址
     * 登录即可访问
     */
    private List<String> publicUrls = Collections.emptyList();

    /**
     * 项目权限处理器, 允许子项目定制授权规则
     */
    private List<String> handler = Collections.emptyList();
}
