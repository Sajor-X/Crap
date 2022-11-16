package work.sajor.crap.core.mybatis.support;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import lombok.Getter;
import lombok.Setter;
import work.sajor.crap.core.mybatis.facade.Entity;
import work.sajor.crap.core.mybatis.facade.FieldEnum;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * Wrapper拓展
 * </p>
 *
 * @author Sajor
 * @since 2022-11-10
 */
public class Wrapper<T> extends QueryWrapper<T> {

    /**
     * 指定查询字段, 字段名 => 别名, 在 listMap / pageMap 查询时有效
     */
    @Getter
    @Setter
    private Map<String, String> fields;

    /**
     * 连接条件, 在 listMap / pageMap 查询时有效
     */
    @Getter
    @Setter
    private String join;

    /**
     * 查询 sql 模板, 不包含 WHERE 和之后的子句, 用于 sqlMap / sqlPage 查询
     */
    @Getter
    @Setter
    private String sqlTemplate;

    // ------------------------------ constructor ------------------------------

    public Wrapper() {
        super();
    }

    /**
     * copy form super
     */
    private Wrapper(T entity, Class<T> entityClass, AtomicInteger paramNameSeq,
                    Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                    SharedString lastSql, SharedString sqlComment, SharedString sqlFirst) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
    }

    /**
     * copy form super
     */
    @Override
    protected Wrapper<T> instance() {
        return new Wrapper<>(getEntity(), getEntityClass(), paramNameSeq, paramNameValuePairs, new MergeSegments(),
                SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString());
    }

    // ------------------------------ methods ------------------------------

    /**
     * 添加查询条件
     */
    public Wrapper<T> addCondition(String column, SqlKeyword sqlKeyword, Object val) {

        return (Wrapper<T>) super.addCondition(true, columnToString(column), sqlKeyword, val);
//        return (Wrapper<T>) super.doIt(true, () -> columnToString(column), sqlKeyword, () -> formatSql("{0}", val));
    }

    /**
     * 调试用, 不能用于数据库查询
     */
    public String getSqlWhere() {
        AtomicReference<String> customSqlSegment = new AtomicReference<>(getCustomSqlSegment());
        Map<String, Object> paramNameValuePairs = getParamNameValuePairs();
        paramNameValuePairs.forEach((placeholder, value) -> {
            if (value instanceof String) {                                      // 字符串类型使用双引号包裹, 并转义内部双引号
                value = "\"" + StrUtil.replace((String) value, "\"", "\\\"") + "\"";
            }

            // todo value 是集合类型时的转义和包装处理
            customSqlSegment.set(StrUtil.replace(customSqlSegment.get(), "#{ew.paramNameValuePairs." + placeholder + "}", String.valueOf(value)));
        });

        // 需要自己拼接删除条件
        return customSqlSegment.get();
    }

    /**
     * eq 支持枚举类型
     */
//    public Wrapper<T> eq(String column, FieldEnum val) {
//        return (Wrapper<T>) doIt(true, () -> columnToString(column), SqlKeyword.EQ, () -> formatSql("{0}", val.getValue()));
//    }

    // ------------------------------ join ------------------------------

    /**
     * 将一个实体类的所有字段添加到查询 : table.id AS 'table.id'
     */
//    public void addEntityFields(Class<? extends Entity> entityClass) {
//        if (fields == null) {
//            fields = new HashMap<>();
//        }
//
//        EntityInfo<? extends Entity> info = EntityInfoUtil.getInfo(entityClass);
//        fields.putAll(info.getAliasMap());
//    }

    /**
     * 根据 @TableField 设置查询字段 : annotationValue AS 'propertyName'
     * eg :  @TableField("count(*)") Integer count => count(*) AS 'count'
     */
    public void addFields(Class beanClass) {

        if (fields == null) {
            fields = new HashMap<>();
        }

        java.lang.reflect.Field[] fieldsList = ReflectUtil.getFields(beanClass);
        for (Field field : fieldsList) {
            String fieldName = null;
            TableField tableField = field.getAnnotation(TableField.class);
            TableId tableId = field.getAnnotation(TableId.class);

            if (tableField != null) {
                fieldName = tableField.value();
            } else if (tableId != null) {
                fieldName = tableId.value();
            }

            if (fieldName != null) {
                fields.put(fieldName, field.getName());
            }
        }
    }

    /**
     * 设置查询字段 : 字段名=>别名
     */
    public void addFields(Map<String, String> fieldsMap) {
        if (fields == null) {
            fields = new HashMap<>();
        }
        fields.putAll(fieldsMap);
    }
}
