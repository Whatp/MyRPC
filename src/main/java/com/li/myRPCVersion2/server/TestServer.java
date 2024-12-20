package com.li.myRPCVersion2.server;

import com.li.myRPCVersion0.service.UserService;
import com.li.myRPCVersion2.server.Impl.ThreadPoolRPCServerImpl;
import com.li.myRPCVersion2.service.BlogService;
import com.li.myRPCVersion2.service.impl.BlogServiceImpl;
import com.li.myRPCVersion2.service.impl.UserServiceImpl;

public class TestServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);

        RPCServer RPCServer = new ThreadPoolRPCServerImpl(serviceProvider);
        RPCServer.start(8899);
    }
}