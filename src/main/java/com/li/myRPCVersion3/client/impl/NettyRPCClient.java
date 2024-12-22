package com.li.myRPCVersion3.client.impl;

import com.li.myRPCVersion1.common.RPCRequest;
import com.li.myRPCVersion1.common.RPCResponse;
import com.li.myRPCVersion3.client.NettyClientInitializer;
import com.li.myRPCVersion3.client.RPCClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

/**
 * 实现RPCClient接口
 * <p>
 *
 * Client/Server
 *    |
 * [Channel]  ->  [ChannelPipeline]  ->  [ChannelHandlers]
 *                  (入站处理器链)          (InboundHandler/OutboundHandler)
 *                  (出站处理器链)
 */
public class NettyRPCClient implements RPCClient {
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
    private final String host;
    private final int port;
    public NettyRPCClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    // netty客户端初始化，重复使用
    static {
        // netty提供的多线程事件循环，用于管理事件的监听和任务的执行
        eventLoopGroup = new NioEventLoopGroup();
        // Netty客户端的启动器，用于配置客户端的相关参数
        bootstrap = new Bootstrap();
        // NioSocketChannel 用于创建基于NIO的客户端通道
        // 服务端在接收到客户端连接后，会为每个连接创建一个 SocketChannel，并通过 ChannelInitializer 来初始化它：
        // NettyClientInitializer 一个ChannelInitializer子类，用于初始化客户端的channel，包括编码器、解码器以及自定义处理器
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

    /**
     * 这里需要操作一下，因为netty的传输都是异步的，你发送request，会立刻返回一个值， 而不是想要的相应的response
     */
    @Override
    public RPCResponse sendRequest(RPCRequest request) {
        try {
            // 连接到服务器，并等待连接完成
            ChannelFuture channelFuture  = bootstrap.connect(host, port).sync();
            // 获取通信通道
            Channel channel = channelFuture.channel();
            // 发送数据
            channel.writeAndFlush(request);
            channel.closeFuture().sync();
            // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在handler中设置）
            // AttributeKey是，线程隔离的，不会有线程安全问题。
            // Netty是异步传输的，这里通过AttributeKey 和阻塞sync()实现了同步获取结果的逻辑，更好的方式是通过回调函数，避免线程阻塞，提高并发性能
            AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
            RPCResponse response = channel.attr(key).get();

            System.out.println(response);
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
        return null;
    }
}
