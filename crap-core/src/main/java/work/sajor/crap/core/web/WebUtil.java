package work.sajor.crap.core.web;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import work.sajor.crap.core.logger.LogUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;

/**
 * <p>
 * Servlet / Request Util
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Slf4j
public class WebUtil {

    private static final String REQUEST_BODY_CACHE = "REQUEST_BODY";

    public static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 获取 request
     */
    public static HttpServletRequest getRequest() {
        try {
            return getRequestAttributes().getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取 response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取 query 参数
     *
     * @return
     */
    public static Map<String, String> getQueryParam() {
        return cn.hutool.extra.servlet.ServletUtil.getParamMap(getRequest());
    }

    /**
     * 获取 query 参数
     *
     * @return
     */
    public static String getQueryParam(String key) {
        Map<String, String> queryMap = getQueryParam();
        return queryMap.getOrDefault(key, "");
    }

    /**
     * 获取 query 参数
     *
     * @return
     */
    public static <T> T getQueryParam(String key, Class<T> clazz) {
        Map<String, String> queryMap = getQueryParam();
        return Convert.convert(clazz, queryMap.getOrDefault(key, ""));
    }

    /**
     * 缓存请求体
     */
    public static void setBody(String body) {
        getRequest().setAttribute(REQUEST_BODY_CACHE, body);
    }

    /**
     * 从请求体中提取 json
     */
    public static String getBody() {
        return (String) getRequest().getAttribute(REQUEST_BODY_CACHE);
    }

    /**
     * 从请求体中提取 bean
     */
    public static <T> T getBody(Class<T> clazz) {
        return BeanUtil.copyProperties(JSONUtil.parseObj(getBody()), clazz);
    }

    /**
     * 获取请求头
     */
    public static String getHeader() {
        StringBuilder header = new StringBuilder();
        HttpServletRequest request = getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            header.append(headerName).append(":").append(request.getHeader(headerName)).append("\n");
        }
        return header.toString();
    }

    /**
     * 检测 ajax 请求
     */
    public static boolean isAjax() {
        return getRequest().getHeader("x-requested-with") != null
                && getRequest().getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest");
    }

    /**
     * 输出 json 响应
     *
     * @param response
     * @param jsonResponse
     * @throws IOException
     */
    public static void sendJson(HttpServletResponse response, Object jsonResponse) throws IOException {
        response.setStatus(200);
        response.setContentType("text/javascript;charset=UTF-8");
        response.getWriter().print(JSONUtil.toJsonStr(jsonResponse));
        response.getWriter().flush();
    }


    /**
     * 获取 IP
     */
    public static String getIp() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "0.0.0.0";
        }
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                        ipAddress = inet.getHostAddress();
                    } catch (UnknownHostException e) {
                        log.warn("获取 IP 失败" + LogUtil.detail(e));
                    }
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            log.warn("获取 IP 失败" + LogUtil.detail(e));
            ipAddress = "";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }
}
