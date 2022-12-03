package work.sajor.crap.core.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * web资源配置
 * </p>
 *
 * @author Sajor
 * @since 2022-12-03
 */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Value("${crap.path.source}")
    private String sourcePath;

    /**
     * 静态资源目录设置为 crap.path.source/public
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("web root: " + sourcePath + "/public");
        registry.addResourceHandler("/**").addResourceLocations("file:" + sourcePath + "/public/");
    }
}
