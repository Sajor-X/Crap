package work.sajor.crap.core.util;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import work.sajor.crap.core.web.WebException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ListUtil {
    
    /**
     * Collections.emptyList 返回的空列表不可编辑, 特殊场景下, 会产生和数据状态有关的错误, 测试可能覆盖不到
     */
    public static <T> List<T> emptyList() {
        return new ArrayList<>();
    }
    
    /**
     * map 转 list
     */
    public static <K, V> List<V> toList(Map<K, V> map) {
        ArrayList<V> list = new ArrayList<>();
        if (map != null) {
            map.forEach((k, v) -> {
                list.add(v);
            });
        }
        return list;
    }
    
    /**
     * 提取 list 元素中的属性, 构建新的 list
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(List list, String property, Class<T> clazz) {
        return toList(list, item -> (T) BeanUtil.getFieldValue(item, property));
    }
    
    /**
     * list 泛型转换
     */
    public static <T, E> List<T> toList(List<E> list, Class<T> clazz) {
        return toList(list, item -> BeanUtil.copyProperties(item, clazz));
    }
    
    /**
     * list 转换
     */
    public static <T, E> List<T> toList(List<E> list, ListItemConvert<E, T> converter) {
        if (list == null) {
            return new ArrayList<>();
        }
        return list.stream().map(converter::convert).collect(Collectors.toList());
    }
    
    /**
     * array 转 list
     * 不能传入基本类型数组, 否则整个数组会做为第一个元素
     */
    public static <T> List<T> toList(T[] array) {
        return array == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(array));
    }
    
    /**
     * list 转 array
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(List<T> list) {
        return list == null ? null : (T[]) list.toArray();
    }
    
    /**
     * list 排序
     *
     * id 升序 : id, true;
     * id 降序 : id, false;
     * 多重排序 : id, true, name, false
     */
    public static <T> List<T> sort(List<T> list, Object... sorts) {
        // --- 排序 ---
        List<Sort> sortList = getSort(sorts);
        return list.stream().sorted((o1, o2) -> {
                int cmp = 0;
                for (Sort sort : sortList) {
                    cmp = sort.compare(o1, o2);
                    if (cmp != 0) {
                        break;
                    }
                }
                return cmp;
            }
        ).collect(Collectors.toList());
    }
    
    /**
     * 分组
     */
    public static <T> List<List<T>> group(List<T> list, String... keys) {
        return group(list, item -> {
            StringBuilder keyBuilder = new StringBuilder();
            for (String s : keys) {
                keyBuilder.append(CommonUtil.getValue(item, s, String.class));
                keyBuilder.append(":");
            }
            return keyBuilder.toString();
        });
    }
    
    /**
     * 分组
     */
    public static <T> List<List<T>> group(List<T> list, ListGroupRouter<T> router) {
        HashMap<String, List<T>> map = new LinkedHashMap<>();
        for (T item : list) {
            String key = String.valueOf(router.getKey(item));
            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(item);
        }
        return ListUtil.toList(map);
    }
    
    /**
     * 分段
     */
    public static <T> List<List<T>> truncate(List<T> list, int size) {
        List<List<T>> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            newList.add(list.subList(i, Math.min(list.size(), i + size)));
        }
        return newList;
    }
    
    // ------------------------------ helper ------------------------------
    
    /**
     * 收集排序列表
     */
    protected static List<Sort> getSort(Object... sorts) {
        ArrayList<Sort> sortList = new ArrayList<>();
        for (int i = 0; i < sorts.length; i += 2) {
            Object obj = sorts[i];
            if (obj instanceof String) {
                Sort sort = new Sort();
                sort.setField((String) obj);
                if (i + 1 < sorts.length) {
                    sort.setAsc((Boolean) sorts[i + 1]);
                }
                sortList.add(sort);
            } else {
                throw new WebException("排序字段名不是 string");
            }
        }
        return sortList;
    }
    
    // ------------------------------ class ------------------------------
    
    @Data
    public static class Sort {
        
        String field;
        Boolean asc = false;
        
        public int compare(Object o1, Object o2) {
            long v1 = 0L;
            try {
                Object v = BeanUtil.getFieldValue(o1, field);
                if (v == null) {
                    v = 0L;
                }
                if (v instanceof Double) {
                    v1 = ((Double) v).longValue();
                } else {
                    v1 = Long.parseLong(String.valueOf(v));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            long v2 = 0L;
            try {
                Object v = BeanUtil.getFieldValue(o2, field);
                if (v == null) {
                    v = 0L;
                }
                if (v instanceof Double) {
                    v2 = ((Double) v).longValue();
                } else {
                    v2 = Long.parseLong(String.valueOf(v));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            int big = asc ? 1 : -1;
            int small = asc ? -1 : 1;
            return v1 == v2 ? 0 : (v1 > v2 ? big : small);
        }
    }
    
    public interface ListItemConvert<E, T> {
        
        T convert(E item);
    }
    
    public interface ListGroupRouter<T> {
        
        Object getKey(T item);
    }
}
