package work.sajor.crap.core.web;

/**
 * <p>
 * 通用异常
 * </p>
 *
 * @author Sajor
 * @since 2022-11-14
 */
public class WebException extends RuntimeException {

    private String detailMessage;

    public WebException() {
        this("系统异常");
    }

    public WebException(String message) {
        detailMessage = message;
    }

    public WebException(String message, Throwable e) {
        super(message, e);
        detailMessage = message;
    }

    public String getMessage() {
        return detailMessage;
    }
}
