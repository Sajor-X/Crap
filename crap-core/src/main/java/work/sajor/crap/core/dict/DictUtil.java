package work.sajor.crap.core.dict;

import work.sajor.crap.core.dict.DictLoader.DictDataProvider;
import work.sajor.crap.core.mybatis.support.Wrapper;
import work.sajor.crap.core.web.WebException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字典工具
 *
 * 提前注册好字典数据的提取方式,
 * 规范和简化前端,导入,导出等场景的字典使用
 * 完成构造字典数据,转换翻译,填充字典快照字段等操作
 */
public class DictUtil {
    
    public static Map<Object, DictLoader> registry = new ConcurrentHashMap<>();
    
    /**
     * 注册字典加载器
     */
    public static void register(Object key, DictDataProvider dataProvider) {
        registry.put(key, new DictLoader(dataProvider));
    }
    
    /**
     * 注册字典加载器
     */
    public static void register(Object key, DictLoader dictLoader) {
        registry.put(key, dictLoader);
    }
    
    /**
     * 加载字典
     */
    public static Dict load(String key) {
        return load(key, null);
    }
    
    /**
     * 加载字典
     */
    public static Dict load(Object key, Wrapper wrapper) {
        if (registry.get(key) == null) {
            throw new WebException("字典不存在 : " + key);
        }
        return registry.get(key).load(wrapper);
    }
}
