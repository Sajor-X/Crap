package work.sajor.crap.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonValue;
import work.sajor.crap.core.mybatis.facade.FieldEnum;
import work.sajor.crap.core.dao.entity.base.RbacRoleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
// import org.springframework.data.elasticsearch.annotations.Document;

/**
 * RBAC 角色
 *
 * 实体扩展
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "crap_rbac_role", autoResultMap = true)
// @Document(indexName = "crap_rbac_role")
public class RbacRole extends RbacRoleBase {

    /**
     * enum 状态. 1:启用; 0:禁用;
     */
    @Getter
    @AllArgsConstructor
    public enum StatusEnum implements FieldEnum<Integer> {
        ON(1, "启用"),
        OFF(0, "禁用");

        @JsonValue
        private Integer value;
        private String name;
    }
}