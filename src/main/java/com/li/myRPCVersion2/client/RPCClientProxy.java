package com.li.myRPCVersion2.client;

import com.li.myRPCVersion1.common.RPCRequest;
import com.li.myRPCVersion1.common.RPCResponse;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

@AllArgsConstructor
public class RPCClientProxy implements InvocationHandler {
    // 传入参数Service接口的class对象，反射封装成一个request
    private String host;
    private int port;

    // jdk 动态代理， 每一次代理对象调用方法，会经过此方法增强（反射获取request对象，socket发送至客户端）
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // request的构建，使用了lombok中的builder，代码简洁
        // method.getDeclaringClass() 返回当前方法所有的类或接口，getName()返回类或接口的全限定名（包括包名）
        RPCRequest request = RPCRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramsTypes(method.getParameterTypes()).build();

        //数据传输
        RPCResponse response = IOClient.sendAndReceive(host, port, request);
        //System.out.println(response);
        return response.getData();
    }

    /**
     * 在运行时动态生成一个代理类，这个类会实现clazz接口
     * 代理类的方法调用会被拦截，并且交给InvocationHandler.invoke()方法处理
     */
    <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(
                clazz.getClassLoader(),    // 使用目标类的类加载器
                new Class[]{clazz},        // 为目标类实现的接口生成代理
                this                       // 代理的核心逻辑由 ClientProxy 的 invoke 方法处理
        );
        return (T)o;                       // 返回代理对象
    }
}
