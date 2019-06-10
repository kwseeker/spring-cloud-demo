package top.kwseeker.springcloudconfigclient.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Value("${name}")
    private String name="test";

    @GetMapping("/test")
    public String searchConfig() {
        return "config info -> [name]:[{"+ name +"}]";
    }
}
