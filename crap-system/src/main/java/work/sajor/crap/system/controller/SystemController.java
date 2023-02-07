package work.sajor.crap.system.controller;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.sajor.crap.core.dao.dao.AdminDetailDao;
import work.sajor.crap.core.security.dto.WebUser;
import work.sajor.crap.core.session.SessionUtil;
import work.sajor.crap.core.web.dto.FormRequest;
import work.sajor.crap.core.web.handler.FormRequestHandler;
import work.sajor.crap.core.web.handler.TableRequestHandler;
import work.sajor.crap.core.web.response.ResponseBuilder;
import work.sajor.crap.system.dto.RbacUserSaveDTO;
import work.sajor.crap.system.dto.SystemInitDTO;
import work.sajor.crap.system.dto.SystemPasswordDTO;
import work.sajor.crap.system.facade.MenuService;
import work.sajor.crap.system.facade.RbacUserService;

import javax.annotation.Resource;

/**
 * <p>
 * 系统控制器
 * 基础公共入口
 * </p>
 *
 * @author Sajor
 * @since 2023-02-07
 */
@RestController
@RequestMapping("/{route}/crap/system")
public class SystemController {

    @Resource
    TableRequestHandler table;
    @Resource
    FormRequestHandler form;
    @Resource
    MenuService menuService;
    @Resource
    RbacUserService rbacUserService;

    @Resource
    AdminDetailDao adminDetailDao;


    /**
     * 获取初始化信息
     * 当前用户, 菜单, 权限等
     */
    @PostMapping("/init")
    public Object systemInit() {
        SystemInitDTO systemInit = new SystemInitDTO();
        WebUser webUser = SessionUtil.getUser();
        systemInit.setMenus(menuService.getMenuList(webUser));
        systemInit.setUser(BeanUtil.copyProperties(webUser, SystemInitDTO.User.class));
        return ResponseBuilder.success(systemInit);
    }

    @PostMapping("/create-user")
    public Object add(@RequestBody FormRequest formRequest) {
        rbacUserService.save(
                form.getEntity(RbacUserSaveDTO.class, formRequest, null),
                adminDetailDao,
                form.getEntity(adminDetailDao.getEntityClass(), formRequest, null));
        return ResponseBuilder.success();
    }


    /**
     * 修改当前用户密码
     */
    @PostMapping("password")
    @Transactional
    public Object password(@RequestBody FormRequest formRequest) {
        SystemPasswordDTO entity = form.getEntity(SystemPasswordDTO.class, formRequest);
        rbacUserService.updatePassword(entity.getPassword(), entity.getNewPassword());
        return ResponseBuilder.success();
    }
}
