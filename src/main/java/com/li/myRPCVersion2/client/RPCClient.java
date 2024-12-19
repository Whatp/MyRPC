package com.li.myRPCVersion2.client;

import com.li.myRPCVersion0.common.User;
import com.li.myRPCVersion0.service.UserService;
import com.li.myRPCVersion2.common.Blog;
import com.li.myRPCVersion2.service.BlogService;

/**
 * 动态代理对象并不直接调用 UserService 的实现，而是将方法名、参数等信息传递给 invoke() 方法。
 * 在 invoke() 方法中，构造了一个 RPCRequest，包含接口名、方法名、参数和参数类型，使用IOClient发送到服务端
 */
public class RPCClient {
    public static void main(String[] args) {
        // 使用new关键字创建一个 ClientProxy 的实例，这个 ClientProxy 实例会被用来作为动态代理的 InvocationHandler，
        // 负责拦截方法调用，并且实现具体逻辑，此时的 ClientProxy 是一个普通对象，还不是代理对象
        RPCClientProxy rpcClientProxy = new RPCClientProxy("127.0.0.1", 8899);

        // 调用 Client Proxy 实例的 getProxy() 方法，创建一个动态代理对象 userServiceProxy ，实现了 UserService 接口。
        // 代理对象的所有方法调用都会被拦截，转交给 ClientProxy 的 invoke() 方法处理。
        UserService userServiceProxy = rpcClientProxy.getProxy(UserService.class);

        // 调用动态代理对象 userServiceProxy 的方法 getUserById() ，方法调用被拦截，并转给 ClientProxy 的 invoke() 方法。
        User userByUserId = userServiceProxy.getUserByUserId(10);
        System.out.println("从服务端得到的user为：" + userByUserId);

        User user = User.builder().userName("张三").id(100).sex(true).build();
        Integer integer = userServiceProxy.insertUserId(user);
        System.out.println("向服务端插入数据："+integer);

        BlogService blogService = rpcClientProxy.getProxy(BlogService.class);
        Blog blogById = blogService.getBlogById(10000);
        System.out.println("从服务端得到的blog为：" + blogById);
    }
}
