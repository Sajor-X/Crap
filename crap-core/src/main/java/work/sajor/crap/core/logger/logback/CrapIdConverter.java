package work.sajor.crap.core.logger.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import work.sajor.crap.core.config.ApplicationConfig;
import work.sajor.crap.core.logger.RequestLogger;

import java.util.Objects;

/**
 * <p>
 * logback 模板解析器
 * 返回当前节点 ID
 * </p>
 *
 * @author Sajor
 * @since 2022-12-07
 */
@Component
public class CrapIdConverter extends ClassicConverter {

    private static String nodeId = "";

    private static RequestLogger requestLogger;


    @Autowired
    private void setKconeConfig(ApplicationConfig applicationConfig) {
        nodeId = applicationConfig.getNodeId();
    }

    @Autowired
    private void setRequestLogger(RequestLogger requestLogger) {
        CrapIdConverter.requestLogger = requestLogger;
    }

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
//        String requestId = requestLogger == null ? "" : requestLogger.getId();
//        return "[" + nodeId + "] [" + iLoggingEvent.getThreadName() + "] [" + requestId + "]";
        String res = "[%s] [%s] [%s]";
        return String.format(res, nodeId, iLoggingEvent.getThreadName(), Objects.isNull(requestLogger) ? "" : requestLogger.getId());
    }
}
