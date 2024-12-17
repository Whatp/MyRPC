package com.li.myRPCVersion0.server;


import com.li.myRPCVersion0.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RPCServer {
    public static void main(String[] args) {
        /**
         * 在端口8899上启动一个serverSocket，等待客户端连接
         * 使用try-with-resources自动关闭Socket流，避免资源泄露
         *
         */
        UserServiceImpl userService = new UserServiceImpl();
        final int port = 8899;

        ExecutorService threadPoll = Executors.newCachedThreadPool();
        try {
            // 监听端口
            System.out.println("服务端启动了");
            // BIO的方式监听Socket
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("收到客户端连接："+socket.getRemoteSocketAddress());
                // 交给线程池去处理
                threadPoll.execute(() -> handleClient(socket, userService));
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }

    private static void handleClient(Socket socket, UserServiceImpl userService) {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())
        ) {
            // 读取客户端传过来的id
            Integer id = ois.readInt();
            System.out.println("服务端接收到ID: " + id);

            // 查询User对象
            User user = userService.getUserByUserId(id);
            System.out.println("服务端查询到的User: " + user);

            // 写入User对象返回给客户端
            oos.writeObject(user);
            oos.flush();
        } catch (IOException e) {
            System.err.println("处理客户端请求时出错：" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("关闭客户端Socket时出错：" + e.getMessage());
            }
        }
    }
}
