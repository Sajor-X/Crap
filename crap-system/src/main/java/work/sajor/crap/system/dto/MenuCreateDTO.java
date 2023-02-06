package work.sajor.crap.system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import work.sajor.crap.core.dao.entity.RbacPrivilege;

import java.util.List;

/**
 * <p>
 * 菜单创建DTO
 * </p>
 *
 * @author Sajor
 * @since 2023-02-05
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class MenuCreateDTO extends RbacPrivilege {

    /**
     * 批量创建子菜单
     */
    private List<String> child;
}
