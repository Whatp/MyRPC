package com.li.myRPCVersion2.client;

import com.li.myRPCVersion0.common.User;
import com.li.myRPCVersion0.service.UserService;
import com.li.myRPCVersion2.common.Blog;
import com.li.myRPCVersion2.service.BlogService;

public class RPCClient {
    public static void main(String[] args) {

        RPCClientProxy rpcClientProxy = new RPCClientProxy("127.0.0.1", 8899);
        UserService userService = rpcClientProxy.getProxy(UserService.class);

        User userByUserId = userService.getUserByUserId(10);
        System.out.println("从服务端得到的user为：" + userByUserId);

        User user = User.builder().userName("张三").id(100).sex(true).build();
        Integer integer = userService.insertUserId(user);
        System.out.println("向服务端插入数据："+integer);

        BlogService blogService = rpcClientProxy.getProxy(BlogService.class);
        Blog blogById = blogService.getBlogById(10000);
        System.out.println("从服务端得到的blog为：" + blogById);
    }
}
