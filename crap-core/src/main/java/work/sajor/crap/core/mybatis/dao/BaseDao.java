package work.sajor.crap.core.mybatis.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.springframework.transaction.annotation.Transactional;
import work.sajor.crap.core.mybatis.facade.Entity;
import work.sajor.crap.core.mybatis.mapper.Mapper;
import work.sajor.crap.core.mybatis.support.CommonFields;
import work.sajor.crap.core.mybatis.support.EntityInfo;
import work.sajor.crap.core.mybatis.support.Wrapper;
import work.sajor.crap.core.mybatis.util.EntityDictUtil;
import work.sajor.crap.core.mybatis.util.EntityInfoUtil;
import work.sajor.crap.core.mybatis.util.SqlUtil;
import work.sajor.crap.core.session.SessionUtil;
import work.sajor.crap.core.web.WebException;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 扩展 service, 包装 mapper 作为 dao 层
 */
public class BaseDao<M extends Mapper<T>, T extends Entity> extends ServiceImpl<M, T> {
    
    /**
     * 实体类
     */
    private Class<T> entityClass;
    
    /**
     * 表信息
     */
    private EntityInfo<T> entityInfo;
    
    // ****************************** Helper ******************************
    
    /**
     * 获取 entity
     */
    @SneakyThrows
    public T getEntity() {
        return getEntityClass().newInstance();
    }
    
    /**
     * 获取 entity class
     */
    @SuppressWarnings("all")
    public Class<T> getEntityClass() {
        if (entityClass == null) {
            entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        }
        return entityClass;
    }
    
    /**
     * 获取表信息
     */
    public EntityInfo<T> getEntityInfo() {
        if (entityInfo == null) {
            entityInfo = EntityInfoUtil.getInfo(getEntityClass());
        }
        return entityInfo;
    }
    
    /**
     * 获取 wrapper
     */
    public Wrapper<T> getWrapper() {
        return getWrapper(true);
    }
    
    /**
     * 获取 wrapper
     */
    public Wrapper<T> getWrapper(boolean withTid) {
        Wrapper<T> wrapper = new Wrapper<>();
        if (withTid && getEntityInfo().getHasTid()) {
            wrapper.eq(CommonFields.TID, SessionUtil.getUserTid());
        }
        return wrapper;
    }
    
    // ****************************** override ******************************
    
    /**
     * 增加乐观锁校验
     */
    @Override
    public boolean updateById(T entity) {
        boolean status = super.updateById(entity);
        if (getEntityInfo().getHasVersion() && !status) {
            throw new WebException("数据已变更, 请重新提交");
        }
        return status;
    }
    
    /**
     * 根据ID 批量更新
     * 增加乐观锁校验
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchById(Collection<T> entityList) {
        if (getEntityInfo().getHasVersion()) {
            throw new WebException(getClass().getSimpleName() + " 包含乐观锁, 使用 updateById 代替");
        }
        return super.updateBatchById(entityList);
    }
    
    // ****************************** extend ******************************
    
    @PostConstruct
    protected void setFieldDict() {
        EntityDictUtil.registerDict(getEntityClass());
    }
    
    /**
     * 记录是否存在
     */
    public boolean exists(Wrapper<T> wrapper) {
        return getOne(wrapper) != null;
    }
    
    /**
     * 附加 limit 1 限制只返回一条记录
     */
    public T getOne(Wrapper<T> wrapper) {
        wrapper.last("LIMIT 1");
        return super.getOne(wrapper);
    }
    
    // ****************************** 动态 wrapper 查询 ******************************
    
    /**
     * 自定义字段查询
     * 使用 wrapper.setFields() 设置查询字段
     * 使用 wrapper.addFields() 自动设置连表字段
     * 使用 wrapper.setJoin() 设置连表条件
     */
    public List<Map<String, Object>> listMap(Wrapper<T> wrapper) {
        return getBaseMapper().selectMapList(wrapper);
    }
    
    
    /**
     * 自定义字段查询
     * 使用 wrapper.setFields() 设置查询字段
     * 使用 wrapper.addFields() 自动设置连表字段
     * 使用 wrapper.setJoin() 设置连表条件
     */
    public <E extends IPage<T>> IPage<Map<String, Object>> pageMap(Wrapper<T> wrapper, E pages) { return getBaseMapper().selectMapPage(wrapper, pages);}
    
    // ****************************** 动态 sql 查询 ******************************
    
    /**
     * sql 模板查询
     * SELECT ... FROM ... JOIN ... WHERE ... GROUP BY ... HAVING ... ORDER BY ... LIMIT ...
     */
    public List<Map<String, Object>> listSql(String sql, Wrapper<T> wrapper) {
        wrapper = wrapper == null ? getWrapper() : wrapper;
        return getBaseMapper().selectSqlList(SqlUtil.fillWrapper(sql, wrapper, false));
    }
    
    /**
     * sql 模板查询
     * SELECT ... FROM ... JOIN ... WHERE ... GROUP BY ... HAVING ... ORDER BY ... LIMIT ...
     */
    public Page<Map<String, Object>> pageSql(String sql, Wrapper<T> wrapper, IPage pages) {
        
        // --- 合计 ---
        wrapper = wrapper == null ? getWrapper() : wrapper;
        List<Map<String, Object>> countRes = getBaseMapper().selectSqlList(SqlUtil.fillWrapper(sql, wrapper, true));
        long total = Long.parseLong(String.valueOf(countRes.get(0).get("total")));
        
        // --- 分页 ---
        long size = pages.getSize();
        long current = pages.getCurrent();
        sql += " LIMIT " + ((current - 1) * size) + "," + size;
        
        Page<Map<String, Object>> page = new Page<>();
        page.setRecords(listSql(sql, wrapper));
        page.setTotal(total);
        page.setSize(size);
        page.setCurrent(current);
        return page;
    }
}
