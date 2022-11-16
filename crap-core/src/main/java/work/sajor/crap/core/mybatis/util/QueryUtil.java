package work.sajor.crap.core.mybatis.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class QueryUtil {
    
    private static SqlSessionTemplate sqlSessionTemplate;
    
    private static final int SELECT = 1;
    private static final int INSERT_UPDATE = 2;
    private static final int EXECUTE = 3;
    
    
    @Autowired
    private void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        QueryUtil.sqlSessionTemplate = sqlSessionTemplate;
    }
    
    /**
     * 执行新增或更新 sql
     */
    public static boolean execute(String sql) { return ((Integer) runSql(sql, INSERT_UPDATE)) >= 0;}
    
    /**
     * 执行 sql
     * insert 和 update 语句即使成功也会返回 false
     */
    public static boolean command(String sql) { return (boolean) runSql(sql, EXECUTE);}
    
    /**
     * 查询 sql
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> query(String sql) { return (List<Map<String, Object>>) runSql(sql, SELECT);}
    
    /**
     * 查询 sql, 单条结果
     */
    public static Map<String, Object> get(String sql) {
        List<Map<String, Object>> list = query(sql);
        return list.size() > 0 ? list.get(0) : null;
    }
    
    // ------------------------------ inner methods ------------------------------
    
    private static Object runSql(String sql, int type) {
        
        PreparedStatement statement = null;
        SqlSession session = getSqlSession();
        ResultSet result = null;
        
        try {
            statement = session.getConnection().prepareStatement(sql);
            log.debug(sql);
        } catch (Exception e) {
            throw new RuntimeException("创建 sql 失败", e);
        }
        
        try {
            switch (type) {
                case SELECT:
                    return toList(statement.executeQuery());
                case INSERT_UPDATE:
                    return statement.executeUpdate();
                default:
                    return statement.execute();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            try {
                statement.close();
            } catch (Exception ignore) {}
            closeSqlSession(session);
        }
    }
    
    /**
     * 获取sqlSession
     *
     * @return
     */
    private static SqlSession getSqlSession() {
        return SqlSessionUtils.getSqlSession(sqlSessionTemplate.getSqlSessionFactory(),
            sqlSessionTemplate.getExecutorType(), sqlSessionTemplate.getPersistenceExceptionTranslator());
    }
    
    /**
     * 关闭 sqlSession
     *
     * @param session
     */
    private static void closeSqlSession(SqlSession session) {
        try {
            SqlSessionUtils.closeSqlSession(session, sqlSessionTemplate.getSqlSessionFactory());
        } catch (Exception e) {
            log.error("关闭 sqlSession 失败", e);
        }
    }
    
    /**
     * 结果集转换为 list
     */
    private static List<Map<String, Object>> toList(ResultSet rs) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                Map<String, Object> rowData = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), rs.getObject(i));
                }
                list.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) { rs.close(); }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
