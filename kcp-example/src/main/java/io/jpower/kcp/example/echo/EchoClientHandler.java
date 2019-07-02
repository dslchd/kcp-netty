package io.jpower.kcp.example.echo;

import io.jpower.kcp.netty.UkcpChannel;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handler implementation for the echo client.
 *
 * @author <a href="mailto:szhnet@gmail.com">szh</a>
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * Creates a client-side handler.
     */
    public EchoClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        UkcpChannel kcpCh = (UkcpChannel) ctx.channel();
        kcpCh.conv(EchoClient.CONV); // set conv
        //建立链接的时候 向服务器发送一个hello
        ByteBuf firstMessage=Unpooled.copiedBuffer("hello world".getBytes());
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof ByteBuf){
            ByteBuf buf= (ByteBuf) msg;
            byte[] byteArrays=new byte[buf.capacity()];
            buf.readBytes(byteArrays);
            System.out.println("收到Server消息:"+new String(byteArrays));
        }
        ctx.writeAndFlush(msg);
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
