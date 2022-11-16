package work.sajor.crap.core.web;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UrlLock {
    
    /**
     * 最大加锁时间
     */
    int ttl() default 600;
    
    /**
     * session 级别锁, 主要用于防止重复新增, 默认关闭
     */
    boolean session() default false;
    
    @AliasFor("session")
    boolean value() default false;
}
