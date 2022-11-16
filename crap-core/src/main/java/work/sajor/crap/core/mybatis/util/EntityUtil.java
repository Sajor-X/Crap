package work.sajor.crap.core.mybatis.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.extern.slf4j.Slf4j;
import work.sajor.crap.core.mybatis.facade.FieldEnum;
import work.sajor.crap.core.web.WebException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class EntityUtil {
    
    /**
     * 清理属性
     */
    private static String[] cleanProps = {
        "id",
        "createUid",
        "createUname",
        "createTime",
        "updateUid",
        "updateUname",
        "updateTime",
        "tid"
    };
    
    /**
     * 获取主键值
     *
     * @param entity
     * @return
     */
    public static Long getID(Object entity) {
        return getID(entity, Long.class);
    }
    
    
    /**
     * 获取主键值
     *
     * @param entity
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getID(Object entity, Class<T> clazz) {
        Field idField = getIDField(entity);
        return Convert.convert(clazz, ReflectUtil.getFieldValue(entity, idField));
    }
    
    /**
     * 获取主键字段名
     *
     * @param entity
     * @return
     */
    // todo cache
    public static String getIDName(Object entity) {
        Field idField = getIDField(entity);
        TableField annotation = idField.getAnnotation(TableField.class);
        return annotation == null ? "id" : annotation.value();
    }
    
    /**
     * 设置主键值
     *
     * @param entity
     * @param id
     * @param <T>
     */
    public static <T> void setID(T entity, Object id) {
        Field idField = getIDField(entity);
        ReflectUtil.setFieldValue(entity, idField, Convert.convert(idField.getType(), id));
    }
    
    /**
     * 获取主键属性
     *
     * @param entity
     * @return
     */
    // todo cache
    private static Field getIDField(Object entity) {
        Field[] fields = ReflectUtil.getFields(entity.getClass());
        for (Field field : fields) {
            TableId annotation = field.getAnnotation(TableId.class);
            if (annotation != null) {
                return field;
            }
        }
        throw new WebException("Can not resolve pk field");
    }
    
    /**
     * 将 value 值转换为枚举值
     * 适用 FieldEnum
     */
    @SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
    public static <T extends Enum> T getEnum(Class<T> enumClass, Object value) {
        
        // 遍历所有枚举值, 查找与 FieldEnum.value 相同的值
        AtomicReference<T> e = new AtomicReference<>();
        LinkedHashMap<String, Enum> enumMap = EnumUtil.getEnumMap((Class<Enum>) enumClass);
        enumMap.entrySet().stream().anyMatch(stringEnumEntry -> {
            Enum enumItem = stringEnumEntry.getValue();
            Serializable enumValue = ((FieldEnum) enumItem).getValue();
            if (enumValue.equals(Convert.convert(enumValue.getClass(), value))) {
                e.set((T) enumItem);
                return true;
            }
            return false;
        });
        return e.get();
    }
    
    
    /**
     * 清理实体 id, 创建时间等属性
     */
    public static void clean(Object object) {
        if (object != null) {
            for (String prop : cleanProps) {
                try {
                    ReflectUtil.setFieldValue(object, prop, null);
                } catch (Exception ignore) {}
            }
        }
    }
}
