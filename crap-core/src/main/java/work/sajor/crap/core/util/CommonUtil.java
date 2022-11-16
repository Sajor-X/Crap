package work.sajor.crap.core.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import work.sajor.crap.core.logger.LogUtil;

import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class CommonUtil {
    
    /**
     * 优先使用 getter 读取属性
     */
    public static Object getValue(Object bean, String fieldNameOrIndex) {
        return getValue(bean, fieldNameOrIndex, Object.class);
    }
    
    /**
     * 优先使用 getter 读取属性
     */
    public static <T> T getValue(Object bean, String fieldNameOrIndex, Class<T> clazz) {
        Object value = null;
        if (bean != null) {
            if (bean instanceof Map) {
                // map
            } else {
                Method method = ReflectUtil.getMethodIgnoreCase(bean.getClass(), "get" + fieldNameOrIndex);
                if (method != null) {
                    try {
                        value = ReflectUtil.invoke(bean, method);
                    } catch (UtilException e) {
                        log.error(LogUtil.detail("getFieldValue 错误", e));
                    }
                }
            }
            
            if (value == null) {
                value = BeanUtil.getFieldValue(bean, fieldNameOrIndex);
            }
        }
        return Convert.convert(clazz, value);
    }
    
    /**
     * 设置属性值
     * 优先使用 setter 设置属性
     */
    public static void setValue(Object bean, String field, Object value) {
        if (bean != null) {
            if (bean instanceof Map) {
                // map
            } else {
                Method method = ReflectUtil.getMethodIgnoreCase(bean.getClass(), "set" + field);
                if (method != null) {
                    try {
                        ReflectUtil.invoke(bean, method, value);
                    } catch (UtilException e) {
                        log.error(LogUtil.detail("setValue 错误", e));
                    }
                }
            }
        }
        BeanUtil.setFieldValue(bean, field, value);
    }
    
    /**
     * 清除字符串前后的空白和指定的字符
     */
    public static String trimString(CharSequence str, String trimList) {
        if (str == null) {
            return "";
        }
        
        int length = str.length();
        int start = 0;
        int end = length;
        
        // 扫描字符串头部
        while ((start < end) && (
            CharUtil.isBlankChar(str.charAt(start)) || StrUtil.contains(trimList, str.charAt(start))
        )) {
            start++;
        }
        
        // 扫描字符串尾部
        while ((start < end) && (
            CharUtil.isBlankChar(str.charAt(end - 1)) || StrUtil.contains(trimList, str.charAt(end - 1))
        )) {
            end--;
        }
        
        if ((start > 0) || (end < length)) {
            return str.toString().substring(start, end);
        }
        
        return str.toString();
    }
    
    /**
     * 基本类型 + 包装类型
     */
    public static boolean isBasicType(Object obj) {
        return obj != null && ClassUtil.isBasicType(obj.getClass());
    }
    
    /**
     * 基本类型 + 包装类型 + String
     */
    public static boolean isSimpleType(Object obj) {
        return obj != null && (ClassUtil.isBasicType(obj.getClass()) || obj instanceof String);
    }
}
