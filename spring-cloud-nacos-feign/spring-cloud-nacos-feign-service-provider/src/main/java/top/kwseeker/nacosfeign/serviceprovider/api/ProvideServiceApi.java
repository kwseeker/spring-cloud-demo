package top.kwseeker.nacosfeign.serviceprovider.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.kwseeker.nacosfeign.serviceprovider.api.fallback.ProvideServiceFallbackFactory;

@FeignClient(value = "spring-cloud-nacos-feign-service-provider"
        ,fallbackFactory = ProvideServiceFallbackFactory.class
)
public interface ProvideServiceApi {

    //@GetMapping("/hello")
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    String greet();
}
