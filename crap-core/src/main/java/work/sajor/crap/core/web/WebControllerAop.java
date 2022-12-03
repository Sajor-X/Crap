package work.sajor.crap.core.web;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import work.sajor.crap.core.logger.LogUtil;
import work.sajor.crap.core.logger.OperationLogger;
import work.sajor.crap.core.logger.RequestLogger;
import work.sajor.crap.core.security.facade.WebUserService;
import work.sajor.crap.core.security.util.SecurityUtil;
import work.sajor.crap.core.session.SessionUtil;
import work.sajor.crap.core.util.AopUtil;
import work.sajor.crap.core.web.dto.JsonResponse;
import work.sajor.crap.core.web.response.ResponseBuilder;
import work.sajor.crap.core.web.response.ResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * <p>
 * 控制器AOP统一返回
 * </p>
 *
 * @author Sajor
 * @since 2022-12-03
 */
@Aspect
@ControllerAdvice
@Component
@Slf4j
public class WebControllerAop {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    RequestLogger requestLogger;

    @Autowired
    WebUrlLock webUrlLock;

    @Autowired
    WebUserService webUserService;

    @Pointcut("execution(* work.sajor..controller..*(..)) || execution(* work.sajor.crap.core.web.WebController.*(..))")
    private void controller() {
    }

    @Before("controller()")
    public void before(JoinPoint point) {
    }

    /**
     * Around Aop
     */
    @SuppressWarnings("ConstantConditions")
    @Around("controller()")
    public Object around(ProceedingJoinPoint point) {

        Object response = SessionUtil.call(() -> {                              // session context

            return requestLogger.call((logContext) -> {                         // request-logger context

                String protocol = "http";
                if (request.getProtocol().toLowerCase().startsWith("https")) {
                    protocol = "https";
                }
                logContext.setHost(protocol + "://" + request.getRemoteHost() + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort()));
                logContext.setMethod(request.getMethod());
                logContext.setUri(request.getRequestURI());
                logContext.setHeader(WebUtil.getHeader());
                logContext.setBody(WebUtil.getBody());

                webUserService.updateUserLastOpTime(SessionUtil.getUserId());   // 更新 lastOpTime

                // --- url 加锁 ---
                Method method = AopUtil.getMethod(point);
                boolean urlLockEnabled = false;
                boolean sessionLock = false;
                int ttl = 0;
                if (method != null) {
                    UrlLock urlLock = method.getAnnotation(UrlLock.class);
                    if (urlLock != null) {
                        sessionLock = urlLock.session();
                        ttl = urlLock.ttl();
                        urlLockEnabled = true;
                    }
                }

                // --- 处理 ---
                OperationLogger.init();

                Object result = null;
                if (urlLockEnabled) {
                    result = webUrlLock.call(point::proceed, sessionLock);      // url-lock context
                } else {
                    result = point.proceed();
                }

                // --- 操作日志 ---
                try {
                    OperationLogger.save(point.getThis().getClass(), method, result);
                } catch (Exception e) {
                    log.error("OP LOG ERROR" + LogUtil.detail(e));
                }

                return result;

            }, "HTTP");

        }, SecurityUtil.getPrinciple());

        // --- 内部转发 ---
        if (response instanceof String && ((String) response).startsWith("forward")) {
            return response;
        }

        // --- 统一包装输出 ---
        ResponseWrapper classAnnotation = point.getThis().getClass().getAnnotation(ResponseWrapper.class);
        if (classAnnotation != null && classAnnotation.value()) {
            response = response instanceof JsonResponse ? response : ResponseBuilder.success(response);
        }

        return response;
    }

    /**
     * 异常拦截
     *
     * @param e
     * @return
     */
    @ExceptionHandler(WebException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JsonResponse jsonException(WebException e) {
        log.error("REQUEST ERROR " + LogUtil.detail(e));
        return ResponseBuilder.error(e.getMessage());
    }

    /**
     * 异常拦截
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JsonResponse jsonException(Exception e) {
        log.error("REQUEST ERROR " + LogUtil.detail(e));
        return ResponseBuilder.error("", JsonResponse.Status.ERROR, e.getMessage());
    }
}
