package work.sajor.crap.core.logger;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.IdUtil;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import work.sajor.crap.core.config.ApplicationConfig;
import work.sajor.crap.core.web.WebException;
import work.sajor.crap.core.web.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * <p>
 * 请求日志
 *
 * 请求异常时记录到数据库
 * </p>
 *
 * @author Sajor
 * @since 2022-12-03
 */
@Component
@Slf4j
public class RequestLogger {

    @Value("${crap.path.log}")
    String logPath;

    // 取消配置
    // @Value("${crap.log.request.file}")
    boolean saveFile = false;

    // 取消配置
    // @Value("${crap.log.request.db}")
    boolean saveDb = false;

    @Autowired
    private HttpServletRequest request;

//    @Autowired
//    ErrorLogDao errorLogDao;
//
//    @Autowired
//    ErrorLogDetailDao errorLogDetailDao;

    @Autowired
    ApplicationConfig applicationConfig;

    private final String TIMER = "REQUEST_START";

    private final String REQUEST_ID = "REQUEST_ID";

    private final String REQUEST_TYPE = "REQUEST_TYPE";

    private ThreadLocal<LogContext> logContext = new ThreadLocal<>();

    private final String DEFAULT_LOG_ID = "DEFAULT-0000000000000000000";

    @SneakyThrows
    public <T> T call(Callable<T> callable, String type) {
        logContext.set(new LogContext());
        try {
            start(type);
            T result = callable.call(logContext.get());
            collect();
            return result;
        } catch (Exception e) {
            setException(e);
            collect();
            throw e;
        }
    }

    /**
     * 日志 ID
     */
    public String getId() {
        try {
            return new String(logContext.get().getId().getBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 异常未抛出时, 使用这个方法将请求标记为异常
     */
    public void setException(Exception e) {
        if (logContext.get() == null) {
            throw new WebException("RequestLogger : LogContext is null");
        }
        logContext.get().setException(e);
    }

    // ------------------------------ helper ------------------------------

    /**
     * 标记日志分块开始
     */
    private void start(String type) {

        // --- MDC ---
        String id = IdUtil.getSnowflake(0, 0).nextIdStr();
        MDC.put(REQUEST_TYPE, type);
        MDC.put(REQUEST_ID, id);
        MDC.put(TIMER, String.valueOf(System.currentTimeMillis()));

        logContext.get().setId(type + "-" + id);

        // --- PRINT ---
        try {
            log.debug("URL     : {}", request.getRequestURL());
            log.debug("REQUEST : " + WebUtil.getBody());
        } catch (Exception ignore) {}
    }

    private void collect() {
        try {
            // --- 收集日志信息 ---
            LogContext context = this.logContext.get();
            String info = "";
            String requestFile = logPath + "/tmp/" + MDC.get(REQUEST_ID);       // todo 配置 tmp 路径, 保持与 logback-spring.xml 一致
            Exception e = context.getException();
            boolean saveDb = this.saveDb || e != null;
            if (saveFile || saveDb) {
                try {
                    FileReader fileReader = new FileReader(requestFile);
                    info = fileReader.readString();

                    if (saveFile) {
                        // --- 将日志写入按天分组的文件内 ---
                        FileWriter fileWriter = new FileWriter(logPath + "/request-" + LocalDate.now().toString() + ".log");
                        fileWriter.append(info + "\n\n");
                    }
                } catch (IORuntimeException ignore) {}

                // --- 写入 DB ---
                /*
                if (saveDb) {
                    ErrorLog log = errorLogDao.getEntity();
                    log.setHost(context.getHost());
                    log.setMethod(context.getMethod());
                    log.setUri(context.getUri());
                    log.setNodeId(applicationConfig.getNodeId());
                    String exception = (e == null ? "" : (e.getMessage() == null ? "NullPointerException" : e.getMessage()));
                    log.setException(exception.substring(0, Math.min(exception.length(), 1000)));
                    log.setStartTime(LocalDateUtil.toTime(Long.parseLong(MDC.get(TIMER))));
                    log.setCost(BigDecimal.valueOf(Math.ceil(System.currentTimeMillis() - Long.parseLong(MDC.get(TIMER))) / 1000));
                    log.setIp(WebUtil.getIp());
                    errorLogDao.save(log);

                    ErrorLogDetail logDetail = errorLogDetailDao.getEntity();
                    logDetail.setId(log.getId());
                    logDetail.setHeader(context.getHeader());
                    logDetail.setBody(context.getBody());
                    logDetail.setInfo(info);
                    errorLogDetailDao.save(logDetail);
                }
                 */
            }

            FileUtil.del(requestFile);
        } finally {
            logContext.set(null);
            MDC.clear();
        }
    }

    // ------------------------------ inner interface ------------------------------

    public interface Callable<V> {

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         * @throws Exception if unable to compute a result
         */
        V call(LogContext logContext) throws Throwable;
    }

    @Data
    public static class LogContext {

        private String id;

        private String host = "";

        private String header = "";

        private String method = "";

        private String uri = "";

        private String body = "";

        private Exception exception = null;
    }
}
