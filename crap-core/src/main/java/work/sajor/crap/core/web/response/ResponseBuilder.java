package work.sajor.crap.core.web.response;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import work.sajor.crap.core.web.dto.JsonResponse;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 返回构造器
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
public class ResponseBuilder {

    /**
     * @param params 参数顺序 : data / message / code
     */
    public static JsonResponse success(Object... params) {
        Object data = null;
        Object message = "";
        Object code = JsonResponse.Status.SUCCESS;
        try {
            data = params[0];
            message = params[1];
            code = params[2];
        } catch (Throwable e) {
            //
        }
        return build(data, message, code);
    }

    /**
     * @param params 参数顺序 : message / code / data
     */
    public static JsonResponse error(Object... params) {
        Object data = null;
        Object message = "";
        Object code = JsonResponse.Status.ERROR;
        try {
            message = params[0];
            code = params[1];
            data = params[2];
        } catch (Throwable e) {
            //
        }
        return build(data, message, code);
    }

    /**
     * Table data response
     */
    public static JsonResponse success(Page page) {
        HashMap<Object, Object> result = new HashMap<>();
        result.put("list", page.getRecords());
        result.put("total", page.getTotal());
        return success(result);
    }

    /**
     * Table data response
     */
    public static JsonResponse success(List list) {
        HashMap<Object, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", list.size());
        return success(result);
    }

    private static JsonResponse build(Object data, Object message, Object code) {
        JsonResponse response = new JsonResponse();
        response.setData(data);

        if (code instanceof JsonResponse.Status) {
            response.setCode(((JsonResponse.Status) code).getCode());
            response.setMessage(((JsonResponse.Status) code).getMessage());
        } else {
            JsonResponse.Status status = getStatus(String.valueOf(code));
            response.setCode(status.getCode());
            response.setMessage(status.getMessage());
        }

        if (message != null && StrUtil.isNotEmpty(String.valueOf(message))) {
            response.setMessage(String.valueOf(message));
        }

        return response;
    }

    private static JsonResponse.Status getStatus(String code) {
        try {
            return JsonResponse.Status.valueOf(code.toUpperCase());
        } catch (Throwable e) {
            throw new RuntimeException("Invalid code : " + code);
        }
    }
}
