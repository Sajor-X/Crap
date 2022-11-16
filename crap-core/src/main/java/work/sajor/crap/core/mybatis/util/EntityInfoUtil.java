package work.sajor.crap.core.mybatis.util;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import work.sajor.crap.core.mybatis.support.CommonFields;
import work.sajor.crap.core.mybatis.support.EntityInfo;
import work.sajor.crap.core.mybatis.support.TableCode;
import work.sajor.crap.core.web.WebException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 实体类详细信息
 */
public class EntityInfoUtil {
    
    /**
     * 实体缓存
     */
    private static ConcurrentHashMap<Class, EntityInfo> entityCache = new ConcurrentHashMap<>();
    
    /**
     * 解析实体类, 提取表名, 字段名
     */
    @SuppressWarnings("unchecked")
    public static <T> EntityInfo<T> getInfo(Class<T> entityClass) {
        
        if (entityCache.containsKey(entityClass)) {
            return (EntityInfo<T>) entityCache.get(entityClass);
        }
        
        EntityInfo<T> entityInfo = new EntityInfo<>();
        
        // --- 提取表名 ---
        TableName tableName = (TableName) entityClass.getAnnotation(TableName.class);
        if (tableName == null) {
            throw new WebException(entityClass.getName() + "不是一个有效的实体");
        }
        String table = tableName.value();
        
        // --- 提取字段名 ---
        ArrayList<String> fieldList = new ArrayList<>();
        
        // --- 属性 / 字段名 映射
        HashMap<String, String> fieldMap = new HashMap<>();
        HashMap<String, String> propertyMap = new HashMap<>();
        HashMap<String, String> aliasMap = new HashMap<>();
        
        Field[] fieldsList = ReflectUtil.getFields(entityClass);
        for (Field field : fieldsList) {
            String fieldName = null;
            
            try {
                Method method = ReflectUtil.getMethodByName(entityClass, field.getName() + "Field");
                String fieldNameInJava = (String) method.invoke(null);
                if (StrUtil.isNotEmpty(fieldNameInJava)) {
                    fieldMap.put(fieldNameInJava, field.getName());             // java 字段名 -> 属性名
                    propertyMap.put(field.getName(), fieldNameInJava);          // 属性名 -> java 字段名
                }
            } catch (Exception ignore) {}
            
            TableField tableField = field.getAnnotation(TableField.class);
            
            // --- 逻辑删除 ---
            TableLogic tableLogic = field.getAnnotation(TableLogic.class);
            if (tableLogic != null) {                                           // 跳过逻辑删除字段
                entityInfo.setDeleteFlag(tableField.value());
                continue;
            }
            
            TableId tableId = field.getAnnotation(TableId.class);
            if (tableField != null && tableField.exist()) {
                fieldName = tableField.value();
            } else if (tableId != null) {
                fieldName = tableId.value();
                entityInfo.setPk(fieldName.replace("`", "")); // 主键名
            }
            if (fieldName != null) {
                fieldName = fieldName.replace("`", "");
                fieldList.add(fieldName);                                       // 字段名
                String alias = "`" + table + "." + fieldName + "`";             // 全限定别名
                aliasMap.put(table + ".`" + fieldName + "`", alias);            // 全限定名 -> 全限定别名 映射
                
                if (fieldName.equals(CommonFields.TID)) {                       // 有 tid
                    entityInfo.setHasTid(true);
                }
                
                if (fieldName.equals(CommonFields.SYS_VERSION)) {               // 有乐观锁
                    entityInfo.setHasVersion(true);
                }
                
                if (fieldName.equals(CommonFields.CODE)) {                      // 检查编码字段上标注的序列名称
                    entityInfo.setHasCode(true);
                    TableCode tableCode = field.getAnnotation(TableCode.class);
                    if (tableCode != null) {
                        entityInfo.setCodePrefix(tableCode.value());
                        if (tableCode.value().equals("false")) {
                            entityInfo.setHasCode(false);                       // code(false) 关闭自动生成
                        }
                    }
                }
            }
        }
        
        entityInfo.setClazz(entityClass);
        entityInfo.setTable(table);
        entityInfo.setFields(fieldList);
        entityInfo.setFieldMap(fieldMap);
        entityInfo.setPropertyMap(propertyMap);
        entityInfo.setAliasMap(aliasMap);
        
        entityCache.put(entityClass, entityInfo);
        
        return entityInfo;
    }
    
    /**
     * 根据表名返回实体类解析
     */
    public static EntityInfo getInfo(String tableName) {
        AtomicReference<EntityInfo> info = new AtomicReference<>();
        entityCache.forEach((entityClass, entityInfo) -> {
            if (entityInfo.getTable().equals(tableName)) {
                info.set(entityInfo);
            }
        });
        return info.get();
    }
}
