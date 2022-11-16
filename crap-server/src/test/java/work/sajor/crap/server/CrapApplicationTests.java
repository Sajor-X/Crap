package work.sajor.crap.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest()
@ComponentScan("work.sajor.crap")
class CrapApplicationTests {


	@Autowired
	TController tController;

	@Test
	void contextLoads() {
		System.out.println(tController.view(1L));
	}

}
