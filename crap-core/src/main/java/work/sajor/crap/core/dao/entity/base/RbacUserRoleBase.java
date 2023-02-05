package work.sajor.crap.core.dao.entity.base;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * RBAC 用户-角色关联
 *
 * 数据表实体, 与数据库保持同步, 不可修改
 *
 * @author Sajor
 * @since 2023-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "crap_rbac_user_role", autoResultMap = true)
@ApiModel(value="RbacUserRoleBase对象", description="RBAC 用户-角色关联")
public class RbacUserRoleBase implements Serializable, work.sajor.crap.core.mybatis.facade.Entity {

    private static final long serialVersionUID = 1L;

    @Id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonProperty("id")
    @JsonFormat(shape = Shape.STRING)
    protected Long id;

    @TableField("`user_id`")
    @JsonProperty("user_id")
    @JsonFormat(shape = Shape.STRING)
    protected Long userId;

    @TableField("`role_id`")
    @JsonProperty("role_id")
    @JsonFormat(shape = Shape.STRING)
    protected Long roleId;

    // ****************************** Db Field ******************************

    public static final String Table = "crap_rbac_user_role";

    public static class Fields {
        public static final String id = "id";
        public static final String userId = "user_id";
        public static final String roleId = "role_id";
    }

    public static class Alias {
        public static final String id = "crap_rbac_user_role.id";
        public static final String userId = "crap_rbac_user_role.user_id";
        public static final String roleId = "crap_rbac_user_role.role_id";
    }

    // ****************************** Entity ******************************

    @Override
    public Long getId() {
        return this.id;
    }

}
