package com.li.myRPCVersion3.client;

import com.li.myRPCVersion1.common.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

/**
 * 接收响应
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RPCResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCResponse msg) throws Exception {
        // 接收到response, 给channel设计别名，让 NettyRPCClient.sendRequest() 里读取response
        AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
        ctx.channel().attr(key).set(msg);
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
