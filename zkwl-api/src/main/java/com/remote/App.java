package com.remote;

import com.rabbitmq.client.Command;
import com.remote.common.netty.NettyServer;
import com.remote.device.util.EchoServerNoBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class App implements CommandLineRunner {

    public static void main(String[] args){
        SpringApplication.run(App.class, args);
        //Thread server = new Thread(new EchoServerNoBlock(2001));
        //server.start();

    }
    @Override
    public void run(String... args) throws Exception {
        Thread server = new Thread(new NettyServer(2001));
        server.start();
    }

}
