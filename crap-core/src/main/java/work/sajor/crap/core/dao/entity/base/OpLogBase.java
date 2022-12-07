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
 * 数据变更日志
 *
 * 数据表实体, 与数据库保持同步, 不可修改
 *
 * @author Sajor
 * @since 2022-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "crap_op_log", autoResultMap = true)
@ApiModel(value="OpLogBase对象", description="数据变更日志")
public class OpLogBase implements Serializable, work.sajor.crap.core.mybatis.facade.Entity {

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
    * 标题
    */
    @ApiModelProperty(value = "标题")
    @TableField("`title`")
    @JsonProperty("title")
    protected String title;

    /**
    * 菜单地址
    */
    @ApiModelProperty(value = "菜单地址")
    @TableField("`resource`")
    @JsonProperty("resource")
    protected String resource;

    /**
    * 类型
    */
    @ApiModelProperty(value = "类型")
    @TableField("`type`")
    @JsonProperty("type")
    protected String type;

    /**
    * 动作
    */
    @ApiModelProperty(value = "动作")
    @TableField("`action`")
    @JsonProperty("action")
    protected String action;

    /**
    * 源数据 ID
    */
    @ApiModelProperty(value = "源数据 ID")
    @TableField("`source_id`")
    @JsonProperty("source_id")
    @JsonFormat(shape = Shape.STRING)
    protected Long sourceId;

    /**
    * 源数据 CODE
    */
    @ApiModelProperty(value = "源数据 CODE")
    @TableField("`source_code`")
    @JsonProperty("source_code")
    protected String sourceCode;

    /**
    * 当前访问地址
    */
    @ApiModelProperty(value = "当前访问地址")
    @TableField("`op_url`")
    @JsonProperty("op_url")
    protected String opUrl;

    /**
    * 预览地址
    */
    @ApiModelProperty(value = "预览地址")
    @TableField("`view_url`")
    @JsonProperty("view_url")
    protected String viewUrl;

    /**
    * 备注
    */
    @ApiModelProperty(value = "备注")
    @TableField("`remark`")
    @JsonProperty("remark")
    protected String remark;

    /**
    * ip
    */
    @ApiModelProperty(value = "ip")
    @TableField("`ip`")
    @JsonProperty("ip")
    protected String ip;

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
    * System TID
    */
    @ApiModelProperty(value = "System TID")
    @TableField(value = "tid", fill = FieldFill.INSERT)
    @JsonProperty("tid")
    protected Integer tid;

    // ****************************** Db Field ******************************

    public static final String Table = "crap_op_log";

    public static class Fields {
        public static final String id = "id";
        public static final String title = "title";
        public static final String resource = "resource";
        public static final String type = "type";
        public static final String action = "action";
        public static final String sourceId = "source_id";
        public static final String sourceCode = "source_code";
        public static final String opUrl = "op_url";
        public static final String viewUrl = "view_url";
        public static final String remark = "remark";
        public static final String ip = "ip";
        public static final String createUid = "create_uid";
        public static final String createUname = "create_uname";
        public static final String createTime = "create_time";
        public static final String tid = "tid";
    }

    public static class Alias {
        public static final String id = "crap_op_log.id";
        public static final String title = "crap_op_log.title";
        public static final String resource = "crap_op_log.resource";
        public static final String type = "crap_op_log.type";
        public static final String action = "crap_op_log.action";
        public static final String sourceId = "crap_op_log.source_id";
        public static final String sourceCode = "crap_op_log.source_code";
        public static final String opUrl = "crap_op_log.op_url";
        public static final String viewUrl = "crap_op_log.view_url";
        public static final String remark = "crap_op_log.remark";
        public static final String ip = "crap_op_log.ip";
        public static final String createUid = "crap_op_log.create_uid";
        public static final String createUname = "crap_op_log.create_uname";
        public static final String createTime = "crap_op_log.create_time";
        public static final String tid = "crap_op_log.tid";
    }

    // ****************************** Entity ******************************

    @Override
    public Long getId() {
        return this.id;
    }

}
