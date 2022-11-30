package work.sajor.crap.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonValue;
import work.sajor.crap.core.mybatis.facade.FieldEnum;
import work.sajor.crap.core.dao.entity.base.RbacPrivilegeBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
// import org.springframework.data.elasticsearch.annotations.Document;

/**
 * RBAC 权限
 *
 * 实体扩展
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "crap_rbac_privilege", autoResultMap = true)
// @Document(indexName = "crap_rbac_privilege")
public class RbacPrivilege extends RbacPrivilegeBase {
    /**
     * 配置后端数据源
     */
    @Data
    public static class Setting {

        private Boolean useSql = false;

        private String table;

        private String where;

        private String order;

        private String sql;
    }
}