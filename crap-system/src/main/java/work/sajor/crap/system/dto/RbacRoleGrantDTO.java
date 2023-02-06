package work.sajor.crap.system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import work.sajor.crap.core.dao.entity.RbacRole;

import java.util.List;

/**
 * <p>
 * 角色授权
 * </p>
 *
 * @author Sajor
 * @since 2023-02-06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RbacRoleGrantDTO extends RbacRole {

    @JsonProperty("privilege_ids")
    List<Long> privilegeIds;
}
