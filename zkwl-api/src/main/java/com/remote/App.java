package com.remote;

import com.remote.device.netty.NettySocket;
import com.remote.device.util.EchoServerNoBlock;
import com.remote.device.util.SocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class App {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = SpringApplication.run(App.class, args);
        //Thread server = new Thread(new EchoServerNoBlock(2001));
        //server.start();
        //SocketServer bean = applicationContext.getBean(SocketServer.class);
        //bean.startAction();
        new NettySocket().bind(2001);
    }
}
