package work.sajor.crap.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author Sajor
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/a")
public class AControlloer {

    @GetMapping("/view")
    public Object view() {
        return "111";
    }
}
