package work.sajor.crap.system.facade;

import work.sajor.crap.core.mybatis.dao.BaseDao;
import work.sajor.crap.core.mybatis.facade.Entity;
import work.sajor.crap.core.mybatis.mapper.Mapper;
import work.sajor.crap.system.dto.RbacUserSaveDTO;

import java.util.List;

/**
 * <p>
 * Rbac 基础用户
 * 各子项目共享用户基础信息, 项目附加信息使用附表存储
 * </p>
 *
 * @author Sajor
 * @since 2023-02-07
 */
public interface RbacUserService {

    /**
     * 新增/更新用户
     * 支持使用自定义detail拓展表
     */
    <T extends Entity, M extends Mapper<T>> void save(RbacUserSaveDTO rbacUser, BaseDao<M, T> detailDao, T detail);

    /**
     * 设置密码
     */
    void setPassword(Long userId, String password);

    /**
     * 变更密码 (当前用户)
     */
    void updatePassword(String oldPassword, String newPassword);

    /**
     * 删除用户
     */
    boolean remove(Long userId, BaseDao detailDao);

    /**
     * 批量删除用户
     */
    boolean remove(Long[] userIds, BaseDao detailDao);

    /**
     * 保存 用户->角色 关联
     */
    void grant(Long userId, List<Long> roleIds);
}
