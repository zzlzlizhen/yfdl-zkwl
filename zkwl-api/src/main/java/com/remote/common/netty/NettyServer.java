package com.remote.common.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * @Author zhangwenping
 * @Date 2019/7/1 15:35
 * @Version 1.0
 **/
public class NettyServer implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private int port;
    public NettyServer(int port){
        this.port = port;
    }
    public NettyServer(){}
    @Override
    public void run() {
        EventLoopGroup pGroup = new NioEventLoopGroup(); //线程组：用来处理网络事件处理（接受客户端连接）
        EventLoopGroup cGroup = new NioEventLoopGroup(); //线程组：用来进行网络通讯读写
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(pGroup, cGroup)
                    .channel(NioServerSocketChannel.class) //注册服务端channel
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel sc) throws Exception {
                            // 超时处理
                            sc.pipeline().addLast(new IdleStateHandler(60,0,0,TimeUnit.SECONDS));
                            /**
                             * 自定义ChannelInboundHandlerAdapter
                             */
                            sc.pipeline().addLast(new ServerHandler());
                        }
                    });
            ChannelFuture cf = null;
            cf = b.bind(2001).sync();
            if(cf.isSuccess()){
                logger.info("启动 Netty 成功");
            }
            cf.channel().closeFuture().sync();
        }
        catch(Exception e)
        {
            pGroup.shutdownGracefully();
            cGroup.shutdownGracefully();
            e.printStackTrace();
        }
    }
}
