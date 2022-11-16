package work.sajor.crap.core.mybatis.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.*;
import work.sajor.crap.core.mybatis.support.Wrapper;
import work.sajor.crap.core.util.CommonUtil;
import work.sajor.crap.core.util.ListUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlUtil {
    
    /**
     * 用 sql 构建 wrapper
     */
    public static <T> Wrapper<T> fillWrapper(String sql, Wrapper<T> wrapper, boolean count) {
        String sqlTemplate = null;
        SQLSelectQueryBlock query = parseSelect(sql);
        
        if (query != null) {
            
            // --- select ---
            List<SQLSelectItem> selectItems = query.getSelectList();
            sqlTemplate = count
                ? "SELECT COUNT(*) total"
                : "SELECT " + String.join(",", ListUtil.toList(selectItems, SQLSelectItem::toString));
            
            // --- from ---
            // 单表 : table instanceof SQLExprTableSource
            // join : table instanceof SQLJoinTableSource
            // 子查询 : table instanceof SQLSubqueryTableSource
            SQLTableSource table = query.getFrom();
            sqlTemplate += " FROM " + table;
            
            // --- where ---
            // 表达式 : where instanceof SQLBinaryOpExpr
            // 子查询 : where instanceof SQLInSubQueryExpr
            SQLExpr where = query.getWhere();
            if (where != null) {
                String sp = wrapper.getExpression().getNormal().size() == 0 ? "" : " AND ";
                wrapper.getExpression().getNormal().add(() -> sp + where.toString());
            }
            
            // --- group by ---
            SQLSelectGroupByClause groupBy = query.getGroupBy();
            if (groupBy != null) {
                wrapper.groupBy(groupBy.toString());
            }
            
            // --- order by ---
            SQLOrderBy orderBy = query.getOrderBy();
            if (orderBy != null && !count) {
                for (SQLSelectOrderByItem orderItem : orderBy.getItems()) {
                    wrapper.orderBy(true, orderItem.getType().name.equalsIgnoreCase("ASC"), orderItem.getExpr().toString());
                }
            }
            
            // --- limit ---
            SQLLimit limit = query.getLimit();
            if (limit != null) {
                String limitSql = "LIMIT " + limit.getOffset().toString();
                if (StrUtil.isNotEmpty(limit.getRowCount().toString())) {
                    limitSql += "," + limit.getRowCount().toString();
                }
                wrapper.last(limitSql);
            }
        }
        
        wrapper.setSqlTemplate(sqlTemplate);
        
        return wrapper;
    }
    
    /**
     * 提取 sql 中的 别名=>字段名映射
     */
    public static Map<String, String> getAlias(String sql) {
        
        HashMap<String, String> alias = new HashMap<>();
        SQLSelectQueryBlock query = parseSelect(sql);
        
        if (query != null) {
            List<SQLSelectItem> selectItems = query.getSelectList();
            for (SQLSelectItem selectItem : selectItems) {
                if (StrUtil.isNotEmpty(selectItem.getAlias())) {
                    alias.put(
                        CommonUtil.trimString(selectItem.getAlias(), "\"'`"),
                        selectItem.getExpr().toString());
                }
            }
        }
        
        return alias;
    }
    
    /**
     * 获取非 union 的 sql 解析对象
     */
    public static SQLSelectQueryBlock parseSelect(String sql) {
        SQLSelectQuery sqlSelectQuery = ((SQLSelectStatement) SQLUtils.parseSingleMysqlStatement(sql)).getSelect().getQuery();
        return sqlSelectQuery instanceof SQLSelectQueryBlock ? (SQLSelectQueryBlock) sqlSelectQuery : null;
    }
}
