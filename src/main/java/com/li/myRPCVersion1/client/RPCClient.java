package com.li.myRPCVersion1.client;

import com.li.myRPCVersion0.common.User;
import com.li.myRPCVersion0.service.UserService;

public class RPCClient {
    public static void main(String[] args) {

        ClientProxy clientProxy = new ClientProxy("127.0.0.1", 8899);
        // 获取 userService 的代理对象
        UserService userServiceProxy = clientProxy.getProxy(UserService.class);

        // 服务的方法1，调用代理对象的方法
        User userByUserId = userServiceProxy.getUserByUserId(10);
        System.out.println("从服务端得到的user为：" + userByUserId);
        // 服务的方法2
        User user = User.builder().userName("张三").id(100).sex(true).build();
        Integer integer = userServiceProxy.insertUserId(user);
        System.out.println("向服务端插入数据："+integer);
    }
}
