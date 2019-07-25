package top.kwseeker.nacosfeign.serviceconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.nacosfeign.serviceprovider.api.ProvideServiceApi;

@RestController
public class TestController {

    private ProvideServiceApi provideServiceApi;

    @Autowired
    public TestController(ProvideServiceApi provideServiceApi) {
        this.provideServiceApi = provideServiceApi;
    }

    @GetMapping("/test/hello")
    public String testHello() {
        return provideServiceApi.greet();
    }
}
