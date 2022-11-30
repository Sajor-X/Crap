package work.sajor.crap.core.util;

import cn.hutool.core.util.ReUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;

/**
 * <p>
 * 系统工具
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Component
public class SpringUtil extends cn.hutool.extra.spring.SpringUtil implements ApplicationContextAware {


    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
    }

    /**
     * 获取 Bean Name
     */
    public static String getBeanName(Class clazz) {
        String className = clazz.getName();
        String beanClass = className.substring(className.lastIndexOf(".") + 1, clazz.getName().length());
        return beanClass.substring(0, 1).toLowerCase() + beanClass.substring(1, beanClass.length());
    }

    /**
     * 等待事务完成后执行
     */
    public static void runAfterTransaction(Runnable runnable) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    runnable.run();
                }
            });
        } else {
            runnable.run();
        }
    }

    /**
     * 替换字符串中的 springEL 表单式
     *
     * @param string  待替换的字符串, eg: SELECT * FROM table WHERE a={['webUser'].id}
     * @param pattern 表达式占位正则, eg: "\\{.*\\}"
     * @param data    环境变量, eg: Map<String,Object> {"webUser":webUser}
     */
    public static String elParseString(String string, String pattern, Map<String, Object> data) {
        return ReUtil.replaceAll(string, pattern, parameter -> {
            String text = string.substring(parameter.start() + 1, parameter.end() - 1);
            return elGetValue(text, data).toString();
        });
    }

    /**
     * 计算 springEL 表达式
     */
    public static Object elGetValue(String script, Map<String, Object> data) {
        EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression(script).getValue(context, data);
    }

    /**
     * 调用 bean
     */
    // public static Object call(String beanName,String method,){
    //
    // }
}
