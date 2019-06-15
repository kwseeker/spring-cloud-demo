package top.kwseeker.springcloudhystrixclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableHystrix      //内部包含 @EnableCircuitBreaker
public class SpringCloudHystrixClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudHystrixClientApplication.class, args);
    }
}
