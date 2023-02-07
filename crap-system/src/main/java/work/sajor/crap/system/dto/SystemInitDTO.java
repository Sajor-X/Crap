package work.sajor.crap.system.dto;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import work.sajor.crap.core.dao.entity.RbacPrivilege;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * web 初始化数据
 * </p>
 *
 * @author Sajor
 * @since 2023-02-07
 */
@Data
public class SystemInitDTO {

    List<WebMenu> menus = new ArrayList<>();

    User user;

    public void setMenus(List<RbacPrivilege> list) {
        for (RbacPrivilege rbacPrivilege : list) {
            menus.add(BeanUtil.copyProperties(rbacPrivilege, WebMenu.class));
        }
    }

    @Data
    public static class User {

        String id;

        // 姓名
        String name;

        // 账号
        String username;
    }

    @Data
    public static class WebMenu {

        @JsonProperty("id")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        protected Long id;

        @JsonProperty("pid")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        protected Long pid;

        @JsonProperty("name")
        protected String name;

        @JsonProperty("resource")
        protected String resource;

        @JsonProperty("icon")
        protected String icon;

        @JsonProperty("show_flag")
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        protected Boolean showFlag;

        @JsonProperty("project")
        protected String project;

        @JsonProperty("tpl")
        protected String tpl;

        @JsonProperty("scope")
        protected String scope;

        @JsonProperty("update_time")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        protected LocalDateTime updateTime;
    }
}
