package work.sajor.crap.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.sajor.crap.core.dao.dao.RbacPrivilegeDao;
import work.sajor.crap.core.dao.entity.RbacPrivilege;
import work.sajor.crap.core.mybatis.util.EntityConvertUtil;
import work.sajor.crap.core.util.ListUtil;
import work.sajor.crap.core.web.WebController;
import work.sajor.crap.core.web.WebDict;
import work.sajor.crap.core.web.dto.FormRequest;
import work.sajor.crap.core.web.dto.FormResponse;
import work.sajor.crap.system.dto.MenuCreateDTO;
import work.sajor.crap.system.facade.MenuService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单控制器
 * </p>
 *
 * @author Sajor
 * @since 2023-02-05
 */
@RestController
@RequestMapping("/{route}/crap/menu")
public class MenuController extends WebController<RbacPrivilegeDao> {

    @Resource
    MenuService menuService;

    @Resource
    RbacPrivilegeDao privilegeDao;


    /**
     * 新增(提交)
     */
    @PostMapping("/add")
    @Transactional
    public Object addPost(@RequestBody FormRequest formRequest) {
        RbacPrivilege rbacPrivilege = menuService.create(formRequestHandler.getEntity(MenuCreateDTO.class, formRequest));
        return new FormResponse(rbacPrivilege);
    }

    /**
     * 排序
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/list/sort")
    public Object listSort(@RequestBody FormRequest formRequest) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) formRequest.getData().get("list");
        List<RbacPrivilege> rbacPrivilegeList = ListUtil.toList(list, item -> EntityConvertUtil.convert(RbacPrivilege.class, item));
        privilegeDao.updateBatchById(rbacPrivilegeList);
        return new FormResponse();
    }

    /**
     * 字典
     */
    @Override
    protected WebDict getDict() {
        WebDict dict = super.getDict();
        dict.put("pid", "menu");
        return dict;
    }
}
