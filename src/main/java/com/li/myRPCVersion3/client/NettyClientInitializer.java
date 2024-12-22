package com.li.myRPCVersion3.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 同样的与服务端解码和编码格式
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * SocketChannel 是Netty中用来表示TCP连接的核心组件，负责网络连接和I/O操作
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // ChannelPipeline 是Netty 的数据处理链，类似于一个流水线
        // 每个通道SocketChannel 都有自己的 ChannelPipeline ，用于管理所有的处理器(ChannelHandler)
        ChannelPipeline pipeline = ch.pipeline();
        // 消息格式 [长度][消息体]
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
        // 计算当前待发送消息的长度，写入到前4个字节中
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new ObjectEncoder());

        pipeline.addLast(new ObjectDecoder(new ClassResolver() {
            @Override
            public Class<?> resolve(String className) throws ClassNotFoundException {
                return Class.forName(className);
            }
        }));

        pipeline.addLast(new NettyClientHandler());
    }
}
