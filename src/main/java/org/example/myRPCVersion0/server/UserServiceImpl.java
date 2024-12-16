package org.example.myRPCVersion0.server;


import org.example.myRPCVersion0.common.User;
import org.example.myRPCVersion0.service.UserService;

import java.util.Random;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    @Override
    public User getUserByUserId(Integer id) {
        System.out.println("客户端查询id为" + id + "的用户");
        // 模拟从数据库中取用户的行为
        Random random = new Random();
        // 使用builder注解的方法
        User user = User.builder().userName(UUID.randomUUID().toString())
                .id(id)
                .sex(random.nextBoolean()).build();
        return user;
    }
}
