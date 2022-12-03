package work.sajor.crap.core.util;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import work.sajor.crap.core.logger.LogUtil;

import java.lang.reflect.Method;

/**
 * <p>
 * aop 辅助工具
 * </p>
 *
 * @author Sajor
 * @since 2022-12-03
 */
@Slf4j
public class AopUtil {

    /**
     * 获取切点拦截的方法
     *
     * @param point
     * @return
     */
    public static Method getMethod(ProceedingJoinPoint point) {

        Method method = null;

        try {
            Object target = point.getTarget();                                  // 实体类
            String methodName = point.getSignature().getName();                 // 方法名称
            Object[] args = point.getArgs();                                    // 方法参数
            Class[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();          // 放参数类型
            method = target.getClass().getMethod(methodName, parameterTypes);

            if (method.isBridge()) {                                            // 如果是桥则要获得实际 method
                for (int i = 0; i < args.length; i++) {
                    Class genClazz = GenericUtil.getSuperClassGenericType(target.getClass(), i);                        // 获得泛型类型
                    if (args[i].getClass().isAssignableFrom(genClazz)) {        // 根据实际参数类型替换 parameterType 中的类型
                        parameterTypes[i] = genClazz;
                    }
                }
                method = target.getClass().getMethod(methodName, parameterTypes);                                       // 获得 parameterType 参数类型的方法
            }
        } catch (Throwable e) {
            log.error(LogUtil.detail("AOP 获取 method 失败", e));
        }

        return method;
    }
}
