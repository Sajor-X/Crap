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
 * 
 *
 * 数据表实体, 与数据库保持同步, 不可修改
 *
 * @author Sajor
 * @since 2023-02-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "crap_admin_detail", autoResultMap = true)
@ApiModel(value="AdminDetailBase对象", description="")
public class AdminDetailBase implements Serializable, work.sajor.crap.core.mybatis.facade.Entity {

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
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("`remark`")
    @JsonProperty("remark")
    protected String remark;

    // ****************************** Db Field ******************************

    public static final String Table = "crap_admin_detail";

    public static class Fields {
        public static final String id = "id";
        public static final String remark = "remark";
    }

    public static class Alias {
        public static final String id = "crap_admin_detail.id";
        public static final String remark = "crap_admin_detail.remark";
    }

    // ****************************** Entity ******************************

    @Override
    public Long getId() {
        return this.id;
    }

}
