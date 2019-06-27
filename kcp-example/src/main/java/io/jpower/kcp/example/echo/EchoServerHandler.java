package io.jpower.kcp.example.echo;

import io.jpower.kcp.netty.UkcpChannel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handler implementation for the echo server.
 *
 * @author <a href="mailto:szhnet@gmail.com">szh</a>
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道已激活");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String message="";
        if (msg instanceof ByteBuf){
            ByteBuf buf= (ByteBuf) msg;
            byte [] byteArray=new byte[buf.capacity()];
            buf.readBytes(byteArray);
            message=new String(byteArray);
            System.out.println("收到Client消息:"+message);
        }
        ctx.write("Server Say:"+message);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
