package work.sajor.crap.core.security.jwt;

import cn.hutool.core.convert.Convert;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import work.sajor.crap.core.dao.dao.RbacTokenDao;
import work.sajor.crap.core.dao.dao.RbacUserDao;
import work.sajor.crap.core.dao.entity.RbacToken;
import work.sajor.crap.core.dao.entity.RbacUser;
import work.sajor.crap.core.mybatis.support.Wrapper;
import work.sajor.crap.core.security.config.JwtConfig;
import work.sajor.crap.core.util.ListUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * token 存储
 *
 * todo : 写入 db 时同步写入 redis, redis 查询不到时(重启或过期), 从 db 中读取
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Component
@Slf4j
public class JwtStorage {

    @Autowired
    RbacUserDao userDao;

    @Autowired
    RbacTokenDao rbacTokenDao;

    @Autowired
    JwtConfig jwtConfig;

    /**
     * 保存 token
     *
     * @param userId
     * @param token
     * @param ttl
     */
    public void save(Long userId, String token, Integer ttl, String from) {
        Wrapper<RbacToken> wrapper = rbacTokenDao.getWrapper();
        wrapper.eq(RbacToken.Fields.userId, userId);
        wrapper.eq(RbacToken.Fields.type, from);
        RbacToken userToken = rbacTokenDao.getOne(wrapper);
        if (userToken == null) {
            userToken = new RbacToken();
            userToken.setUserId(userId);
            userToken.setType(from);
        }

        userToken.setToken(token);
        userToken.setTokenTime(Convert.toLocalDateTime(new Date()));
        userToken.setTokenTtl(ttl);
        rbacTokenDao.saveOrUpdate(userToken);
    }

    /**
     * 检查 token 是否有效
     *
     * @param userId
     * @param token
     */
    public void check(Long userId, String token, String from) {

        Wrapper<RbacToken> wrapper = rbacTokenDao.getWrapper();
        wrapper.eq(RbacToken.Fields.userId, userId);
        wrapper.eq(RbacToken.Fields.type, from);
        RbacToken userToken = rbacTokenDao.getOne(wrapper);

//        RbacUser user = userDao.getById(userId);
        if (userToken == null) {
            log.warn("token 无效");
            throw new JwtException("登录状态无效, 请重新登录");
        }

        if (!userToken.getToken().equals(token)) {
            log.warn("token 无效 : {}!={} 已变更", token, userToken.getToken());
            throw new JwtException("当前账号已在其他设备登录, 请重新登录");
        }

        LocalDateTime expireTime = userToken.getTokenTime().plusSeconds(userToken.getTokenTtl());
        if (expireTime.isBefore(LocalDateTime.now())) {
            log.warn("token 无效 : 已过期 {}", expireTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            throw new JwtException("登录状态已过期, 请重新登录");
        }

        // 10 分钟刷新一次
        LocalDateTime refreshTime = userToken.getTokenTime().plusSeconds(jwtConfig.getRefresh());
        if (refreshTime.isBefore(LocalDateTime.now())) {
            refresh(userToken.getId(), from);
        }
    }

    /**
     * 清空 token
     */
    public void remove(Long userId, String from) {
        Wrapper<RbacToken> wrapper = rbacTokenDao.getWrapper();
        wrapper.eq(RbacToken.Fields.userId, userId);
        wrapper.eq(RbacToken.Fields.type, from);
        List<Map<String, Object>> list = rbacTokenDao.listMap(wrapper);
        rbacTokenDao.removeByIds(ListUtil.toList(list, "id", Long.class));
    }

    /**
     * 刷新 token
     */
    private void refresh(Long userId, String from) {
        Wrapper<RbacToken> wrapper = rbacTokenDao.getWrapper();
        wrapper.eq(RbacToken.Fields.userId, userId);
        wrapper.eq(RbacToken.Fields.type, from);
        RbacToken userToken = rbacTokenDao.getOne(wrapper);
        if (userToken != null) {
            userToken.setTokenTime(LocalDateTime.now());
            rbacTokenDao.updateById(userToken);
        }

    }

    /**
     * 复用 token
     * 生产环境不会进入这个方法, 先简单处理
     */
    public String reuse(Long userId, String from) {
        try {
            Wrapper<RbacToken> wrapper = rbacTokenDao.getWrapper();
            wrapper.eq(RbacToken.Fields.userId, userId);
            wrapper.eq(RbacToken.Fields.type, from);
            RbacToken userToken = rbacTokenDao.getOne(wrapper);

            if (userToken == null
                    || userToken.getTokenTime().plusSeconds(userToken.getTokenTtl()).isBefore(LocalDateTime.now())) {
                return null;
            }
            refresh(userId, from);
            return userToken.getToken();
        } catch (Exception ignore) {
            return null;
        }
    }

}
