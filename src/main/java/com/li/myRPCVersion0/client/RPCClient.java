package com.li.myRPCVersion0.client;


import com.li.myRPCVersion0.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class RPCClient {
    public static void main(String[] args) {
        try {
            // 建立Socket连接
            Socket socket = new Socket("127.0.0.1", 8899);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // 传给服务器id
            int userID = new Random().nextInt();
            objectOutputStream.writeInt(userID);
            objectOutputStream.flush();
            System.out.println("客户端发送的用户ID："+userID);

            // 接受服务端返回的user对象
            Object resp = objectInputStream.readObject();
            if (resp instanceof User) {
                User user = (User) resp;
                System.out.println("服务端返回的User:" + user);
            }else {
                System.out.println("服务端返回的不是User对象");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("客户端启动失败");
        }
    }
}
