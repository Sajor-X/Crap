package work.sajor.crap.core.security.facade;

import work.sajor.crap.core.security.dto.WebUser;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 权限控制, 用于项目扩展
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
public interface ProjectAccessControl {

    /**
     * 检查用户是否有权限调用请求
     * 第一个参数是上一个回调的验证结果
     * 以最后一个回调的返回结果为准
     */
    default boolean check(Boolean previousCheckStatus, WebUser webUser, String uri) {
        return previousCheckStatus;
    }

    /**
     * 获取用户项目权限 ID 列表
     * 返回的权限与系统配置的权限合并, 构成完整的权限列表
     */
    default List<Long> getPrivilegeIds(Long userId) {
        return Collections.emptyList();
    }
}
