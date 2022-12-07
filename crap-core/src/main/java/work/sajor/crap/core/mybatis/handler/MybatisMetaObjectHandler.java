package work.sajor.crap.core.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import work.sajor.crap.core.mybatis.support.CommonFields;
import work.sajor.crap.core.mybatis.support.EntityInfo;
import work.sajor.crap.core.mybatis.util.EntityInfoUtil;
import work.sajor.crap.core.security.dto.WebUser;
import work.sajor.crap.core.session.SessionUtil;

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
        WebUser user = SessionUtil.getUser();
        if (user == null) {
            user = new WebUser();
            user.setId(0L);
            user.setName("");
        }

        // --- 填充默认编码 ---
        EntityInfo<?> info = EntityInfoUtil.getInfo(metaObject.getOriginalObject().getClass());
        if (info.getHasCode()) {
            Object codeVal = getFieldValByName(CommonFields.CODE, metaObject);
            if (codeVal == null || StrUtil.isEmpty(String.valueOf(codeVal))) {
                // TODO 自动生成编码
//                this.setFieldValByName("code", SnUtil.getTableCode(info.getTable(), info.getCodePrefix()), metaObject);       // 覆盖
            }
        }
        this.setFieldValByName("tid", user.getTid(), metaObject);               // 覆盖创建人TID
        this.setFieldValByName("createUid", user.getId(), metaObject);          // 覆盖创建人UID
        this.setFieldValByName("createUname", user.getName(), metaObject);      // 覆盖创建人姓名
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);  // 覆盖创建时间
        this.setFieldValByName("updateUid", user.getId(), metaObject);          // 覆盖修改人UID
        this.setFieldValByName("updateUname", user.getName(), metaObject);      // 覆盖修改人姓名
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);  // 覆盖修改时间
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        WebUser user = SessionUtil.getUser();
        this.setFieldValByName("updateUid", user.getId(), metaObject);          // 覆盖修改人UID
        this.setFieldValByName("updateUname", user.getName(), metaObject);      // 覆盖修改人姓名
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);  // 覆盖修改时间
    }
}
