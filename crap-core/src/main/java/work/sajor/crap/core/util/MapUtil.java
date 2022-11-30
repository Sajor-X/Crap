package work.sajor.crap.core.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import work.sajor.crap.core.logger.LogUtil;
import work.sajor.crap.core.mybatis.facade.FieldEnum;
import work.sajor.crap.core.web.WebException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * map工具
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Slf4j
public class MapUtil {

    // ------------------------------ list to map ------------------------------

    /**
     * list 转 map
     * 使用指定属性作为 key
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> toMap(List<V> list, String property, Class<K> clazz) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        for (V item : list) {
            map.put((K) BeanUtil.getFieldValue(item, property), item);
        }
        return map;
    }

    /**
     * list 转 map
     * 提取指定属性转换 k/v map
     * 由于构建字典映射
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> toMap(List list, String keyProperty, Class<K> keyClazz, String valueProperty, Class<V> valueClazz) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        for (Object item : list) {
            map.put((K) BeanUtil.getFieldValue(item, keyProperty), (V) BeanUtil.getFieldValue(item, valueProperty));
        }
        return map;
    }

    // ------------------------------ bean to map ------------------------------

    /**
     * bean 转 map
     * 不递归处理
     * 优先使用 public getter 作为 value
     * todo 有父类 getter 时会取错
     */
    public static Map<String, Object> toMap(Object bean) {
        return toMap(bean, true, false);
    }

    /**
     * bean 转 map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object bean, boolean skipStaticField, boolean useJsonProperty) {
        if (bean == null) {
            return null;
        }

        if (bean instanceof Map) {
            return (Map<String, Object>) bean;
        }

        Map<String, Object> map = new HashMap<>();

        Field[] fields = ReflectUtil.getFields(bean.getClass());
        for (Field field : fields) {

            // --- skip static field ---
            if (skipStaticField && Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            // --- key ---
            String key = field.getName();
            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            if (useJsonProperty && jsonProperty != null) {
                key = jsonProperty.value();
            }

            // --- value ---
            Object value;
            Method method = ReflectUtil.getMethodIgnoreCase(bean.getClass(), "get" + field);
            if (method != null && method.isAccessible()) {
                value = ReflectUtil.invoke(bean, method);
            } else {
                value = ReflectUtil.getFieldValue(bean, field);
            }
            map.put(key, value);
        }

        return map;
    }

    /**
     * 优先使用 @JsonProperty 作为 key
     */
    public static Map<String, Object> toJsonMap(Object bean) {
        return toMap(bean, true, true);
    }

    /**
     * 优先使用 @JsonProperty 作为 key
     */
    public static Map<String, Object> toJsonMap(Object bean, boolean skipStaticField) {
        return toMap(bean, skipStaticField, true);
    }

    /**
     * List<?> 转 List<Map>
     */
    public static List<Map<String, Object>> toMap(List<?> list) {
        return list.stream().map(MapUtil::toJsonMap).collect(Collectors.toList());
    }

    // ------------------------------ page to map ------------------------------

    /**
     * Page<?> 转 Page<Map>
     */
    public static Page<Map<String, Object>> toMap(Page page) {
        Page<Map<String, Object>> newPage = new Page<>();
        newPage.setRecords(toMap(page.getRecords()));
        newPage.setTotal(page.getTotal());
        newPage.setSize(page.getSize());
        newPage.setCurrent(page.getCurrent());
        return newPage;
    }

    // ------------------------------ map to bean ------------------------------

    /**
     * Map 转 Bean
     */
    public static <T> T toBean(Map<String, Object> map, Class<T> beanClass) {
        return toBean(map, beanClass, value -> value);
    }

    /**
     * Map 转 Bean
     */
    @SneakyThrows
    public static <T> T toBean(Object mapOrObject, Class<T> beanClass, MapToBeanValueConverter converter) {
        if (ClassUtil.isBasicType(beanClass)) {                                 // 包装类型
            return Convert.convert(beanClass, mapOrObject);
        }

        T entity = beanClass.newInstance();
        for (Field field : ReflectUtil.getFields(beanClass)) {
            if (Modifier.isFinal(field.getModifiers())) {                       // final 修饰
                continue;
            }
            Object fieldValue = getFieldValue(field, mapOrObject, converter);
            if (fieldValue == null) {                                           // 防止对基本类型设置 null 值
                continue;
            }

            ReflectUtil.setFieldValue(entity, field, fieldValue);
        }
        return entity;
    }

    /**
     * 提取属性值
     */
    @SuppressWarnings("unchecked")
    private static Object getFieldValue(Field field, Object source, MapToBeanValueConverter valueConverter) {

        // --- 查找顺序 ---
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add(field.getName());                                        // 1. 属性名

        JsonProperty jsonAnnotation = field.getAnnotation(JsonProperty.class);  // 2. json 键名
        fieldNames.add(jsonAnnotation == null ? null : jsonAnnotation.value());

        TableField fieldAnnotation = field.getAnnotation(TableField.class);     // 3. 实体字段名
        fieldNames.add(fieldAnnotation == null ? null : fieldAnnotation.value());

        fieldNames.add(StrUtil.toUnderlineCase(field.getName()));               // 4. 属性名转下划线

        fieldNames.add(StrUtil.toCamelCase(field.getName()));                   // 5. 属性名转驼峰

        // --- 提取值 ---
        Object value = null;
        Class<?> fieldType = field.getType();
        for (String name : fieldNames) {

            if (value != null) {                                                // 取到非 null 值停止
                break;
            }

            if (source instanceof Map) {
                Map sourceMap = (Map) source;
                if (sourceMap.containsKey(name)) {
                    value = sourceMap.get(name);
                }
                continue;
            }

            Method method = ReflectUtil.getMethodIgnoreCase(fieldType, "get" + name);
            if (method != null && method.isAccessible()) {                      // 优先使用 getter 取值
                value = ReflectUtil.invoke(source, method);
                continue;
            }

            value = ReflectUtil.getFieldValue(source, name);
        }

        // --- 枚举转换 ---
        if (EnumUtil.isEnum(fieldType)) {
            return getFieldEnumValue((Class<Enum>) fieldType, value);
        }

        // --- List 泛型转换 ---
        if (fieldType.isAssignableFrom(List.class)) {
            List<Object> listData = new ArrayList<>();
            try {
                Class<?> genericClass = GenericUtil.getFieldGenericType(field); // 泛型类型
                List<?> listValue = Convert.toList(value);

                if (genericClass != null) {
                    if (listValue != null) {
                        listValue.forEach(itemValue -> {
                            // 递归
                            listData.add(toBean(itemValue, genericClass, valueConverter));
                        });
                    }
                } else {                                                        // 未获取到泛型
                    return listValue;
                }
            } catch (Exception e) {
                throw new WebException("参数异常", e);
            }
            return listData;
        }

        // --- 回调转换 ---
        value = valueConverter.convert(value);

        return Convert.convert(fieldType, value);

    }

    /**
     * 获取 list 类型值
     */
    protected static Object getListValue(Field field, Object value, MapToBeanValueConverter valueConverter) {
        List<Object> listData = new ArrayList<>();
        try {
            Class<?> genericClass = GenericUtil.getFieldGenericType(field);       // 泛型类型
            List<?> listValue = Convert.toList(value);

            if (genericClass != null) {
                if (listValue != null) {
                    listValue.forEach(itemValue -> {
                        // 递归
                        listData.add(toBean(itemValue, genericClass, valueConverter));
                    });
                }
            } else {                                                            // 未获取到泛型
                return listValue;
            }
        } catch (Exception e) {
            throw new WebException("参数异常", e);
        }
        return listData;
    }

    /**
     * Enum 转换
     * FieldEnum.getValue() 或 FieldEnum.getName() 匹配即可
     */
    @SuppressWarnings("unchecked")
    protected static Object getFieldEnumValue(Class<Enum> enumClass, Object scalarValue) {

        // 枚举类型, 不需要转换
        if (scalarValue == null || EnumUtil.isEnum(scalarValue)) {
            return scalarValue;
        }

        // 遍历所有枚举值, 查找与 FieldEnum.value 或 FieldEnum.name 相同的值
        LinkedHashMap<String, Enum> enumMap = EnumUtil.getEnumMap(enumClass);
        for (Map.Entry<String, Enum> stringEnumEntry : enumMap.entrySet()) {
            Enum enumItem = stringEnumEntry.getValue();

            // --- 按名称转换 ---
            String stringName = enumItem.toString();
            if (scalarValue instanceof String) {
                String stringValue = (String) scalarValue;
                if (stringValue.equals(stringName) || stringValue.equals(stringName.toLowerCase())) {
                    return enumItem;
                }
            }

            // --- FieldEnum ---
            Serializable enumValue = null;
            Serializable enumName = null;
            try {
                enumValue = ((FieldEnum) enumItem).getValue();
                enumName = ((FieldEnum) enumItem).getName();
            } catch (Exception e) {
                continue;
            }

            try {
                if (enumValue.equals(Convert.convert(enumValue.getClass(), scalarValue))) {
                    return enumItem;
                }
                if (enumName.equals(Convert.convert(enumName.getClass(), scalarValue))) {
                    return enumItem;
                }
            } catch (Exception e) {
                log.warn("枚举类型转换失败 " + LogUtil.detail(e));
            }
        }
        return null;
    }

    public interface MapToBeanValueConverter {

        Object convert(Object value);
    }
}
