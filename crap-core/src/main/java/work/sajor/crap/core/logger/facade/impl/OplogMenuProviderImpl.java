package work.sajor.crap.core.logger.facade.impl;

import org.springframework.context.annotation.Primary;
import work.sajor.crap.core.dao.dao.RbacPrivilegeDao;
import work.sajor.crap.core.dao.entity.RbacPrivilege;
import work.sajor.crap.core.logger.facade.OplogMenuProvider;
import work.sajor.crap.core.mybatis.support.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Primary
public class OplogMenuProviderImpl implements OplogMenuProvider {
    
    @Autowired
    RbacPrivilegeDao rbacPrivilegeDao;
    
    @Override
    public MenuInfo get(String resource) {
        MenuInfo menuInfo = new MenuInfo();
        Wrapper<RbacPrivilege> wrapper = rbacPrivilegeDao.getWrapper();
        wrapper.eq(RbacPrivilege.Fields.resource, resource);
        RbacPrivilege menu = rbacPrivilegeDao.getOne(wrapper);
        if (menu != null) {
            menuInfo.setAction(menu.getName());
            
            if (menu.getPid() != 0) {
                Wrapper<RbacPrivilege> parentWrapper = rbacPrivilegeDao.getWrapper();
                parentWrapper.eq(RbacPrivilege.Fields.id, menu.getPid());
                RbacPrivilege parentMenu = rbacPrivilegeDao.getOne(parentWrapper);
                if (parentMenu != null) {
                    menuInfo.setType(parentMenu.getName());
                }
            }
        }
        return menuInfo;
    }
}
