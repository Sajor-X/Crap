package work.sajor.crap.core.logger;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <p>
 * 日志辅助工具
 * </p>
 *
 * @author Sajor
 * @since 2022-11-10
 */
@Slf4j
public class LogUtil {
    
    /**
     * 获取 logger 实例
     */
    public static Logger getLogger() {
        return log;
    }
    
    /**
     * 拼接日志明细
     * 每个参数占一行, 缩进
     */
    public static String lines(Object... detail) {
        StringBuilder info = new StringBuilder();
        for (Object o : detail) {
            info.append("\n\t - ").append(toString(o));
        }
        return info.toString();
    }
    
    /**
     * 拼接日志明细
     * 每两个参数占一行, 冒号分隔, 缩进
     */
    public static String detail(Object... detail) {
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < detail.length; i += 2) {
            if (i + 1 < detail.length) {
                list.add(
                    toString(detail[i]) + " : " + toString(detail[i + 1]));
            } else {
                list.add(detail[i]);
            }
        }
        return lines(list.toArray());
    }
    
    /**
     * 拼接异常日志, 递归打印上级异常
     */
    private static String exception(Throwable e) {
        int i = 0;
        StringBuilder info = new StringBuilder();
        while (e != null && i++ < 10) {
            if (i > 1) {
                info.append("Caused by ");
            }
            info.append(e.getClass().getName());
            info.append(" : ").append(e.getMessage()).append("\n\tat ")
                .append(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n\tat ")));
            info.append("\n");
            e = e.getCause();
        }
        return info.toString();
    }
    
    private static String toString(Object o) {
        if (o instanceof String) {
            return (String) o;
        }
        if (o instanceof Throwable) {
            return exception(((Throwable) o));
        }
        // TODO json
        return "";
//        return JacksonUtil.serialize(o);
    }
}
