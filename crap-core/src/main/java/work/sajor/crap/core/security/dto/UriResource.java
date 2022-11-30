package work.sajor.crap.core.security.dto;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * <p>
 * 资源
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Data
public class UriResource {

    // 路由标识
    String route;

    // 项目
    String module;

    // 控制器
    String controller;

    // 方法
    String method;

    // id
    String id;

    // 附加标识
    String action;

    /**
     * 只取前三级地址作为权限标识, /module/controller/method
     * action 用于辅助动作, 如动态查询字典等
     */
    public String getResource() {
        return "/" + module + "/" + controller + "/" + method;
    }

    public String getUri() {
        return getResource() + (StrUtil.isEmpty(id) ? "" : "/" + id);
    }
}
