package work.sajor.crap.server.core.dao.dao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import work.sajor.crap.core.dao.dao.RbacTokenDao;
import work.sajor.crap.core.dao.dao.RbacUserDao;
import work.sajor.crap.core.dao.entity.RbacUser;
import work.sajor.crap.core.json.JacksonUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;

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

        work.sajor.crap.core.dao.entity.Test test = new work.sajor.crap.core.dao.entity.Test();
        test.setSex(work.sajor.crap.core.dao.entity.Test.SexEnum.MALE);
        test.setId(11111111111111111L);
        test.setCode("sss");
        test.setName("fjsdkl");
        test.setCreateTime(new Date());
        test.setUpdateTime(LocalDateTime.now());
        System.out.println(JacksonUtil.typedSerialize(test));
        System.out.println(JacksonUtil.serialize(test));
    }

}