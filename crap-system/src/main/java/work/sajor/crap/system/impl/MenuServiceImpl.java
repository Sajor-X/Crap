package work.sajor.crap.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.sajor.crap.core.dao.dao.RbacPrivilegeDao;
import work.sajor.crap.core.dao.entity.RbacPrivilege;
import work.sajor.crap.core.mybatis.util.EntityConvertUtil;
import work.sajor.crap.core.security.dto.WebUser;
import work.sajor.crap.core.web.WebException;
import work.sajor.crap.system.dto.MenuCreateDTO;
import work.sajor.crap.system.facade.MenuService;
import work.sajor.crap.system.facade.RbacPrivilegeService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * <p>
 * 菜单服务
 * </p>
 *
 * @author Sajor
 * @since 2023-02-05
 */
@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    RbacPrivilegeDao privilegeDao;

    @Resource
    RbacPrivilegeService privilegeService;

    /**
     * 获取用户菜单列表
     */
    @Override
    public List<RbacPrivilege> getMenuList(WebUser webUser) {
        List<RbacPrivilege> menuList = privilegeDao.getMenuList();
        if (webUser.isDevUser()) {
            return menuList;
        }

        // TODO 可优化 使用filter
        ArrayList<RbacPrivilege> userMenuList = new ArrayList<>();
        for (RbacPrivilege menu : menuList) {
            if (menu.getResource().equals("") || webUser.getPrivilegeIds().contains(menu.getId())) {
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    /**
     * 创建菜单
     */
    @Override
    public RbacPrivilege create(MenuCreateDTO menuCreate) {
        RbacPrivilege menu = EntityConvertUtil.convert(RbacPrivilege.class, menuCreate);
        if (!privilegeDao.saveOrUpdate(menu)) {
            throw new WebException("创建菜单失败");
        }

        // 批量创建子菜单
        if (menuCreate.getChild() != null) {

            // 支持的子菜单类型
            HashMap<String, String> types = new HashMap<String, String>() {
                {
                    this.put("list", "列表");
                    this.put("add", "新增");
                    this.put("edit", "编辑");
                    this.put("view", "查看");
                    this.put("del", "删除");
                    this.put("import", "导入");
                    this.put("export", "导出");
                }
            };

            for (String type : menuCreate.getChild()) {
                if (types.get(type) == null) {
                    continue;
                }

                RbacPrivilege subMenu = EntityConvertUtil.convert(RbacPrivilege.class, menuCreate);
                subMenu.setPid(menu.getId());
                subMenu.setName(types.get(type));
                subMenu.setIcon("");
                subMenu.setShowFlag(false);

                // 计算 url
                int i = subMenu.getResource().lastIndexOf("/");
                if (i != -1) {
                    subMenu.setResource(subMenu.getResource().substring(0, i) + "/" + type);
                } else {
                    subMenu.setResource("/" + type);
                }
                privilegeDao.saveOrUpdate(subMenu);
            }
        }
        return menu;
    }
}
