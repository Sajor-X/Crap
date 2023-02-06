package work.sajor.crap.system.controller;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import work.sajor.crap.core.dao.dao.RbacRoleDao;
import work.sajor.crap.core.dao.entity.RbacRole;
import work.sajor.crap.core.mybatis.util.EntityDictUtil;
import work.sajor.crap.core.security.util.UriUtil;
import work.sajor.crap.core.web.WebController;
import work.sajor.crap.core.web.WebDict;
import work.sajor.crap.core.web.dto.FormRequest;
import work.sajor.crap.core.web.dto.FormResponse;
import work.sajor.crap.core.web.dto.TableRequest;
import work.sajor.crap.core.web.dto.TableResponse;
import work.sajor.crap.system.dto.RbacRoleGrantDTO;
import work.sajor.crap.system.facade.RbacPrivilegeService;
import work.sajor.crap.system.facade.RbacRoleService;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 角色控制器
 * </p>
 *
 * @author Sajor
 * @since 2023-02-06
 */
@RestController
@RequestMapping("/{route}/crap/role")
public class RoleController extends WebController<RbacRoleDao> {

    @Resource
    RbacRoleService rbacRoleService;

    @Resource
    RbacPrivilegeService rbacPrivilegeService;


    /**
     * 列表页数据
     */
    @PostMapping("/list")
    public Object list(@RequestBody(required = false) TableRequest tableRequest) {
        // 只显示当前项目的角色
        tableRequest.getWhere().add(RbacRole.Fields.project, UriUtil.getUriResource().getModule());
        return new TableResponse(tableRequestHandler.getData(getDao(), tableRequest))
                .setDict(EntityDictUtil.getDict(getDao().getEntity()));
    }

    /**
     * 新增(提交)
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/add")
    @Transactional
    public Object addPost(@RequestBody FormRequest formRequest) {

        // 补全项目
        Map data = formRequest.getData();
        data.put(RbacRole.Fields.project, UriUtil.getUriResource().getModule());

        return super.addPost(formRequest);
    }


    /**
     * 编辑(预备数据)
     */
    @GetMapping("/edit/{id}")
    public Object editGet(@PathVariable("id") Long id) {
        Object entity = getDao().getById(id);                                   // 实体数据
        RbacRoleGrantDTO rbacRoleGrant = BeanUtil.copyProperties(entity, RbacRoleGrantDTO.class);
        rbacRoleGrant.setPrivilegeIds(rbacPrivilegeService.getRolePrivilegeIds(id));

        FormResponse formResponse = new FormResponse();                         // 响应结构
        formResponse.setData(rbacRoleGrant);                                    // 设置数据
        formResponse.setDict(getDict());                                        // 设置字典
        return formResponse;
    }

    /**
     * 编辑(提交)
     */
    @PostMapping("/edit/{id}")
    @Transactional
    public Object editPost(@PathVariable("id") Long id, @RequestBody FormRequest formRequest) {
        RbacRoleGrantDTO roleGrant = formRequestHandler.getEntity(RbacRoleGrantDTO.class, formRequest);
        rbacRoleService.grant(id, roleGrant.getPrivilegeIds());
        return super.editPost(id, formRequest);
    }


    /**
     * 删除
     */
    @PostMapping("/del/{id}")
    @Transactional
    public Object delPost(@PathVariable("id") Long id) {
        return rbacRoleService.remove(id);
    }

    /**
     * 批量删除
     */
    @PostMapping("/del")
    @Transactional
    public Object delPost(@RequestBody Long[] id) {
        return rbacRoleService.remove(id);
    }

    /**
     * 提取枚举字典
     */
    @Override
    protected WebDict getDict() {
        WebDict dict = super.getDict();
        dict.put("privilege_ids", "privilege");
        return dict;
    }

}
