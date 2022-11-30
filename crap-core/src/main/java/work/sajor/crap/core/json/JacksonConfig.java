package work.sajor.crap.core.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * jackson配置
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Configuration
@Data
public class JacksonConfig {

    /**
     * 标准 json
     */
    private ObjectMapper objectMapper;

    /**
     * 包含类型信息, java 内部使用
     */
    private ObjectMapper typeMapper;

    /**
     * 初始化
     */
    @PostConstruct
    private void construct() {
        objectMapper = getBaseMapper();
        typeMapper = getBaseMapper();
        typeMapper.activateDefaultTyping(typeMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
    }

    /**
     * SpringMVC response 序列化处理器
     */
    @Bean
    protected Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        Map<Class<?>, JsonSerializer<?>> map = new HashMap<>();
        map.put(LocalDateTime.class, localDateTimeSerializer());                // 日期格式化
        map.put(Long.class, ToStringSerializer.instance);                       // Long->String : 防止 js 丢失精度
        return builder -> builder.serializersByType(map);
    }

    @Bean
    protected LocalDateTimeSerializer localDateTimeSerializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // ------------------------------ helper ------------------------------

    /**
     * 基础 objectMapper
     */
    private ObjectMapper getBaseMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 忽略多余字段

        mapper.setVisibility(
                mapper.getSerializationConfig().getDefaultVisibilityChecker()
                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)           // 所有属性都参与序列化
                        .withGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)  // 只使用 public getter
                        .withSetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)  // 只使用 public setter
                        .withCreatorVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY) // 只使用 public creator
                        .withIsGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)// 只使用 public isGetter
        );

        // --- LocalDateTime fix---
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);         // 解决 localDateTime 反序列化失败的问题
        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }

}
