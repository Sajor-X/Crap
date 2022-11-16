package work.sajor.crap.core.mybatis.facade;

import java.io.Serializable;

/**
 * <p>
 * 数据枚举类型基础接口
 * </p>
 *
 * @author Sajor
 * @since 2022-11-10
 */
public interface FieldEnum<T extends Serializable> extends IEnum<T> {
    
    /**
     * 枚举数据库存储值
     */
    T getValue();
    
    /**
     * 翻译
     *
     * @return
     */
    String getName();
}
