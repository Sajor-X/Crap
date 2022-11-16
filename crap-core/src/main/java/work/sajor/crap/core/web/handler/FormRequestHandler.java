package work.sajor.crap.core.web.handler;

import org.springframework.stereotype.Component;
import work.sajor.crap.core.mybatis.util.EntityConvertUtil;
import work.sajor.crap.core.mybatis.util.EntityUtil;
import work.sajor.crap.core.web.WebException;
import work.sajor.crap.core.web.dto.FormRequest;

@Component
public class FormRequestHandler {

    /**
     * 提取表单数据
     */
    public <T> T getEntity(Class<T> entityClass, FormRequest formRequest, Object id) {
        try {
            // todo dto/entity 格式验证
            T entity = EntityConvertUtil.convert(entityClass, formRequest.getData());
            try {
                EntityUtil.setID(entity, id);
            } catch (Exception ignore) {
            }
            return entity;
        } catch (Exception e) {
            throw new WebException("提交数据异常", e);
        }
    }

    public <T> T getEntity(Class<T> entityClass, FormRequest formRequest) {
        return getEntity(entityClass, formRequest, null);
    }
}
