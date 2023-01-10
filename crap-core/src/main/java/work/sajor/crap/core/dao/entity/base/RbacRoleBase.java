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
import work.sajor.crap.core.dao.entity.RbacRole.StatusEnum;
import work.sajor.crap.core.mybatis.support.TableCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * RBAC 角色
 * <p>
 * 数据表实体, 与数据库保持同步, 不可修改
 *
 * @author Sajor
 * @since 2023-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "crap_rbac_role", autoResultMap = true)
@ApiModel(value="RbacRoleBase对象", description="RBAC 角色")
public class RbacRoleBase implements Serializable, work.sajor.crap.core.mybatis.facade.Entity {

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
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @TableField("`name`")
    @JsonProperty("name")
    protected String name;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    @TableField("`code`")
    @TableCode()
    @JsonProperty("code")
    protected String code;

    /**
     * 项目
     */
    @ApiModelProperty(value = "项目")
    @TableField("`project`")
    @JsonProperty("project")
    protected String project;

    /**
     * enum 状态. 1:启用; 0:禁用;
     */
    @ApiModelProperty(value = "enum 状态. 1:启用; 0:禁用;")
    @TableField("`status`")
    @JsonProperty("status")
    protected StatusEnum status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("`remark`")
    @JsonProperty("remark")
    protected String remark;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(value = "逻辑删除")
    @TableField("crap_rbac_role.`delete_flag`")
    @JsonProperty("delete_flag")
    @TableLogic
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime deleteFlag;

    /**
     * System TID
     */
    @ApiModelProperty(value = "System TID")
    @TableField(value = "tid", fill = FieldFill.INSERT)
    @JsonProperty("tid")
    protected Integer tid;

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

    // ****************************** Db Field ******************************

    public static final String Table = "crap_rbac_role";

    public static class Fields {
        public static final String id = "id";
        public static final String name = "name";
        public static final String code = "code";
        public static final String project = "project";
        public static final String status = "status";
        public static final String remark = "remark";
        public static final String deleteFlag = "delete_flag";
        public static final String tid = "tid";
        public static final String createUid = "create_uid";
        public static final String createUname = "create_uname";
        public static final String createTime = "create_time";
        public static final String updateUid = "update_uid";
        public static final String updateUname = "update_uname";
        public static final String updateTime = "update_time";
    }

    public static class Alias {
        public static final String id = "crap_rbac_role.id";
        public static final String name = "crap_rbac_role.name";
        public static final String code = "crap_rbac_role.code";
        public static final String project = "crap_rbac_role.project";
        public static final String status = "crap_rbac_role.status";
        public static final String remark = "crap_rbac_role.remark";
        public static final String deleteFlag = "crap_rbac_role.delete_flag";
        public static final String tid = "crap_rbac_role.tid";
        public static final String createUid = "crap_rbac_role.create_uid";
        public static final String createUname = "crap_rbac_role.create_uname";
        public static final String createTime = "crap_rbac_role.create_time";
        public static final String updateUid = "crap_rbac_role.update_uid";
        public static final String updateUname = "crap_rbac_role.update_uname";
        public static final String updateTime = "crap_rbac_role.update_time";
    }

    // ****************************** Entity ******************************

    @Override
    public Long getId() {
        return this.id;
    }

}
