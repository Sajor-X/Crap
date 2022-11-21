package work.sajor.crap.core.mybatis.support;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import work.sajor.crap.core.mybatis.mapper.method.Increase;
import work.sajor.crap.core.mybatis.mapper.method.SelectMapList;
import work.sajor.crap.core.mybatis.mapper.method.SelectMapPage;
import work.sajor.crap.core.mybatis.mapper.method.SelectSqlList;

import java.util.List;

/**
 * <p>
 * Mybatis配置
 * </p>
 *
 * @author Sajor
 * @since 2022-11-21
 */
@Configuration
@MapperScan("work.sajor.**.mapper")
@EnableTransactionManagement
public class MybatisConfig {


    /**
     * 扩展 mapper
     */
    @Bean
    public DefaultSqlInjector sqlInjector() {
        return new SqlInjector();
    }


    /**
     * 乐观锁
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    /**
     * 扩展 mapper
     */
    static class SqlInjector extends DefaultSqlInjector {
        @Override
        public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
            List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
            methodList.add(new Increase());
            methodList.add(new SelectMapList());
            methodList.add(new SelectMapPage());
            methodList.add(new SelectSqlList());
            return methodList;
        }
    }
}
