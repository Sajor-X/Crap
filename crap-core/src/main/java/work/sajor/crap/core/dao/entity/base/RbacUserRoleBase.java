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
 * RBAC 用户-角色关联
 *
 * 数据表实体, 与数据库保持同步, 不可修改
 *
 * @author Sajor
 * @since 2022-11-29
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
