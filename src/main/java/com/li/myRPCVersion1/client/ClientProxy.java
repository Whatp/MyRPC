package com.li.myRPCVersion1.client;

import com.li.myRPCVersion1.common.RPCRequest;
import com.li.myRPCVersion1.common.RPCResponse;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    // 传入参数Service接口的class对象，反射封装成一个request
    private String host;
    private int port;

    /**
     * 实现客户端的核心组件，利用JDK动态代理机制简化了接口调用的实现。
     * 将接口调用抽象为RPC请求，隐藏网络通信细节
     * 利用反射与序列化机制，实现请求构造与服务端交互
     * 重写的invoke方法，每一次代理对象调用方法，会经过此方法增强（反射获取request对象，socket发送至客户端）
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 1.request的构建，使用了lombok中的builder，代码简洁
        RPCRequest request = RPCRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramsTypes(method.getParameterTypes()).build();
        // 2.发送请求，得到响应（数据传输）
        RPCResponse response = IOClient.sendRequest(host, port, request);
        if (response != null && response.getCode() == 200) {
            System.out.println("调用成功，返回数据：" + response.getData());
        }
        else {
            System.out.println("调用失败，错误信息：" + (response != null ? response.getMessage() : "未知错误"));
        }
        // 3.返回响应中的数据 System.out.println(response);
        return response.getData();
    }
    <T>T getProxy(Class<T> clazz){
        // 动态代理的核心方法：Proxy.newProxyInstance这是用于生成接口的代理对象
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)o;
    }
}
