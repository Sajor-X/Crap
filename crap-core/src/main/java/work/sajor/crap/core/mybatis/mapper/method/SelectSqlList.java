package work.sajor.crap.core.mybatis.mapper.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

public class SelectSqlList extends AbstractMethod {
    public SelectSqlList() {
        super("selectSqlList");
    }

    public SelectSqlList(String methodName) {
        super(methodName);
    }

    /**
     * sqlTemplate 只包含 where 前的部分
     * where 后的字句都已组装到 wrapper 内
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = String.format(
                "<script>${ew.sqlTemplate} %s\n</script>",
                sqlWhereEntityWrapper(true, tableInfo)
        );
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);

        return addMappedStatement(mapperClass, methodName, sqlSource, SqlCommandType.SELECT, null,
                null, Map.class, new NoKeyGenerator(), null, null);
    }
}
