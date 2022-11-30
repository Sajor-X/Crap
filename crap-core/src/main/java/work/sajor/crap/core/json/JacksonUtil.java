package work.sajor.crap.core.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import work.sajor.crap.core.web.WebException;

import java.util.LinkedHashMap;

/**
 * <p>
 * jackson 序列化/反序列化
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Component
public class JacksonUtil {

    private static JacksonConfig jacksonConfig;

    @Autowired
    private void setJacksonConfig(JacksonConfig jacksonConfig) {
        JacksonUtil.jacksonConfig = jacksonConfig;
    }


    /**
     * 标准 json 序列化
     */
    public static String serialize(Object obj) {
        try {
            return jacksonConfig.getObjectMapper().writeValueAsString(obj);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new WebException("JSON 序列化失败", e);
        }
    }

    /**
     * typed json 序列化
     */
    public static String typedSerialize(Object obj) {
        try {
            return jacksonConfig.getTypeMapper().writeValueAsString(obj);
        } catch (Throwable e) {
            throw new WebException("JSON 序列化失败", e);
        }
    }

    /**
     * 反序列化
     */
    public static LinkedHashMap deserialize(String jsonString) {
        return deserialize(jsonString, LinkedHashMap.class);
    }

    /**
     * 反序列化
     * 类型是 Object.class 返回 LinkedHashMap
     */
    public static <T> T deserialize(String jsonString, Class<T> clazz) {
        try {
            return jacksonConfig.getObjectMapper().readValue(jsonString, clazz);
        } catch (Throwable e) {
            throw new WebException("JSON 反序列化失败", e);
        }
    }

    /**
     * 反序列化 (typed json)
     * 反序列化失败后尝试使用标准反序列化
     */
    public static Object typedDeserialize(String jsonString) {
        try {
            return jacksonConfig.getTypeMapper().readValue(jsonString, Object.class);
        } catch (Throwable e) {
            return deserialize(jsonString, Object.class);
        }
    }
}
