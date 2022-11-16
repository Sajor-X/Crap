package work.sajor.crap.core.web;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import work.sajor.crap.core.dict.Dict;
import work.sajor.crap.core.dict.DictUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 字典
 * </p>
 *
 * @author Sajor
 * @since 2022-11-15
 */
public class WebDict {

    @JsonValue
    @Getter
    private Map<String, Object> dictMap = new ConcurrentHashMap<>();

    public WebDict() {
    }

    @SuppressWarnings("unchecked")
    public WebDict(Map map) {
        dictMap.putAll(map);
    }

    /**
     * 注册标准字典
     */
    public void put(String key) {
        put(key, key);
    }

    /**
     * 注册标准字典
     */
    public void put(String key, String dict) {
        put(key, DictUtil.load(dict));
    }

    /**
     * 注册标准字典
     */
    public void put(String key, Dict dict) {
        dictMap.put(key, dict);
    }
}
