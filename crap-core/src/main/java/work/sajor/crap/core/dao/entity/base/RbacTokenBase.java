package work.sajor.crap.core.dao.entity.base;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * RBAC 登录令牌
 * <p>
 * 数据表实体, 与数据库保持同步, 不可修改
 *
 * @author Sajor
 * @since 2023-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "crap_rbac_token", autoResultMap = true)
@ApiModel(value="RbacTokenBase对象", description="RBAC 登录令牌")
public class RbacTokenBase implements Serializable, work.sajor.crap.core.mybatis.facade.Entity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @Id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonProperty("id")
    @JsonFormat(shape = Shape.STRING)
    protected Long id;

    /**
     * 渠道
     */
    @ApiModelProperty(value = "渠道")
    @TableField("`type`")
    @JsonProperty("type")
    protected String type;

    /**
     * 用户 ID
     */
    @ApiModelProperty(value = "用户 ID")
    @TableField("`user_id`")
    @JsonProperty("user_id")
    @JsonFormat(shape = Shape.STRING)
    protected Long userId;

    /**
     * jwt
     */
    @ApiModelProperty(value = "jwt")
    @TableField("`token`")
    @JsonProperty("token")
    protected String token;

    /**
     * jwt 有效期(s)
     */
    @ApiModelProperty(value = "jwt 有效期(s)")
    @TableField("`token_ttl`")
    @JsonProperty("token_ttl")
    protected Integer tokenTtl;

    /**
     * jwt  刷新时间
     */
    @ApiModelProperty(value = "jwt  刷新时间")
    @TableField("`token_time`")
    @JsonProperty("token_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime tokenTime;

    /**
     * 最近操作时间
     */
    @ApiModelProperty(value = "最近操作时间")
    @TableField("`last_op_time`")
    @JsonProperty("last_op_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime lastOpTime;

    /**
     * System 创建时间
     */
    @ApiModelProperty(value = "System 创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime createTime;

    /**
     * System 更新时间
     */
    @ApiModelProperty(value = "System 更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonProperty("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime updateTime;

    // ****************************** Db Field ******************************

    public static final String Table = "crap_rbac_token";

    public static class Fields {
        public static final String id = "id";
        public static final String type = "type";
        public static final String userId = "user_id";
        public static final String token = "token";
        public static final String tokenTtl = "token_ttl";
        public static final String tokenTime = "token_time";
        public static final String lastOpTime = "last_op_time";
        public static final String createTime = "create_time";
        public static final String updateTime = "update_time";
    }

    public static class Alias {
        public static final String id = "crap_rbac_token.id";
        public static final String type = "crap_rbac_token.type";
        public static final String userId = "crap_rbac_token.user_id";
        public static final String token = "crap_rbac_token.token";
        public static final String tokenTtl = "crap_rbac_token.token_ttl";
        public static final String tokenTime = "crap_rbac_token.token_time";
        public static final String lastOpTime = "crap_rbac_token.last_op_time";
        public static final String createTime = "crap_rbac_token.create_time";
        public static final String updateTime = "crap_rbac_token.update_time";
    }

    // ****************************** Entity ******************************

    @Override
    public Long getId() {
        return this.id;
    }

}
