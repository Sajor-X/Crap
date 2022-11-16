package work.sajor.crap.core.mybatis.support;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface TableCode {
    
    /**
     * 前缀
     */
    String value() default "";
}
