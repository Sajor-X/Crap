package work.sajor.crap.core.web.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Component;
import work.sajor.crap.core.mybatis.dao.BaseDao;
import work.sajor.crap.core.mybatis.facade.Entity;
import work.sajor.crap.core.mybatis.mapper.Mapper;
import work.sajor.crap.core.mybatis.support.EntityInfo;
import work.sajor.crap.core.mybatis.support.Wrapper;
import work.sajor.crap.core.mybatis.util.EntityInfoUtil;
import work.sajor.crap.core.web.dto.TableRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 表格请求处理器
 * </p>
 *
 * @author Sajor
 * @since 2022-11-15
 */
@Component
public class TableRequestHandler {


    /**
     * 查询数据
     */
    public <M extends Mapper<T>, T extends Entity> Page<T> getData(BaseDao<M, T> dao, TableRequest tableRequest) {
        return dao.page(
                getPage(tableRequest),
                getWrapper(dao, tableRequest)
        );
    }

    /**
     * 查询数据
     */
    public <M extends Mapper<T>, T extends Entity> Page<T> getData(BaseDao<M, T> dao, TableRequest tableRequest, Wrapper<T> wrapper) {
        return dao.page(
                getPage(tableRequest),
                getWrapper(dao, tableRequest, wrapper)
        );
    }

    /**
     * 查询合计数据
     */
    public <M extends Mapper<T>, T extends Entity> Map<String, Object> getTotal(BaseDao<M, T> dao, TableRequest tableRequest, Wrapper<T> wrapper) {
        return getTotalMap(dao, tableRequest, getWrapper(dao, tableRequest, wrapper));
    }


    /**
     * 查询合计数据
     */
    public <M extends Mapper<T>, T extends Entity> Map<String, Object> getTotal(BaseDao<M, T> dao, TableRequest tableRequest) {
        return getTotalMap(dao, tableRequest, getWrapper(dao, tableRequest));
    }

    /**
     * 获取合计结果
     *
     * @param dao          dao
     * @param tableRequest table
     * @param wrapper      wrapper
     */
    public <M extends Mapper<T>, T extends Entity> Map<String, Object> getTotalMap(BaseDao<M, T> dao, TableRequest tableRequest,
                                                                                   Wrapper<T> wrapper) {
        // if (0 == tableRequest.getTotalColumns().size()) {
        //     return null;
        // }
        // EntityInfo<?> info = EntityInfoUtil.getInfo(dao.getEntity().getClass());
        // String prefix = StrUtil.isEmpty(tableRequest.getPrefix()) ? "" : tableRequest.getPrefix();
        //
        // ArrayList<String> totalList = new ArrayList<>();
        // for (TableRequest.TotalItem totalColumn : tableRequest.getTotalColumns()) {
        //     totalList.add(getFormatSql(totalColumn, prefix, info.getTable()));
        // }
        //
        // List<Map<String, Object>> maps = dao.listMap(totalList, wrapper);
        // if (1 != maps.size()) {
        //     return null;
        // }
        // return maps.get(0);
        return null;
    }

    /**
     * 分页对象
     */
    public <M extends Mapper<T>, T extends Entity> Page<T> getPage(TableRequest tableRequest) {
        return new Page<>(tableRequest.getPageNo(), tableRequest.getPageSize());
    }

    /**
     * 查询条件
     */
    public <M extends Mapper<T>, T extends Entity> Wrapper<T> getWrapper(BaseDao<M, T> dao, TableRequest tableRequest) {
        return getWrapper(dao, tableRequest, dao.getWrapper());
    }

    /**
     * 查询条件
     */
    public <M extends Mapper<T>, T extends Entity> Wrapper<T> getWrapper(BaseDao<M, T> dao, TableRequest tableRequest, Wrapper<T> wrapper) {
        return getWrapper(dao, tableRequest, wrapper, true);
    }

    /**
     * 查询条件
     */
    @SuppressWarnings("unchecked")
    public <M extends Mapper<T>, T extends Entity> Wrapper<T> getWrapper(BaseDao<M, T> dao, TableRequest tableRequest, Wrapper<T> wrapper, Boolean fixKey) {

        List<TableRequest.SortItem> sort = tableRequest.getSort();

        EntityInfo<?> info = EntityInfoUtil.getInfo(dao.getEntity().getClass());
        String prefix = StrUtil.isEmpty(tableRequest.getPrefix()) ? "" : tableRequest.getPrefix();                      // 字段前缀(表名)
        String table = info.getTable();

//        if (sort.size() > 0 && !tableRequest.getType().equals(ExportRequest.DICT_TYPE)) {                               // 导出请求不接收排序
//            ArrayList<String> check = new ArrayList<>();
//            for (int i = 0; i < sort.size(); i++) {
//                if (check.indexOf(sort.get(i).getKey()) == -1) {
//                    wrapper.orderBy(true,
//                            sort.get(i).getOrder().equalsIgnoreCase("asc"),
//                            formatKey(sort.get(i).getKey(), prefix, table, fixKey));
//                }
//            }
//        } else {
//            // wrapper.orderByDesc(formatKey(info.getId(), prefix, table));
//            // wrapper.orderBy(true,
//            //     tableRequest.getType().equals(ExportRequest.DICT_TYPE),                                                 // 默认降序, 导出请求默认升序
//            //     formatKey(info.getPk(), prefix, table, fixKey));
//        }

        // 检索条件
        for (TableRequest.SearchItem searchItem : tableRequest.getWhere().getList()) {
            if (searchItem.getValue() != null) {
                String key = searchItem.getKey();
                if (key.contains("|")) {                                                                                // 竖线分隔的 key 拆分成多个 or 组合条件, 用于模糊搜索
                    wrapper.nested(nestedWrapper -> {                                                                   // a|b|c -> ( a=1 or b=1 or c=1 )
                        for (String orKey : key.split("\\|")) {
                            nestedWrapper.or();
                            ((Wrapper) nestedWrapper).addCondition(
                                    formatKey(orKey, prefix, table, fixKey),
                                    searchItem.getOp(),
                                    searchItem.getValue());
                        }
                    });
                } else {
                    if (searchItem.getOp() == SqlKeyword.NOT_IN) {              // not in 特殊处理
                        wrapper.notIn(
                                formatKey(key, prefix, table, fixKey),
                                Convert.toList(searchItem.getValue()));
                    } else if (searchItem.getOp() == SqlKeyword.IN) {           // in 特殊处理
                        wrapper.in(
                                formatKey(key, prefix, table, fixKey),
                                Convert.toList(searchItem.getValue()));
                    } else if (searchItem.getOp() == SqlKeyword.BETWEEN) {
                        List<?> betweenValue = Convert.toList(searchItem.getValue());
                        String star = Convert.toStr(betweenValue.get(0), "");
                        star = star.length() > 10 ? star : star + " 00:00:00";

                        String end = Convert.toStr(betweenValue.size() > 1 ? betweenValue.get(1) : "", "");

                        end = end.length() > 10 ? end : end + " 23:59:59";
                        wrapper.between(formatKey(key, prefix, table, fixKey),
                                star,
                                end
                        );
                    }
//                    else if (searchItem.getOp() == SqlKeyword.LIKE_IN) {
//                        List<String> list = (List<String>) Convert.toList(searchItem.getValue());
//                        wrapper.nested(nestedWrapper -> {                      // key a,b,c -> ( k like a or k like b or k like c )
//                            for (String str : list) {
//                                nestedWrapper.or();
//                                ((Wrapper) nestedWrapper).addCondition(
//                                        formatKey(searchItem.getKey(), prefix, table, fixKey),
//                                        SqlKeyword.LIKE,
//                                        "%" + str + "%"
//                                );
//                            }
//                        });
//                    }
                    else {                                                    // 通用条件处理
                        wrapper.addCondition(
                                formatKey(key, prefix, table, fixKey),
                                searchItem.getOp(),
                                searchItem.getValue());
                    }
                }
            }
        }

        return wrapper;
    }

    /**
     * 格式化表格请求字段名
     */
    private String formatKey(String key, String prefix, String table, Boolean fixKey) {
        if (!fixKey) {                                                          // 动态表不修正 key
            return key;
        }
        if (key.contains(".")) {                                                // 字段中包含点号, 说明已指定表
            return prefix + key;
        }
        return table + "." + key;                                               // 补全表名, 防止字段重名
    }
}
