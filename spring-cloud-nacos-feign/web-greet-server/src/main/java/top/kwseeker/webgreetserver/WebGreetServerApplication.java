package top.kwseeker.webgreetserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WebGreetServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebGreetServerApplication.class, args);
	}

	//Feign 客户端接口定义
	//@RequestLine("GET /greet/{name}/hello")
	//String greetHello(@Param("name") String name);
	@GetMapping("/greet/{name}/hello")
	public String greetHello(@PathVariable("name") String name) {
		return "hello " + name;
	}
}
