package work.sajor.crap.system.facade;

import work.sajor.crap.core.dao.entity.RbacRole;

import java.util.List;

/**
 * <p>
 * RBAC 角色
 * 各子项目角色分离
 * todo 增加租户字段, 按租户隔离
 * </p>
 *
 * @author Sajor
 * @since 2023-02-06
 */
public interface RbacRoleService {

    /**
     * 获取用户角色 id 列表
     *
     * @param userId 用户id
     * @return 角色id list
     */
    List<Long> getIds(Long userId);

    /**
     * 获取项目角色列表
     *
     * @return 角色list
     */
    List<RbacRole> getList();

    /**
     * 删除角色
     *
     * @param roleId 角色id
     */
    boolean remove(Long roleId);

    /**
     * 批量删除角色
     *
     * @param roleIds 角色id list
     */
    boolean remove(Long[] roleIds);

    /**
     * 保存 角色->权限 关联
     *
     * @param roleId 角色id
     * @param privilegeIds 权限id list
     */
    void grant(Long roleId, List<Long> privilegeIds);
}
