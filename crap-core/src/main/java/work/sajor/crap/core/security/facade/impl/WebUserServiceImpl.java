package work.sajor.crap.core.security.facade.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import work.sajor.crap.core.dao.dao.RbacUserDao;
import work.sajor.crap.core.dao.entity.RbacUser;
import work.sajor.crap.core.dao.entity.base.RbacUserBase;
import work.sajor.crap.core.mybatis.support.Wrapper;
import work.sajor.crap.core.mybatis.util.QueryUtil;
import work.sajor.crap.core.security.config.SecurityConfig;
import work.sajor.crap.core.security.dto.WebUser;
import work.sajor.crap.core.security.facade.ProjectAccessControl;
import work.sajor.crap.core.security.facade.WebUserService;
import work.sajor.crap.core.util.LocalDateUtil;
import work.sajor.crap.core.web.WebException;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户服务
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "WebUser")
public class WebUserServiceImpl implements WebUserService {

    @Autowired
    RbacUserDao userDao;

    @Autowired
    SecurityConfig securityConfig;

    /**
     * 可以注册多个权限验证回调, 组合权限 ID 列表
     */
    @Autowired
    private ProjectAccessControl[] accessHandler;

    /**
     * 获取 WebUser 对象, 不含权限, 用于验证账号/密码
     */
    @Override
    public WebUser loadUserByUsername(String username) throws UsernameNotFoundException {
        Wrapper<RbacUser> wrapper = userDao.getWrapper();
        wrapper.eq(RbacUser.Fields.username, username);
        RbacUser user = userDao.getOne(wrapper);
        if (user == null || !user.getUsername().equals(username)) {
            throw new UsernameNotFoundException("username : " + username + " not exists");
        }
        return BeanUtil.copyProperties(user, WebUser.class);
    }

    /**
     * 获取用户
     */
    @Override
    @Cacheable
    public WebUser get(Long id) {
        RbacUser user = userDao.getById(id);
        if (user == null) {
            throw new WebException("用户不存在");
        }
        if (user.getStatus().equals(0)) {
            throw new WebException("账号已禁用");
        }

        ArrayList<Long> privilegeIds = new ArrayList<>();                       // 菜单权限列表
        List<String> handler = securityConfig.getHandler();
        for (ProjectAccessControl projectAccessControl : accessHandler) {
            privilegeIds.addAll(projectAccessControl.getPrivilegeIds(user.getId()));
        }

        WebUser webUser = BeanUtil.copyProperties(user, WebUser.class);
        webUser.setDevUser(user.getUsername().equals(securityConfig.getDevUser()));
        webUser.setPrivilegeIds(privilegeIds);
        return webUser;
    }

    /**
     * 刷新用户缓存
     */
    @Override
    @CacheEvict
    public void reload(Long id) {}

    /**
     * 清空用户缓存
     */
    @Override
    @CacheEvict(allEntries = true)
    public void reload() {}

    /**
     * 更新最近操作时间
     */
    @Override
    public void updateUserLastOpTime(Long id) {
        String updateSql = "UPDATE {} set {}='{}' WHERE id={}";
        QueryUtil.execute(String.format(updateSql, RbacUser.Table, RbacUser.Fields.lastOpTime, LocalDateUtil.toString(true), id));
    }
}
