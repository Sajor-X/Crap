package work.sajor.crap.system.facade;

import work.sajor.crap.core.dao.entity.RbacPrivilege;
import work.sajor.crap.core.security.dto.WebUser;
import work.sajor.crap.system.dto.MenuCreateDTO;

import java.util.List;

/**
 * <p>
 * 菜单服务
 * </p>
 *
 * @author Sajor
 * @since 2023-02-05
 */
public interface MenuService {

    /**
     * 获取用户菜单列表
     */
    List<RbacPrivilege> getMenuList(WebUser webUser);

    /**
     * 创建菜单
     */
    RbacPrivilege create(MenuCreateDTO menuCreate);
}
