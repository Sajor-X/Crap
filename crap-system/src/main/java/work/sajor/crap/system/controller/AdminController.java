package work.sajor.crap.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.sajor.crap.core.dao.dao.RbacUserDao;
import work.sajor.crap.core.web.WebController;

/**
 * <p>
 *
 * </p>
 *
 * @author Sajor
 * @since 2022-12-01
 */
@RestController
@RequestMapping("/user")
public class AdminController extends WebController<RbacUserDao> {
    @GetMapping("/hello")
    public Object view() {
        return "hello";
    }

    @GetMapping("/list")
    public Object list() {
        return dao.list();
    }

}

