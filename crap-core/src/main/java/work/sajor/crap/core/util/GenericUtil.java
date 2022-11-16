package work.sajor.crap.core.util;

import cn.hutool.core.util.TypeUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>
 * 泛型参数
 * </p>
 *
 * @author Sajor
 * @since 2022-11-14
 */
public class GenericUtil {

    /**
     * 获取父类泛型类型
     */
    public static Class getSuperClassGenericType(Class clazz) {
        return getSuperClassGenericType(clazz, 0);
    }

    /**
     * 获取父类泛型类型
     */
    public static Class getSuperClassGenericType(Class clazz, int index) {
        boolean flag = true;
        Type genType = clazz.getGenericSuperclass();
        Type[] params = null;

        if (!(genType instanceof ParameterizedType)) {
            flag = false;
        } else {
            params = ((ParameterizedType) genType).getActualTypeArguments();
            if (index >= params.length || index < 0) {
                flag = false;
            }
            if (!(params[index] instanceof Class)) {
                flag = false;
            }
        }
        if (!flag) {
            clazz = clazz.getSuperclass();
            if (clazz == Object.class) {
                return Object.class;
            } else {
                return getSuperClassGenericType(clazz, index);
            }
        }
        return (Class) params[index];
    }

    /**
     * 获取属性泛型
     */
    public static Class<?> getFieldGenericType(Field field) {
        return getFieldGenericType(field, 0);
    }

    /**
     * 获取属性泛型
     */
    public static Class<?> getFieldGenericType(Field field, Integer index) {
        try {
            ParameterizedType listGenericType = (ParameterizedType) field.getGenericType();
            Type[] listActualTypeArguments = listGenericType.getActualTypeArguments();
            return TypeUtil.getClass(listActualTypeArguments[index]);
        } catch (Exception e) {
            return null;
        }
    }
}
