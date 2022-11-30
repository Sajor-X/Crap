package work.sajor.crap.core.session;

import lombok.Data;
import work.sajor.crap.core.dao.entity.RbacPrivilege;
import work.sajor.crap.core.security.dto.WebUser;

/**
 * <p>
 * 系统会话
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Data
public class SessionContext {

    /**
     * 当前用户
     */
    WebUser user = new WebUser();

    /**
     * 当前菜单
     */
    RbacPrivilege menu = null;
}