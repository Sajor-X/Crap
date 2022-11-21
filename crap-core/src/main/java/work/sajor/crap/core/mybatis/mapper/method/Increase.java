package work.sajor.crap.core.mybatis.mapper.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 字段值自增方法
 */
public class Increase extends AbstractMethod {

    public Increase() {
        super("increase");
    }

    public Increase(String methodName) {
        super(methodName);
    }

    /**
     * 字段自增
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {

        String sql = String.format("<script>\nUPDATE %s <set>%s</set> %s\n</script>",
                tableInfo.getTableName(),
                getSqlSet(tableInfo),
                sqlWhereEntityWrapper(true, tableInfo)
        );
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
    }

    protected String getSqlSet(TableInfo tableInfo) {
        StringBuilder sqlSet = new StringBuilder();

        // tableInfo.getFieldList() 不包含主键
        for (TableFieldInfo tableFieldInfo : tableInfo.getFieldList()) {
            sqlSet.append(String.format("\n<if test=\"cm['%s'] !=null\">%s=%s+#{cm.%s},</if>",
                    tableFieldInfo.getColumn(),
                    tableFieldInfo.getColumn(),
                    tableFieldInfo.getColumn(),
                    tableFieldInfo.getColumn()));
        }
        return sqlSet.append("\n").toString();
    }
}
