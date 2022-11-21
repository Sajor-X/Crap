package work.sajor.crap.core.mybatis.mapper.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

public class SelectMapList extends AbstractMethod {

    public SelectMapList() {
        super("selectMapList");
    }

    public SelectMapList(String methodName) {
        super(methodName);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = String.format(
                "<script>SELECT %s FROM %s %s %s\n</script>",
                getFieldsString(),
                tableInfo.getTableName(),
                getJoinString(),
                sqlWhereEntityWrapper(true, tableInfo)
        );
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);

        return addMappedStatement(mapperClass, methodName, sqlSource, SqlCommandType.SELECT, null,
                null, Map.class, new NoKeyGenerator(), null, null);
    }

    protected String getFieldsString() {
        String fieldsSql = "<if test=\"ew.fields == null\">*</if><if test=\"ew.fields != null\">%s</if>";
        String wrapperFields = "<foreach item = \"alias\" index = \"item\" collection=\"ew.fields\" open=\"\" close=\"\" separator=\",\">${item} AS `${alias}`</foreach>";
        return String.format(fieldsSql, wrapperFields);
    }

    protected String getJoinString() {
        return "<if test=\"ew.join != null\">${ew.join}</if>";
    }
}
