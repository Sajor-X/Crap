package work.sajor.crap.core.mybatis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import work.sajor.crap.core.mybatis.facade.Entity;
import work.sajor.crap.core.mybatis.mapper.Mapper;
import work.sajor.crap.core.web.WebException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 提供数据变更事件监听
 *
 * 所有回调在当前会话内执行, 这个时候不能做事务数据更新之外的动作, 比如调用接口.
 * 对于需要在事务提交后执行的回调, 需要使用 SpringUtil.runAfterTransaction() 包装
 * </p>
 *
 * @author Sajor
 * @since 2022-11-14
 */
public class EventDao<M extends Mapper<T>, T extends Entity> extends BaseDao<M, T> {
    
    /**
     * 代理实例, 通过代理调用回调方法
     */
    @Lazy
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    protected EventDao<M, T> self;
    
    // ------------------------------ 回调 ------------------------------
    
    /**
     * 新增完成
     *
     * 等待事务完成 SpringUtil.runAfterTransaction()
     */
    public void onInsert(Long id) {}
    
    /**
     * 更新完成
     *
     * 等待事务完成 SpringUtil.runAfterTransaction()
     */
    public void onUpdate(Long id) {}
    
    /**
     * 删除完成
     *
     * 等待事务完成 SpringUtil.runAfterTransaction()
     */
    public void onDelete(Long id) {}
    
    /**
     * 变更完成
     *
     * 等待事务完成 SpringUtil.runAfterTransaction()
     */
    public void onChange(Long id) {}
    
    // ------------------------------ 重写数据变更方法 ------------------------------
    
    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        boolean status = super.saveBatch(entityList, batchSize);
        if (status) {
            innerInsert(getIds(entityList));
        }
        return status;
    }
    
    @Override
    public boolean saveOrUpdate(T entity) {
        boolean isSave = entity.getId() == null;
        boolean status = super.saveOrUpdate(entity);
        if (status) {
            if (isSave) {
                innerInsert(entity.getId());
            } else {
                innerUpdate(entity.getId());
            }
        }
        return status;
    }
    
    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        ArrayList<Long> save = new ArrayList<>();
        ArrayList<Long> update = new ArrayList<>();
        entityList.forEach(entity -> {
            if (entity.getId() != null) {
                update.add(entity.getId());
            }
        });
        boolean status = super.saveOrUpdateBatch(entityList, batchSize);
        entityList.forEach(entity -> {
            if (update.indexOf(entity.getId()) == -1) {
                save.add(entity.getId());
            }
        });
        if (status) {
            innerInsert(save);
            innerUpdate(update);
        }
        return status;
    }
    
    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        boolean status = super.updateBatchById(entityList, batchSize);
        if (status) {
            innerUpdate(getIds(entityList));
        }
        return status;
    }
    
    /**
     * 插入一条记录（选择字段，策略插入）
     */
    @Override
    public boolean save(T entity) {
        boolean status = super.save(entity);
        if (status) {
            innerInsert(entity.getId());
        }
        return status;
    }
    
    /**
     * 根据 ID 删除
     */
    @Override
    public boolean removeById(Serializable id) {
        boolean status = super.removeById(id);
        if (status) {
            innerDelete((Long) id);
        }
        return status;
    }
    
    /**
     * 根据 entity 条件，删除记录
     */
    @Override
    @Deprecated
    public boolean remove(com.baomidou.mybatisplus.core.conditions.Wrapper<T> queryWrapper) {
        throw new WebException("Event Dao 不支持条件删除");
    }
    
    /**
     * 删除（根据ID 批量删除）
     */
    @Override
    public boolean removeByIds(Collection<?> idList) {
        boolean status = super.removeByIds(idList);
        if (status) {
            ArrayList<Long> ids = new ArrayList<>();
            idList.forEach(id -> ids.add((Long) id));
            innerDelete(ids);
        }
        return status;
    }
    
    /**
     * 根据 ID 选择修改
     */
    @Override
    public boolean updateById(T entity) {
        boolean status = super.updateById(entity);
        if (status) {
            innerUpdate(entity.getId());
        }
        return status;
    }
    
    /**
     * 根据 UpdateWrapper 条件，更新记录 需要设置sqlset
     */
    @Override
    @Deprecated
    public boolean update(com.baomidou.mybatisplus.core.conditions.Wrapper<T> updateWrapper) {
        throw new WebException("Event Dao 不支持条件更新");
    }
    
    /**
     * 根据 whereEntity 条件，更新记录
     */
    @Override
    @Deprecated
    public boolean update(T entity, com.baomidou.mybatisplus.core.conditions.Wrapper<T> updateWrapper) {
        throw new WebException("Event Dao 不支持条件更新");
    }
    
    /**
     * 根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)方法
     * 此次修改主要是减少了此项业务代码的代码量（存在性验证之后的saveOrUpdate操作）
     */
    @Override
    @Deprecated
    public boolean saveOrUpdate(T entity, com.baomidou.mybatisplus.core.conditions.Wrapper<T> updateWrapper) {
        throw new WebException("Event Dao 不支持条件更新");
    }
    
    // ------------------------------ helper ------------------------------
    
    /**
     * 新增完成
     */
    private void innerInsert(List<Long> ids) { ids.forEach(this::innerInsert);}
    
    /**
     * 更新完成
     */
    private void innerUpdate(List<Long> ids) { ids.forEach(this::innerUpdate);}
    
    /**
     * 删除完成
     */
    private void innerDelete(List<Long> ids) { ids.forEach(this::innerDelete);}
    
    /**
     * 新增完成
     */
    private void innerInsert(Long id) {
        self.onInsert(id);
        self.onChange(id);
    }
    
    /**
     * 更新完成
     */
    private void innerUpdate(Long id) {
        self.onUpdate(id);
        self.onChange(id);
    }
    
    /**
     * 删除完成
     */
    private void innerDelete(Long id) {
        self.onDelete(id);
        self.onChange(id);
    }
    
    List<Long> getIds(Collection<T> entityList) {
        ArrayList<Long> ids = new ArrayList<>();
        entityList.forEach(t -> ids.add(t.getId()));
        return ids;
    }
}
