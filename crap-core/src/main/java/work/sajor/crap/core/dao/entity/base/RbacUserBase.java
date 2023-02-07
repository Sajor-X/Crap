package work.sajor.crap.core.dao.entity.base;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import work.sajor.crap.core.mybatis.handler.AESEncryptHandler;
import work.sajor.crap.core.mybatis.support.TableCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * RBAC 用户
 *
 * 数据表实体, 与数据库保持同步, 不可修改
 *
 * @author Sajor
 * @since 2023-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "crap_rbac_user", autoResultMap = true)
@ApiModel(value="RbacUserBase对象", description="RBAC 用户")
public class RbacUserBase implements Serializable, work.sajor.crap.core.mybatis.facade.Entity {

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
     * code(U). 编码
     */
    @ApiModelProperty(value = "code(U). 编码")
    @TableField("`code`")
    @TableCode("U")
    @JsonProperty("code")
    protected String code;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    @TableField("`username`")
    @JsonProperty("username")
    protected String username;

    /**
     * ignore 密码
     */
    @ApiModelProperty(value = "ignore 密码")
    @TableField("`password`")
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    protected String password;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    @TableField(value = "`name`")
    @JsonProperty("name")
    protected String name;

    /**
     * encrypt 手机号码
     */
    @ApiModelProperty(value = "encrypt 手机号码")
    @TableField(value = "`mobile`", typeHandler = AESEncryptHandler.class)
    @JsonProperty("mobile")
    protected String mobile;

    /**
     * bool 是否有效
     */
    @ApiModelProperty(value = "bool 是否有效")
    @TableField("`status`")
    @JsonProperty("status")
    protected Integer status;

    /**
     * json(java.util.List<Long>) 角色 ID 列表
     */
    @ApiModelProperty(value = "json(java.util.List<Long>) 角色 ID 列表")
    @TableField(value = "`role_ids`", typeHandler = JacksonTypeHandler.class)
    @JsonProperty("role_ids")
    protected java.util.List<Long> roleIds;

    /**
     * json(java.util.List<Long>) 部门 ID 列表
     */
    @ApiModelProperty(value = "json(java.util.List<Long>) 部门 ID 列表")
    @TableField(value = "`department_ids`", typeHandler = JacksonTypeHandler.class)
    @JsonProperty("department_ids")
    protected java.util.List<Long> departmentIds;

    /**
     * 0: 未锁定; 1: 已锁定;
     */
    @ApiModelProperty(value = "0: 未锁定; 1: 已锁定;")
    @TableField("`lock_flag`")
    @JsonProperty("lock_flag")
    protected Integer lockFlag;

    /**
     * 最近操作时间
     */
    @ApiModelProperty(value = "最近操作时间")
    @TableField("`last_op_time`")
    @JsonProperty("last_op_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime lastOpTime;

    /**
     * 乐观锁版本号
     */
    @ApiModelProperty(value = "乐观锁版本号")
    @TableField("`sys_version`")
    @JsonProperty("sys_version")
    @Version
    protected Integer sysVersion;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(value = "逻辑删除")
    @TableField("crap_rbac_user.`delete_flag`")
    @JsonProperty("delete_flag")
    @TableLogic
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime deleteFlag;

    /**
     * System 创建人
     */
    @ApiModelProperty(value = "System 创建人")
    @TableField(value = "create_uid", fill = FieldFill.INSERT)
    @JsonProperty("create_uid")
    @JsonFormat(shape = Shape.STRING)
    protected Long createUid;

    /**
     * System 创建人
     */
    @ApiModelProperty(value = "System 创建人")
    @TableField(value = "create_uname", fill = FieldFill.INSERT)
    @JsonProperty("create_uname")
    protected String createUname;

    /**
     * System 创建时间
     */
    @ApiModelProperty(value = "System 创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime createTime;

    /**
     * System 更新人
     */
    @ApiModelProperty(value = "System 更新人")
    @TableField(value = "update_uid", fill = FieldFill.INSERT_UPDATE)
    @JsonProperty("update_uid")
    @JsonFormat(shape = Shape.STRING)
    protected Long updateUid;

    /**
     * System 更新人
     */
    @ApiModelProperty(value = "System 更新人")
    @TableField(value = "update_uname", fill = FieldFill.INSERT_UPDATE)
    @JsonProperty("update_uname")
    protected String updateUname;

    /**
     * System 更新时间
     */
    @ApiModelProperty(value = "System 更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonProperty("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime updateTime;

    /**
     * System TID
     */
    @ApiModelProperty(value = "System TID")
    @TableField(value = "tid", fill = FieldFill.INSERT)
    @JsonProperty("tid")
    protected Integer tid;

    // ****************************** Db Field ******************************

    public static final String Table = "crap_rbac_user";

    public static class Fields {
        public static final String id = "id";
        public static final String code = "code";
        public static final String username = "username";
        public static final String password = "password";
        public static final String name = "name";
        public static final String mobile = "mobile";
        public static final String status = "status";
        public static final String roleIds = "role_ids";
        public static final String departmentIds = "department_ids";
        public static final String lockFlag = "lock_flag";
        public static final String lastOpTime = "last_op_time";
        public static final String sysVersion = "sys_version";
        public static final String deleteFlag = "delete_flag";
        public static final String createUid = "create_uid";
        public static final String createUname = "create_uname";
        public static final String createTime = "create_time";
        public static final String updateUid = "update_uid";
        public static final String updateUname = "update_uname";
        public static final String updateTime = "update_time";
        public static final String tid = "tid";
    }

    public static class Alias {
        public static final String id = "crap_rbac_user.id";
        public static final String code = "crap_rbac_user.code";
        public static final String username = "crap_rbac_user.username";
        public static final String password = "crap_rbac_user.password";
        public static final String name = "crap_rbac_user.name";
        public static final String mobile = "crap_rbac_user.mobile";
        public static final String status = "crap_rbac_user.status";
        public static final String roleIds = "crap_rbac_user.role_ids";
        public static final String departmentIds = "crap_rbac_user.department_ids";
        public static final String lockFlag = "crap_rbac_user.lock_flag";
        public static final String lastOpTime = "crap_rbac_user.last_op_time";
        public static final String sysVersion = "crap_rbac_user.sys_version";
        public static final String deleteFlag = "crap_rbac_user.delete_flag";
        public static final String createUid = "crap_rbac_user.create_uid";
        public static final String createUname = "crap_rbac_user.create_uname";
        public static final String createTime = "crap_rbac_user.create_time";
        public static final String updateUid = "crap_rbac_user.update_uid";
        public static final String updateUname = "crap_rbac_user.update_uname";
        public static final String updateTime = "crap_rbac_user.update_time";
        public static final String tid = "crap_rbac_user.tid";
    }

    // ****************************** Entity ******************************

    @Override
    public Long getId() {
        return this.id;
    }

}
