package work.sajor.crap.core.web.response;

import java.lang.annotation.*;

/**
 * <p>
 * 标记是否对响应进行统一包装
 * </p>
 *
 * @author Sajor
 * @since 2022-11-15
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ResponseWrapper {

    boolean value() default true;
}
