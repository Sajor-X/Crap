package work.sajor.crap.core.mybatis.util;

import work.sajor.crap.core.mybatis.facade.Entity;
import work.sajor.crap.core.mybatis.support.EntityInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理 dao.listMap 连表查询结果
 */
public class JoinResultUtil {
    
    /**
     * 从提取 dao.listMap 的结果中提取连表查询实体
     */
    public <T extends Entity> T get(Map<String, Object> map, Class<T> entityClazz) {
        EntityInfo<T> info = EntityInfoUtil.getInfo(entityClazz);
        return EntityConvertUtil.convert(entityClazz, map.get(info.getTable()));
    }
    
    /**
     * 将 dao.listMap 的查询结果扁平化, 使用第一个表的 id 作为 id
     * {table1:{id:1}, table2:{id:2}} => {id:1, table1.id:1, table2.id:2}
     */
    public List<Map<String, Object>> flatten(List<Map<String, Object>> listMap) {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        listMap.forEach(map -> list.add(flatten(map)));
        return list;
    }
    
    /**
     * 将 dao.listMap 的查询结果扁平化, 使用第一个表的 id 作为 id
     * {table1:{id:1}, table2:{id:2}} => {id:1, table1.id:1, table2.id:2}
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> flatten(Map<String, Object> map) {
        HashMap<String, Object> jsonMap = new HashMap<>();
        final boolean[] id = {false};
        
        map.forEach((table, valueMap) -> {
            
            EntityInfo entityInfo = EntityInfoUtil.getInfo(table);
            
            ((Map) valueMap).forEach((field, value) -> {
                
                if (!id[0]) {
                    id[0] = true;
                    jsonMap.put("id", ((Map) valueMap).get("id"));          // 从第一张表中取出 id
                }
                
                if (value instanceof Boolean) {                             // bool to int
                    jsonMap.put(table + "." + field, (Boolean) value ? 1 : 0);
                } else {
                    jsonMap.put(table + "." + field, value);
                }
            });
        });
        
        return jsonMap;
    }
}
