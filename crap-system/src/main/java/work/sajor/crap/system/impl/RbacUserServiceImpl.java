package work.sajor.crap.system.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.sajor.crap.core.dao.dao.RbacUserDao;
import work.sajor.crap.core.dao.dao.RbacUserRoleDao;
import work.sajor.crap.core.dao.entity.RbacUser;
import work.sajor.crap.core.dao.entity.RbacUserRole;
import work.sajor.crap.core.mybatis.dao.BaseDao;
import work.sajor.crap.core.mybatis.facade.Entity;
import work.sajor.crap.core.mybatis.mapper.Mapper;
import work.sajor.crap.core.mybatis.support.Wrapper;
import work.sajor.crap.core.security.component.AccessControlHandler;
import work.sajor.crap.core.security.config.SecurityConfig;
import work.sajor.crap.core.security.dto.WebUser;
import work.sajor.crap.core.security.facade.WebUserService;
import work.sajor.crap.core.session.SessionUtil;
import work.sajor.crap.core.util.ListUtil;
import work.sajor.crap.core.web.WebException;
import work.sajor.crap.core.web.handler.FormRequestHandler;
import work.sajor.crap.system.dto.RbacUserSaveDTO;
import work.sajor.crap.system.facade.RbacUserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * Rbac 基础用户
 * 各子项目共享用户基础信息, 项目附加信息使用附表存储
 * </p>
 *
 * @author Sajor
 * @since 2023-02-07
 */
@Service
public class RbacUserServiceImpl implements RbacUserService {
    @Resource
    RbacUserDao userDao;


    @Resource
    RbacUserRoleDao userRoleDao;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    WebUserService webUserService;

    static Long devUserId = 1L;

    /**
     * 新增/更新用户
     */
    @Override
    @Transactional
    public <T extends Entity, M extends Mapper<T>> void save(RbacUserSaveDTO rbacUser, BaseDao<M, T> detailDao, T userDetail) {

        Long userId = rbacUser.getId();
        String password = rbacUser.getPassword();

        if (userId == null) {                                                   // 新增项目用户
            Wrapper<RbacUser> wrapper = userDao.getWrapper();
            wrapper.eq(RbacUser.Fields.username, rbacUser.getUsername());
            RbacUser dUser = userDao.getOne(wrapper);
            if (dUser != null) {
                rbacUser.setId(dUser.getId());                                  // 用户名已存在时更新数据
                Wrapper<T> existsWrapper = detailDao.getWrapper();
                existsWrapper.eq(RbacUser.Fields.id, dUser.getId());
                if (detailDao.getOne(existsWrapper) != null) {
                    throw new WebException("用户名已存在");                       // 附表中记录已存在, 给出提示
                }
            }
        } else {
            rbacUser.setUsername(null);                                         // 不能更新用户名
        }
        rbacUser.setPassword(null);                                             // 不能直接设置密码
        RbacUser user = BeanUtil.copyProperties(rbacUser, RbacUser.class);
        userDao.saveOrUpdate(user);                                         // 更新记录
        rbacUser.setId(user.getId());

        setPassword(rbacUser.getId(), password);                                // 更新密码

        userDetail.setId(rbacUser.getId());
        if (userId == null) {
            detailDao.save(userDetail);                                         // 附表新增
        } else {
            detailDao.updateById(userDetail);                                   // 附表更新
        }

        grant(rbacUser.getId(), rbacUser.getRoleIds());                         // 授权

        webUserService.reload(rbacUser.getId());
    }

    @Override
    public void setPassword(Long userId, String password) {
        if (StrUtil.isNotEmpty(password)) {
            RbacUser user = new RbacUser();
            user.setId(userId);
            user.setPassword(passwordEncoder.encode(password));
            userDao.updateById(user);
        }
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        WebUser principle = SessionUtil.getUser();
        if (!passwordEncoder.matches(oldPassword, principle.getPassword())) {
            throw new WebException("原密码错误");
        }
        setPassword(principle.getId(), newPassword);
    }

    @Override
    public boolean remove(Long userId, BaseDao detailDao) {
        if (userId.equals(devUserId)) {
            throw new WebException("不能删除开发者账号");
        }
        detailDao.removeById(userId);
        userDao.removeById(userId);
        webUserService.reload(userId);                                          // 清空用户缓存
        return true;
    }

    /**
     * 批量删除用户
     */
    @Override
    @Transactional
    public boolean remove(Long[] userIds, BaseDao detailDao) {
        if (userIds == null) {
            return true;
        }
        for (Long userId : userIds) {
            remove(userId, detailDao);
        }
        return true;
    }


    /**
     * 保存 用户->角色 关联
     */
    @Override
    public void grant(Long userId, List<Long> roleIds) {

        if (roleIds == null) {
            roleIds = Collections.emptyList();
        }

        Wrapper<RbacUserRole> wrapper = userRoleDao.getWrapper();
        wrapper.eq(RbacUserRole.Fields.userId, userId);
        List<RbacUserRole> roleList = userRoleDao.list(wrapper);
        userRoleDao.removeByIds(ListUtil.toList(roleList, RbacUserRole::getId)); // 删除已有关联

        ArrayList<RbacUserRole> rbacUserRoles = new ArrayList<>();
        roleIds.forEach(roleId -> {
            RbacUserRole rbacUserRole = new RbacUserRole();
            rbacUserRole.setUserId(userId);
            rbacUserRole.setRoleId(roleId);
            rbacUserRoles.add(rbacUserRole);
        });
        userRoleDao.saveBatch(rbacUserRoles);                                   // 新增关联关系

        RbacUser userUpdate = new RbacUser();                                   // 更新关联快照
        userUpdate.setId(userId);
        userUpdate.setRoleIds(roleIds);
        userDao.updateById(userUpdate);

        webUserService.reload(userId);                                          // 重置用户缓存
    }

}
