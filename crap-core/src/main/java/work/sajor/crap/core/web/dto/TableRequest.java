package work.sajor.crap.core.web.dto;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 列表请求DTO
 * </p>
 *
 * @author Sajor
 * @since 2022-11-15
 */
@Data
public class TableRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DICT_TYPE = "table";

    // ------------------------------ properties ------------------------------

    private String type = DICT_TYPE;

    /**
     * 搜索条件只允许数字,字母,下划线,点号,竖线
     */
    // private String KEY_FORMAT = "^[a-zA-Z\\d_\\.|\\u4e00-\\u9fa5]+$";
    private static final String KEY_FORMAT = "^[a-zA-Z\\d_\\.|]+$";

    /**
     * 页码
     */
    private int pageNo = 1;

    /**
     * 条数
     */
    private int pageSize = 10;

    /**
     * 连表数据附加的字段前缀, 屏蔽前端字段名差异
     */
    private String prefix = "";

    /**
     * 索引高的排序条件优先级更高
     */
    private List<SortItem> sort = new ArrayList<>();

    /**
     * 检索条件
     */
    private SearchWhere where = new SearchWhere();

    /**
     * 合计
     */
    private List<TotalItem> totalColumns = new ArrayList<>();

    /**
     * 附加参数
     */
    private Map<String, Object> extra = new HashMap<>();

    // ------------------------------ Getter / Setter ------------------------------

    /**
     * 获取 key 列表, 清理注入
     * key 防注入
     */
    public List<TotalItem> getTotalColumns() {
        return totalColumns
                .stream()
                .filter(item -> ReUtil.isMatch(KEY_FORMAT, item.getKey()))
                .collect(Collectors.toList());
    }

    /**
     * 排序列表
     * key 防注入
     */
    public List<SortItem> getSort() {
        return sort
                .stream()
                .filter(item -> ReUtil.isMatch(KEY_FORMAT, item.getKey()))
                .collect(Collectors.toList());
    }

    public void setWhere(List<SearchItem> whereList) {
        where.setList(whereList);
    }

    // ------------------------------ inner class ------------------------------

    @Data
    public static class SearchWhere implements Map<String, SearchItem> {

        /**
         * 过滤后的请求
         */
        private List<SearchItem> list = new ArrayList<>();

        /**
         * 原始请求
         */
        private List<SearchItem> rawList = new ArrayList<>();

        /**
         * 清理 value 是 null 的搜索项
         * 排除掉含有非法字符的 key
         */
        public void setList(List<SearchItem> list) {
            this.rawList = list;
            this.list = list.stream().filter(                                   // 清理
                    item -> item.getValue() != null && ReUtil.isMatch(KEY_FORMAT, item.getKey())
            ).collect(Collectors.toList());
        }

        /**
         * 添加搜索项
         */
        public void add(String key, Object value) {
            put(key, new SearchItem(key, value));
        }

        /**
         * 添加搜索项
         */
        public void add(String key, Object value, SqlKeyword op) {
            put(key, new SearchItem(key, value, op));
        }

        /**
         * 添加搜索项
         */
        public void add(SearchItem searchItem) {
            put(searchItem.getKey(), searchItem);
        }

        /**
         * 获取搜索值
         */
        public Object getValue(String key) {
            SearchItem searchItem = get(key);
            return searchItem == null ? null : searchItem.getValue();
        }

        @Override
        public int size() {
            return this.list.size();
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }


        @Override
        public boolean containsKey(Object key) {
            for (SearchItem searchItem : this.list) {
                if (searchItem.getKey().equals(key)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public SearchItem get(Object key) {
            for (SearchItem searchItem : this.list) {
                if (searchItem.getKey().equals(key)) {
                    return searchItem;
                }
            }
            return null;
        }

        @Override
        public SearchItem put(String key, SearchItem value) {
            for (int i = 0; i < this.list.size(); i++) {
                if (this.list.get(i).getKey().equals(key)) {
                    this.list.set(i, value);
                    return value;
                }
            }
            this.list.add(value);
            return value;
        }

        @Override
        public SearchItem remove(Object key) {
            SearchItem value = null;
            for (int i = this.list.size() - 1; i > -1; i--) {
                if (this.list.get(i).getKey().equals(key)) {
                    value = this.list.get(i);
                    this.list.remove(i);
                }
            }
            return value;
        }

        @Override
        public void putAll(Map<? extends String, ? extends SearchItem> m) {
            if (m != null) {
                m.forEach(this::put);
            }
        }

        @Override
        public void clear() {
            while (list.size() > 0) {
                list.remove(0);
            }
        }

        @Override
        public Set<String> keySet() {
            Set<String> set = new HashSet<>();
            this.list.forEach(searchItem -> set.add(searchItem.getKey()));
            return set;
        }

        @Override
        public Collection<SearchItem> values() {
            return list;
        }

        @Override
        public Set<Entry<String, SearchItem>> entrySet() {
            HashMap<String, SearchItem> map = new HashMap<>();
            this.list.forEach(searchItem -> map.put(searchItem.getKey(), searchItem));
            return map.entrySet();
        }
    }

    /**
     * 搜索项
     */
    @Data
    @NoArgsConstructor
    public static class SearchItem {

        private String key;
        private SqlKeyword op = SqlKeyword.EQ;
        private Object value;

        /**
         * 用于内部构造请求条件
         *
         * @param key
         * @param value
         */
        public SearchItem(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        /**
         * 用于内部构造请求条件
         *
         * @param key
         * @param value
         */
        public SearchItem(String key, Object value, SqlKeyword op) {
            this.key = key;
            this.value = value;
            this.op = op;
        }

        /**
         * 用于接收字符串类型的值
         *
         * @param op
         */
        public void setOp(String op) {
            if (StrUtil.isEmpty(op)) {
                op = "like";
            }
            this.op = SqlKeyword.valueOf(op.toUpperCase());
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SortItem {

        /**
         * 排序字段
         */
        private String key;

        /**
         * 排序方向 asc / desc
         */
        private String order;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TotalItem {

        /**
         * 合计字段
         */
        private String key;

        /**
         * 合计格式
         */
        private Integer scale = 0;

        /**
         * 除数
         */
        private Integer divisor = 1;

        /**
         * 舍进模式
         */
        private Integer round;
    }
}
