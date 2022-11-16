package work.sajor.crap.core.mybatis.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import work.sajor.crap.core.logger.LogUtil;
import work.sajor.crap.core.mybatis.facade.FieldEnum;
import work.sajor.crap.core.util.GenericUtil;
import work.sajor.crap.core.web.WebException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 将 json 反序列化后得到的结果映射到实体类
 * 将数据库自定义查询结果(Map)映射到实体类
 * 恢复 MybatisPlus 实体丢失的泛型属性, 比如使用 getOne() 方法获取的实体中, List<Bean> 属性实际存储的是 List<HashMap>, 通过 convert 方法恢复
 * 递归处理
 *
 * 1. 根据 @JsonProperty 解析字段映射关系
 * 2. 解析 FieldEnum 映射
 * 3. 集合类泛型保持
 */
@Slf4j
public class EntityConvertUtil {
    
    /**
     * 转换
     */
    @SneakyThrows
    public static <T> T convert(Class<T> entityClass, Object source) {
        
        if (ClassUtil.isBasicType(entityClass)) {                               // 包装类型
            return Convert.convert(entityClass, source);
        }
        
        T entity = entityClass.newInstance();
        for (Field field : ReflectUtil.getFields(entityClass)) {
            if (Modifier.isFinal(field.getModifiers())) {                       // final 修饰
                continue;
            }
            Object fieldValue = getFieldValue(field, source);
            if (fieldValue == null) {                                           // 非包装类型基本类型跳过 null
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
    private static Object getFieldValue(Field field, Object source) {
        
        // 使用 JsonProperty 映射 key
        JsonProperty annotation = field.getAnnotation(JsonProperty.class);
        String fieldName = field.getName();                                     // 字段名
        String jsonName = annotation == null ? fieldName : annotation.value();  // json 键名
        
        Object value = getSourceValue(source, jsonName);                        // 适用于前端提交或 sql 查询时字段名是下划线的情况
        if (value == null) {
            value = getSourceValue(source, fieldName);                          // 适用于恢复实体泛型属性的情况
        }
        
        Class<?> fieldType = field.getType();
        
        // 基本类型
        // if (ClassUtil.isBasicType(fieldType) || fieldType.isPrimitive() || fieldType.equals(String.class)) {
        //     return Convert.convert(fieldType, value);
        // }
        
        // List 泛型
        if (fieldType.isAssignableFrom(List.class)) {
            return getListValue(field, value);
        }
        
        // FieldEnum 反转
        if (EnumUtil.isEnum(fieldType) && ReflectUtil.getMethodByName(fieldType, "getValue") != null) {
            return getFieldEnumValue((Class<Enum>) fieldType, value);
        }
        
        return Convert.convert(fieldType, value);
        
        // 非基本类型嵌套
        // return convert(fieldType, value);
    }
    
    /**
     * 获取 list 类型值
     */
    @SuppressWarnings("unchecked")
    protected static Object getListValue(Field field, Object value) {
        
        List<Object> listData = new ArrayList<>();
        try {
            Class genericClass = GenericUtil.getFieldGenericType(field);       // 泛型类型
            List<?> listValue = Convert.toList(value);
            
            if (genericClass != null) {
                if (listValue != null) {
                    listValue.forEach(valueObject -> {
                        // 递归
                        listData.add(convert(genericClass, valueObject));
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
     * FieldEnum 转换
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
        for (Entry<String, Enum> stringEnumEntry : enumMap.entrySet()) {
            Enum enumItem = stringEnumEntry.getValue();
            Serializable enumValue = ((FieldEnum) enumItem).getValue();
            Serializable enumName = ((FieldEnum) enumItem).getName();
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
    
    private static Object getSourceValue(Object source, String fieldName) {
        if (source instanceof Map) {                                            // map 处理
            return ((Map) source).get(fieldName);
        }
        return ReflectUtil.getFieldValue(source, fieldName);
    }
}
