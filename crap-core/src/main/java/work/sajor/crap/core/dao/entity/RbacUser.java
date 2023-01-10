package work.sajor.crap.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import work.sajor.crap.core.annotation.PrivacyEncrypt;
import work.sajor.crap.core.dao.entity.base.RbacUserBase;
import work.sajor.crap.core.json.PrivacyTypeEnum;
import work.sajor.crap.core.mybatis.handler.AESEncryptHandler;

import java.util.ArrayList;
import java.util.List;
// import org.springframework.data.elasticsearch.annotations.Document;

/**
 * RBAC 用户
 *
 * 实体扩展
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "crap_rbac_user", autoResultMap = true)
// @Document(indexName = "crap_rbac_user")
public class RbacUser extends RbacUserBase {

    /**
     * 手机号码
     */
    @PrivacyEncrypt(type = PrivacyTypeEnum.MOBILE)
    @ApiModelProperty(value = "encrypt(手机号码)")
    @TableField(value = "`mobile`", typeHandler = AESEncryptHandler.class)
    @JsonProperty("mobile")
    protected String mobile;

    public List<Long> getRoleIds() {
        return roleIds == null ? new ArrayList<>() : roleIds;
    }

    public List<Long> getDepartmentIds() {
        return departmentIds == null ? new ArrayList<>() : departmentIds;
    }
}