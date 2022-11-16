package work.sajor.crap.core.mybatis.facade;


/**
 * <p>
 * 数据枚举类型基础接口
 * </p>
 *
 * @author Sajor
 * @since 2022-11-10
 */
public interface StringFieldEnum extends FieldEnum<String> {
    
    /**
     * 枚举数据库存储值
     */
    default String getValue() {
        return toString();
    }
    
    /**
     * 翻译
     *
     * @return
     */
    String getName();
}
