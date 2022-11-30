package work.sajor.crap.core.security.util;

import cn.hutool.core.util.StrUtil;
import work.sajor.crap.core.security.dto.UriResource;
import work.sajor.crap.core.web.WebUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 资源工具
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
public class UriUtil {

    /**
     * 获取请求的地址信息
     */
    public static UriResource getUriResource() {
        HttpServletRequest request = WebUtil.getRequest();
        return getUriResource(request.getRequestURI());
    }

    /*
     * uri 格式 :
     * /route/module/controller/method/id/action
     */
    public static UriResource getUriResource(String uri) {
        UriResource uriResource = new UriResource();
        try {
            HttpServletRequest request = WebUtil.getRequest();
            String[] split = uri.substring(request.getContextPath().length()).split("/");
            uriResource.setRoute(split[1]);
            uriResource.setModule(split[2]);
            uriResource.setController(split[3]);
            uriResource.setMethod(split[4]);
            uriResource.setId(split[5]);
            uriResource.setAction(split[6]);
        } catch (Exception ignore) {}

        return uriResource;
    }

    /**
     * 补全路径
     * 完整路径, 斜线开头, 不处理 : /module/controller/method 或 /module/controller/method/{id}
     * 相对路径, 非斜线开头, 用当前控制器地址补全 : method 或 method/{id}
     */
    public static UriResource parseRelativeUri(String uri) {
        if (StrUtil.isEmpty(uri)) {
            return getUriResource();
        }
        if (uri.startsWith("/")) {
            return getUriResource(uri);
        }
        UriResource uriResource = getUriResource();
        String[] split = uri.split("/");
        uriResource.setMethod(split[0]);
        if (split.length > 1) {
            uriResource.setId(split[1]);
        }
        return uriResource;
    }
}
