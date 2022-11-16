package work.sajor.crap.server;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
@ComponentScan("work.sajor.crap")
@MapperScan("work.sajor.crap")
public class CrapApplication {

	public static void main(String[] args) throws UnknownHostException {
		ConfigurableApplicationContext run = SpringApplication.run(CrapApplication.class, args);
		ConfigurableEnvironment environment = run.getEnvironment();

		String ip = InetAddress.getLocalHost().getHostAddress();
		String port = environment.getProperty("server.port");
		String path = environment.getProperty("server.servlet.context-path");

		log.info("\n----------------------------------------------------------\n\t" +
				"Application is running! Access URLs:\n\t" +
				"Local: \t\thttp://localhost:" + port + path + "/\n\t" +
				"External: \thttp://" + ip + ":" + port + path + "/\n" +
				"----------------------------------------------------------");
	}

}
