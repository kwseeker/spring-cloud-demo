package top.kwseeker.springcloudconfignacosserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope   //
public class SampleController {

    @Value("${user.name}")
    String username;

    @Value("${user.age:18}")
    int age;

    @RequestMapping("/user")
    public String simple() {
        return "Hello Nacos config!" + " Hello " + username + " " + age;
    }
}
