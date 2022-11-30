package work.sajor.crap.core.dao.entity.base;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import java.sql.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import work.sajor.crap.core.mybatis.support.TableCode;
import work.sajor.crap.core.mybatis.handler.AESEncryptHandler;
import work.sajor.crap.core.mybatis.facade.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

/**
 * RBAC 角色-权限关联
 *
 * 数据表实体, 与数据库保持同步, 不可修改
 *
 * @author Sajor
 * @since 2022-11-29
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
