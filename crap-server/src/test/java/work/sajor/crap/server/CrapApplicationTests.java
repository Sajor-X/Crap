package work.sajor.crap.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest()
@ComponentScan("work.sajor.crap")
class CrapApplicationTests {


    @Test
    void contextLoads() {
//		System.out.println(tController.view(1L));
    }

}
