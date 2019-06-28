package com.remote;

import com.remote.device.util.EchoServerNoBlock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class App {

    public static void main(String[] args){
        SpringApplication.run(App.class, args);
        Thread server = new Thread(new EchoServerNoBlock(2001));
        server.start();

    }

}
