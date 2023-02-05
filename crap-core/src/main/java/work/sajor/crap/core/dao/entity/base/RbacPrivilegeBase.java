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
import work.sajor.crap.core.mybatis.support.TableCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * RBAC 权限
 *
 * 数据表实体, 与数据库保持同步, 不可修改
 *
 * @author Sajor
 * @since 2023-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "crap_rbac_privilege", autoResultMap = true)
@ApiModel(value="RbacPrivilegeBase对象", description="RBAC 权限")
public class RbacPrivilegeBase implements Serializable, work.sajor.crap.core.mybatis.facade.Entity {

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
     * PID
     */
    @ApiModelProperty(value = "PID")
    @TableField("`pid`")
    @JsonProperty("pid")
    @JsonFormat(shape = Shape.STRING)
    protected Long pid;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    @TableField("`name`")
    @JsonProperty("name")
    protected String name;

    /**
     * 资源标识 uri / code ...
     */
    @ApiModelProperty(value = "资源标识 uri / code ...")
    @TableField("`resource`")
    @JsonProperty("resource")
    protected String resource;

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
     * 排序权重, 降序
     */
    @ApiModelProperty(value = "排序权重, 降序")
    @TableField("`sort`")
    @JsonProperty("sort")
    protected Integer sort;

    /**
     * 0:禁用, 1:启用
     */
    @ApiModelProperty(value = "0:禁用, 1:启用")
    @TableField("`status`")
    @JsonProperty("status")
    protected Integer status;

    /**
     * 图标
     */
    @ApiModelProperty(value = "图标")
    @TableField("`icon`")
    @JsonProperty("icon")
    protected String icon;

    /**
     * 是否显示
     */
    @ApiModelProperty(value = "是否显示")
    @TableField("`show_flag`")
    @JsonProperty("show_flag")
    @JsonFormat(shape = Shape.NUMBER)
    protected Boolean showFlag;

    /**
     * 1: 授权访问; 2: 完全公开; 3: 仅用于开发者账号
     */
    @ApiModelProperty(value = "1: 授权访问; 2: 完全公开; 3: 仅用于开发者账号")
    @TableField("`type`")
    @JsonProperty("type")
    protected Integer type;

    /**
     * sql 模板 : SELECT * FROM table WHERE user_id={['webUser'].id}
     */
    @ApiModelProperty(value = "sql 模板 : SELECT * FROM table WHERE user_id={['webUser'].id}")
    @TableField("`sql`")
    @JsonProperty("sql")
    protected String sql;

    /**
     * 前端模板 : @/views*
     */
    @ApiModelProperty(value = "前端模板 : @/views*")
    @TableField("`tpl`")
    @JsonProperty("tpl")
    protected String tpl;

    /**
     * 系统标识
     */
    @ApiModelProperty(value = "系统标识")
    @TableField("`scope`")
    @JsonProperty("scope")
    protected String scope;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("`remark`")
    @JsonProperty("remark")
    protected String remark;

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

    public static final String Table = "crap_rbac_privilege";

    public static class Fields {
        public static final String id = "id";
        public static final String pid = "pid";
        public static final String name = "name";
        public static final String resource = "resource";
        public static final String code = "code";
        public static final String project = "project";
        public static final String sort = "sort";
        public static final String status = "status";
        public static final String icon = "icon";
        public static final String showFlag = "show_flag";
        public static final String type = "type";
        public static final String sql = "sql";
        public static final String tpl = "tpl";
        public static final String scope = "scope";
        public static final String remark = "remark";
        public static final String tid = "tid";
        public static final String createUid = "create_uid";
        public static final String createUname = "create_uname";
        public static final String createTime = "create_time";
        public static final String updateUid = "update_uid";
        public static final String updateUname = "update_uname";
        public static final String updateTime = "update_time";
    }

    public static class Alias {
        public static final String id = "crap_rbac_privilege.id";
        public static final String pid = "crap_rbac_privilege.pid";
        public static final String name = "crap_rbac_privilege.name";
        public static final String resource = "crap_rbac_privilege.resource";
        public static final String code = "crap_rbac_privilege.code";
        public static final String project = "crap_rbac_privilege.project";
        public static final String sort = "crap_rbac_privilege.sort";
        public static final String status = "crap_rbac_privilege.status";
        public static final String icon = "crap_rbac_privilege.icon";
        public static final String showFlag = "crap_rbac_privilege.show_flag";
        public static final String type = "crap_rbac_privilege.type";
        public static final String sql = "crap_rbac_privilege.sql";
        public static final String tpl = "crap_rbac_privilege.tpl";
        public static final String scope = "crap_rbac_privilege.scope";
        public static final String remark = "crap_rbac_privilege.remark";
        public static final String tid = "crap_rbac_privilege.tid";
        public static final String createUid = "crap_rbac_privilege.create_uid";
        public static final String createUname = "crap_rbac_privilege.create_uname";
        public static final String createTime = "crap_rbac_privilege.create_time";
        public static final String updateUid = "crap_rbac_privilege.update_uid";
        public static final String updateUname = "crap_rbac_privilege.update_uname";
        public static final String updateTime = "crap_rbac_privilege.update_time";
    }

    // ****************************** Entity ******************************

    @Override
    public Long getId() {
        return this.id;
    }

}
