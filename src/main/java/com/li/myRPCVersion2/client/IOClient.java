package com.li.myRPCVersion2.client;

import com.li.myRPCVersion1.common.RPCRequest;
import com.li.myRPCVersion1.common.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOClient {
    // 客户端发起一次请求调用，Socket建立连接，发起请求Request，得到响应Response
    // 这里的request是封装好的，不同的service需要进行不同的封装， 客户端只知道Service接口，需要一层动态代理根据反射封装不同的Service
    public static RPCResponse sendAndReceive (String host, int port, RPCRequest request) {
        try (Socket socket = new Socket(host, port)) {

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println("封装好的request请求为：" + request);

            objectOutputStream.writeObject(request);
            objectOutputStream.flush();

            //System.out.println(response.getData());
            return (RPCResponse) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println();
            return null;
        }
    }
}