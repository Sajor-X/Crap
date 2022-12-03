package work.sajor.crap.core.logger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 记录操作日志
 * </p>
 *
 * @author Sajor
 * @since 2022-12-03
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Oplog {
    
    /**
     * 操作标题模板
     */
    String title() default "[{ACTION}] {TYPE} : {CODE}";
    
    /**
     * 操作标题 : 动作
     * 默认当前地址对应的三级菜单名称, 如新增/编辑/删除
     */
    String action() default "";
    
    /**
     * 操作标题 : 类别
     * 默认当前地址对应的二级菜单名称, 如采购订单/入库单
     */
    String type() default "";
    
    /**
     * 预览地址
     * 完整路径, 斜线开头 : /module/controller/action 或 /module/controller/action/{id}
     * 相对路径, 非斜线开头, 用当前控制器地址补全 : action 或 action/{id}
     */
    String url() default "view/{ID}";
    
    /**
     * 是否必须提供 id
     */
    boolean id() default true;
    
    /**
     * 是否必须提供 code
     */
    boolean code() default true;
}
