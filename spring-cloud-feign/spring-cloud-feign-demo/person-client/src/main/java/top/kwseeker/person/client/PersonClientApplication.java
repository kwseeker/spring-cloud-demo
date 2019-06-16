package top.kwseeker.person.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import top.kwseeker.feign.api.service.PersonService;
import top.kwseeker.person.client.ribbon.FirstServerForeverRule;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(clients = PersonService.class)
@EnableHystrix
public class PersonClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonClientApplication.class, args);
    }

    @Bean
    public FirstServerForeverRule firstServerForeverRule() {
        return new FirstServerForeverRule();
    }
}
