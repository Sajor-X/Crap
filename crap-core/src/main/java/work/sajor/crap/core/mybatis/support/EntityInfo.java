package work.sajor.crap.core.mybatis.support;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 实体(表)信息
 * </p>
 *
 * @author Sajor
 * @since 2022-11-14
 */
@Data
public class EntityInfo<T> {

    // ------------------------------ 实体信息 ------------------------------

    /**
     * 实体类
     */
    private Class<T> clazz;

    /**
     * 全表名
     */
    private String table;

    /**
     * 主键字段名
     */
    private String pk;

    /**
     * 逻辑删除字段
     */
    private String deleteFlag;

    /**
     * 包含 tid
     */
    private Boolean hasTid = false;

    /**
     * 包含乐观锁
     */
    private Boolean hasVersion = false;

    /**
     * 包含编码列
     */
    private Boolean hasCode = false;

    /**
     * 编码前缀
     */
    private String codePrefix = "";

    // ------------------------------ sql 辅助 ------------------------------

    /**
     * 字段名 -> 属性名映射
     */
    private HashMap<String, String> fieldMap;

    /**
     * 属性名 -> 字段名映射
     */
    private HashMap<String, String> propertyMap;

    /**
     * 字段名列表
     */
    private List<String> fields;

    /**
     * 全限定名 -> 全限定别名
     */
    private Map<String, String> aliasMap;
}
