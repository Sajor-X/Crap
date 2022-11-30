package work.sajor.crap.core.web.dto;

import lombok.Data;
import lombok.Getter;

/**
 * <p>
 * 返回类
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Data
public class JsonResponse {

    private int code;

    private String message;

    private Object data;

    public enum Status {
        SUCCESS(0, "操作成功"),
        ERROR(100, "操作失败"),
        UNAUTHORIZED(101, "未登录"),
        FORBIDDEN(102, "未授权"),
        EXCEPTION(103, "系统异常");

        @Getter
        private int code;

        @Getter
        private String message;

        Status(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
