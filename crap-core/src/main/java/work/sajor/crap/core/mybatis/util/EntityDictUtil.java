package work.sajor.crap.core.mybatis.util;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ReflectUtil;
import work.sajor.crap.core.dict.DictUtil;
import work.sajor.crap.core.mybatis.facade.FieldEnum;
import work.sajor.crap.core.web.WebDict;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提取实体类中的 FieldEnum 属性, 转化成字典
 */
public class EntityDictUtil {
    
    /**
     * 字典缓存
     */
    private static ConcurrentHashMap<Class, Map<String, Map<FieldEnum, Object>>> dictCache = new ConcurrentHashMap<>();
    
    /**
     * 获取实体内定义的 enum 字典值
     */
    public static WebDict getDict(Object entity) {
        return getDict(entity.getClass());
    }
    
    /**
     * 获取实体内定义的 enum 字典值
     */
    public static WebDict getDict(Class clazz) {
        return new WebDict(dictCache.getOrDefault(clazz, Collections.emptyMap()));
    }
    
    /**
     * 注册实体类内的枚举字典
     */
    @SuppressWarnings("unchecked")
    public static void registerDict(Class clazz) {
        Map<String, Map<FieldEnum, Object>> dict = new HashMap<>();
        for (Field field : ReflectUtil.getFields(clazz)) {
            if (EnumUtil.isEnum(field.getType())) {
                Map<FieldEnum, Object> fieldDict = enumToDict((Class<? extends Enum>) field.getType());
                dict.put(field.getName(), fieldDict);                           // 按 class 缓存
                DictUtil.register(field.getType(), wrapper -> fieldDict);       // 全局注册
            }
        }
        dictCache.put(clazz, dict);
    }
    
    /**
     * 获取属性 enum 字典值
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum> Map<FieldEnum, Object> enumToDict(Class<T> clazz) {
        Map<FieldEnum, Object> map = new LinkedHashMap<>();
        EnumUtil.getEnumMap(clazz).forEach((s, e) -> {
            try {
                map.put((FieldEnum) e, ((FieldEnum) e).getName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return map;
    }
    
}
