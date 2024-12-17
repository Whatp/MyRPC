package com.li.myRPCVersion1.server;


import com.li.myRPCVersion1.common.RPCRequest;
import com.li.myRPCVersion1.common.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RPCServer {
    /**
     * 服务器接受解析request与封装发送response对象
     * 接受客户端发送的RPCRequest对象，解析请求内容，利用Java反射调用目标的方法
     * 讲执行结果封装到RPCResponse对象中，并且返回给客户端
     */
    public static void main(String[] args) {

        // BIO模式下，这种方式在高并发情况下可能导致线程资源耗尽，后续可优化为NIO或者线程池
        try (ServerSocket serverSocket = new ServerSocket(8899)) {
            System.out.println("服务端启动了");
            ExecutorService threadPool = Executors.newCachedThreadPool();
            // BIO的方式监听Socket
            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.execute(() -> handleClient(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }

    private static void handleClient(Socket socket) {
        UserServiceImpl userService = new UserServiceImpl();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            // 客户端发送的是序列化之后的，使用ois.readObject()反序列化，RPCRequest包含了接口名、方法名、参数类型和参数值
            RPCRequest request = (RPCRequest) ois.readObject();

            // 反射调用对应方法
            Method method = userService.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
            Object invoke = method.invoke(userService, request.getParams());
            // 封装，写入response对象
            oos.writeObject(RPCResponse.success(invoke));
            oos.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("从IO中读取数据错误");
        }
    }

}

