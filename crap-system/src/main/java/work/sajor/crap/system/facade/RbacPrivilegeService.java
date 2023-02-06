package work.sajor.crap.system.facade;
import work.sajor.crap.core.dao.entity.RbacPrivilege;

import java.util.List;

/**
 * <p>
 * 权限服务
 * 子项目权限隔离
 * </p>
 *
 * @author Sajor
 * @since 2023-02-05
 */
public interface RbacPrivilegeService {

    /**
     * 获取角色权限 id 列表
     *
     * @param roleId
     * @return
     */
    List<Long> getRolePrivilegeIds(Long roleId);

    /**
     * 获取项目权限列表
     *
     * @return
     */
    List<RbacPrivilege> getList();
}
