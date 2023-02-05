package work.sajor.crap.server.core.dao.dao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import work.sajor.crap.core.dao.dao.RbacTokenDao;
import work.sajor.crap.core.dao.dao.RbacUserDao;
import work.sajor.crap.core.dao.entity.RbacUser;

import javax.annotation.Resource;

@SpringBootTest
class RbacUserDaoTest {

    @Resource
    RbacUserDao userDao;

    @Resource
    RbacTokenDao tokenDao;

    @Test
    public void testUserList() {
        System.out.println(tokenDao.list());
        for (RbacUser rbacUser : userDao.list()) {
            System.out.println(rbacUser.getName());
            System.out.println(rbacUser.getPassword());
        }
    }

    @Test
    public void testJackson() {

    }

}