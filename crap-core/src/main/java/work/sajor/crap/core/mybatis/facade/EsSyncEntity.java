package work.sajor.crap.core.mybatis.facade;

import java.time.LocalDateTime;

/**
 * 数据表实体同步接口
 */

public interface EsSyncEntity<T extends Entity> {
    
    // ------------------------------ es_time : 索引时间 ------------------------------
    
    LocalDateTime getEsTime();
    
    void setEsTime(LocalDateTime esTime);
}
