package top.kwseeker.userapi.service;

import top.kwseeker.userapi.domain.User;

import java.util.Collection;

public interface UserService {

    /**
     * 保存一个用户
     * @param user
     * @return
     */
    boolean save(User user);

    /**
     * 查询所有用户
     * @return
     */
    Collection<User> findAll();
}
