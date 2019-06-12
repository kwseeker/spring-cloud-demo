package top.kwseeker.userservice.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.userapi.domain.User;
import top.kwseeker.userapi.service.UserService;

import java.util.Collection;

@RestController
public class UserServiceProviderRestApiController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/save")
    public User saveUser(@RequestBody User user) {
        if (userService.save(user)) {
            System.out.println("UserService 服务方：保存用户成功！" + user);
            return user;
        } else {
            return null;
        }
    }

    @GetMapping("/user/list")
    public Collection<User> list() {
        return userService.findAll();
    }
}
