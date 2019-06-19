package com.remote;

import com.remote.device.util.EchoServerNoBlock;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class App {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
        Thread server = new Thread(new EchoServerNoBlock(2001));
        server.start();

    }

}
