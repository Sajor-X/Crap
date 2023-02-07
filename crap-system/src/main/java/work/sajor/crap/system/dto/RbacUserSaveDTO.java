package work.sajor.crap.system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import work.sajor.crap.core.dao.entity.RbacUser;

import java.util.List;

/**
 * <p>
 * 保存用户
 * </p>
 *
 * @author Sajor
 * @since 2023-02-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RbacUserSaveDTO extends RbacUser {

    @JsonProperty("role_ids")
    private List<Long> roleIds;
}
