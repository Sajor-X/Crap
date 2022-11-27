package work.sajor.crap.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 收集配置
 * </p>
 *
 * @author Sajor
 * @since 2022-11-21
 */
//@Configuration
@Data
@Component
public class ApplicationConfig {

    private static ApplicationConfig self;

//    @Autowired
//    private void setApplicationConfig(ApplicationConfig applicationConfig) {
//        self = applicationConfig;
//    }

    /**
     * 应用节点标识
     */
    @Value("${crap.id}")
    private String nodeId;

    @Value("${jasypt.encryptor.password}")
    private String datasourceKey;

    // ------------------------------ 临时目录 ------------------------------

    @Value("${crap.path.tmp}")
    private String tmpPath = "/tmp";

    // ------------------------------ static method ------------------------------

    public static ApplicationConfig get() {
        return self == null ? new ApplicationConfig() : self;
    }
}
