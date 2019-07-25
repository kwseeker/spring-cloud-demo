package top.kwseeker.nacosfeign.serviceconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import top.kwseeker.nacosfeign.serviceprovider.api.ProvideServiceApi;

@EnableCircuitBreaker
@EnableFeignClients(clients = ProvideServiceApi.class)
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"top.kwseeker.nacosfeign"})
public class ServiceConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceConsumerApplication.class, args);
    }
}

