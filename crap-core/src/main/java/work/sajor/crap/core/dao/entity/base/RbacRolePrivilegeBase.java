package work.sajor.crap.core.dao.entity.base;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * RBAC 角色-权限关联
 * <p>
 * 数据表实体, 与数据库保持同步, 不可修改
 *
 * @author Sajor
 * @since 2022-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "crap_rbac_role_privilege", autoResultMap = true)
@ApiModel(value="RbacRolePrivilegeBase对象", description="RBAC 角色-权限关联")
public class RbacRolePrivilegeBase implements Serializable, work.sajor.crap.core.mybatis.facade.Entity {

    private static final long serialVersionUID = 1L;

    /**
    * ID
    */
    @ApiModelProperty(value = "ID")
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @JsonProperty("id")
    @JsonFormat(shape = Shape.STRING)
    protected Long id;

    /**
    * 角色 ID
    */
    @ApiModelProperty(value = "角色 ID")
    @TableField("`role_id`")
    @JsonProperty("role_id")
    @JsonFormat(shape = Shape.STRING)
    protected Long roleId;

    /**
    * 权限 ID
    */
    @ApiModelProperty(value = "权限 ID")
    @TableField("`privilege_id`")
    @JsonProperty("privilege_id")
    @JsonFormat(shape = Shape.STRING)
    protected Long privilegeId;

    // ****************************** Db Field ******************************

    public static final String Table = "crap_rbac_role_privilege";

    public static class Fields {
        public static final String id = "id";
        public static final String roleId = "role_id";
        public static final String privilegeId = "privilege_id";
    }

    public static class Alias {
        public static final String id = "crap_rbac_role_privilege.id";
        public static final String roleId = "crap_rbac_role_privilege.role_id";
        public static final String privilegeId = "crap_rbac_role_privilege.privilege_id";
    }

    // ****************************** Entity ******************************

    @Override
    public Long getId() {
        return this.id;
    }

}
