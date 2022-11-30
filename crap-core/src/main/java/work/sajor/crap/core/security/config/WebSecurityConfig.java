package work.sajor.crap.core.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import work.sajor.crap.core.security.component.CaptchaFilter;
import work.sajor.crap.core.security.component.CrossOriginFilter;
import work.sajor.crap.core.security.dto.WebUser;
import work.sajor.crap.core.security.facade.WebUserService;
import work.sajor.crap.core.security.jwt.JwtFilter;
import work.sajor.crap.core.security.jwt.JwtManager;
import work.sajor.crap.core.web.WebUtil;
import work.sajor.crap.core.web.dto.JsonResponse;
import work.sajor.crap.core.web.response.ResponseBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 权限配置
 * </p>
 *
 * @author Sajor
 * @since 2022-11-28
 */
@EnableWebSecurity
@Configuration
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private CaptchaFilter captchaFilter;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private CrossOriginFilter crossOriginFilter;

    // ****************************** Configure ******************************

    @Override
    @SuppressWarnings("all")
    protected void configure(HttpSecurity http) throws Exception {

        if (!securityConfig.isEnable()) {
            http.authorizeRequests().anyRequest().permitAll();
            return;
        }

        // 登录/验证码不需要登录访问
        http.authorizeRequests().antMatchers(securityConfig.getLoginUrl(), securityConfig.getCaptchaUrl()).permitAll();

        // 静态文件不需要登录访问
        http.authorizeRequests().antMatchers("/**/*.*").permitAll();

        // 在配置文件中指定的不需要登录访问的地址
        for (String anonymousUrl : securityConfig.getAnonymousUrls()) {
            http.authorizeRequests().antMatchers(anonymousUrl).permitAll();
        }

        // 所有 url 经过 rbac 权限过滤
        http.authorizeRequests()
                .anyRequest()
                .access("@accessControlHandler.check(authentication,request)");

        // 允许跨域
        // http.cors().configurationSource(CorsConfigurationSource());

        // 关闭 csrf
        http.csrf().disable();

        // 表单登录
        http.formLogin()
                .loginPage(securityConfig.getLoginUrl())
                .loginProcessingUrl(securityConfig.getLoginUrl())
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(this::loginSuccess)
                .failureHandler(this::loginFailure)
                .permitAll(); // 使用自定义登录地址时允许所有访问, 防止 302 死循环

        // 关闭 http base 认证
        http.httpBasic().disable();

        // 注销登录
        http.logout()
                .logoutUrl(securityConfig.getLogoutUrl())
                .invalidateHttpSession(true)
                .logoutSuccessHandler(this::logoutSuccess)
                .addLogoutHandler(this::logoutHandler)
                .deleteCookies("SESSION");

        // 关闭 session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 异常处理
        http.exceptionHandling()
                // 已登录, 未授权
                .accessDeniedHandler((request, response, e) -> {
                    WebUtil.sendJson(response, ResponseBuilder.error("", JsonResponse.Status.FORBIDDEN));
                })
                // 未登录
                .authenticationEntryPoint((request, response, authException) -> {
                    WebUtil.sendJson(response, ResponseBuilder.error("", JsonResponse.Status.UNAUTHORIZED));
                });


        // * Filter : 图形验证码
        http.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);

        // * Filter : Jwt
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // * Filter : 跨域拦截
        http.addFilterBefore(crossOriginFilter, CaptchaFilter.class);

        // url 验证规则, 按顺序验证, 遇到符合的规则即停止
        // http.authorizeRequests().antMatchers(loginUrl, captchaUrl).permitAll();
        // http.authorizeRequests().antMatchers("/**").access("@accessControlHandler.check(authentication,request)");
        // 添加自定义 JWT 过滤器
        // http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // http.authorizeRequests().accessDecisionManager(accessDecisionManager);
        // http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
        // @Override
        // public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
        // fsi.setSecurityMetadataSource(filterInvocationSecurityMetadataSource);
        // return fsi;}});
        // http.rememberMe().rememberMeParameter("remember").tokenValiditySeconds(30 * 24 * 3600).key("crap-remember-me");
        // http.logout()
        // .logoutUrl(logoutUrl)
        // .invalidateHttpSession(true)
        // .logoutSuccessHandler(this::logoutSuccess)
        // .addLogoutHandler((httpServletRequest, httpServletResponse, authentication) -> {})
        // .deleteCookies("SESSION");
    }

    /**
     * 用户适配
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(webUserService).passwordEncoder(passwordEncoder());
    }

    /**
     * 白名单
     */
    @Override
    public void configure(WebSecurity web) {
        // WebSecurity and = web.ignoring().and();
        // for (String anonymousUrl : anonymousUrls) {                             // 不需要登录的地址
        //     and.ignoring().antMatchers(servletContext + anonymousUrl);
        // }
        // and.ignoring().antMatchers("/**/*.*");                      // 忽略静态文件
    }

    // ****************************** Bean ******************************
    @Bean
    PasswordEncoder passwordEncoder() {
        return new SCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource CorsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");    // 同源配置，*表示任何请求都视为同源，若需指定ip和端口可以改为如“localhost：8080”，多个以“，”分隔；
        corsConfiguration.addAllowedHeader("*");    // header，允许哪些header，本案中使用的是token，此处可将*替换为token；
        corsConfiguration.addAllowedMethod("*");    // 允许的请求方法，POST、GET等
        source.registerCorsConfiguration("/**", corsConfiguration); // 配置允许跨域访问的url
        return source;
    }

    // ****************************** Handler ******************************

    // 登录成功回调
    public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
        WebUser principal = (WebUser) auth.getPrincipal();
        String token = jwtManager.create(principal.getId(), WebUtil.getQueryParam(securityConfig.getRememberMeKey(), Boolean.class));
        principal.setToken(token);

        webUserService.reload(principal.getId());

        // --- 登录日志 ---
//        OperationLogger.login();

        WebUtil.sendJson(response, ResponseBuilder.success(principal, "登录成功"));
    }

    // 登录失败回调
    public void loginFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        String message = "";
        if (e instanceof LockedException) {
            message = "账户被锁定";
        } else if (e instanceof DisabledException) {
            message = "账户被禁用";
        } else if (e instanceof BadCredentialsException) {
            message = "用户名或者密码输入错误";
        }
        WebUtil.sendJson(response, ResponseBuilder.error(message));
    }

    // 退出处理回调
    public void logoutHandler(HttpServletRequest request, HttpServletResponse response, Authentication auth) {

        Long userId = jwtManager.clear(request);
        if (userId != null) {
            // --- 登出日志 ---
//            OperationLogger.logout(userId);
        }
    }

    // 退出成功回调
    public void logoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
        WebUtil.sendJson(response, ResponseBuilder.success("注销成功"));
    }
//
//    public static void main(String[] args) {
//        System.out.println(new SCryptPasswordEncoder().encode("sys"));
//    }
}
