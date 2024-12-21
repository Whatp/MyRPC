package com.li.myRPCVersion3.server;

import com.li.myRPCVersion3.server.impl.NettyRPCServer;
import com.li.myRPCVersion3.service.BlogService;
import com.li.myRPCVersion3.service.impl.BlogServiceImpl;
import com.li.myRPCVersion3.service.UserService;
import com.li.myRPCVersion3.service.impl.UserServiceImpl;

public class TestServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);

        RPCServer RPCServer = new NettyRPCServer(serviceProvider);
        RPCServer.start(8899);
    }
}
