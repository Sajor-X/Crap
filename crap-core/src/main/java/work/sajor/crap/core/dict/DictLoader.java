package work.sajor.crap.core.dict;

import work.sajor.crap.core.mybatis.support.Wrapper;

/**
 * 字典加载器
 */
public class DictLoader {
    
    private DictDataProvider dataProvider;
    
    public DictLoader() {}
    
    /**
     * 常规字典在构造时传入数据源就够了
     */
    public DictLoader(DictDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }
    
    /**
     * 加载常规字典时使用
     */
    public Dict load() {return load(null);}
    
    /**
     * 需要在字典里添加筛选时使用
     *
     * 需要修饰返回的字典对象时覆写
     */
    public Dict load(Wrapper wrapper) {
        return new Dict(dataProvider.fetch(wrapper));
    }
    
    // ------------------------------ inner class ------------------------------
    
    /**
     * 提供字典数据
     */
    public interface DictDataProvider {
        
        Object fetch(Wrapper wrapper);
    }
    
    /**
     * 逐条获取字典, 先检查缓存, 不存在时提取
     * 用于批量处理, 先不做
     */
    // public interface CacheDataProvider {
    //
    //     Object fetch(Object key);
    // }
}
