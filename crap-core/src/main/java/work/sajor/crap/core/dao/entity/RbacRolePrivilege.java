package work.sajor.crap.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonValue;
import work.sajor.crap.core.mybatis.facade.FieldEnum;
import work.sajor.crap.core.dao.entity.base.RbacRolePrivilegeBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
// import org.springframework.data.elasticsearch.annotations.Document;

/**
 * RBAC 角色-权限关联
 *
 * 实体扩展
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "crap_rbac_role_privilege", autoResultMap = true)
// @Document(indexName = "crap_rbac_role_privilege")
public class RbacRolePrivilege extends RbacRolePrivilegeBase {

}