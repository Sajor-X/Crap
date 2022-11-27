package work.sajor.crap.core.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p>
 * 通用字段填充
 * </p>
 *
 * @author Sajor
 * @since 2022-11-27
 */
@Component
@Slf4j
public class MybatisMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
//        this.setFieldValByName("createUid", user.getId(), metaObject);       // 覆盖
//        this.setFieldValByName("createUname", user.getName(), metaObject);   // 覆盖
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
//        this.setFieldValByName("updateUid", user.getId(), metaObject);       // 覆盖
//        this.setFieldValByName("updateUname", user.getName(), metaObject);   // 覆盖
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        WebUser user = SessionUtil.getUser();
//        this.setFieldValByName("updateUid", user.getId(), metaObject);       // 覆盖
//        this.setFieldValByName("updateUname", user.getName(), metaObject);   // 覆盖
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
