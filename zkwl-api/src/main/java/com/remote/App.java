package com.remote;

import com.remote.device.util.SocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        SocketServer socketServer = new SocketServer();
        socketServer.startAction();
    }
}
