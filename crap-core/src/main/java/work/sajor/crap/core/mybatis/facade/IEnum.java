package work.sajor.crap.core.mybatis.facade;

import java.io.Serializable;

public interface IEnum<T extends Serializable> extends com.baomidou.mybatisplus.annotation.IEnum<T> {
    
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
