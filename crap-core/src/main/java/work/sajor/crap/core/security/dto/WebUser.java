package work.sajor.crap.core.security.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import work.sajor.crap.core.dao.entity.RbacUser;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 系统用户
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
@Data
public class WebUser extends RbacUser implements UserDetails {

    /**
     * 是否是开发者账号
     */
    private boolean devUser = false;

    /**
     * 权限 ID 列表
     */
    private List<Long> privilegeIds = Collections.emptyList();

    private String token;

    private Integer tid = 0;

    // ------------------------------ implements ------------------------------

    /**
     * 角色列表
     * 暂未使用
     */
    private List<WebGrantedAuthority> authorities = Collections.emptyList();

    /**
     * 未过期
     */
    boolean accountNonExpired = true;

    /**
     * 未锁定
     */
    boolean accountNonLocked = true;

    /**
     * 有效
     */
    boolean enabled = true;

    /**
     * 密码未过期
     */
    boolean credentialsNonExpired = true;

    @Data
    public static class WebGrantedAuthority implements GrantedAuthority {

        private String authority;
    }
}
