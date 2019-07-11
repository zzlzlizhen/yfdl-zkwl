package com.remote.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttTask implements Runnable{
    @Override
    public void run() {
        final long timeInterval = 2000;// 两秒运行一次
        while (true) {
            // ------- code for task to run
            try {       //你要运行的程序
                ClientSarch clientSearch = new ClientSarch();
                try {
                    clientSearch.start();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                Thread.sleep(1000); //给一秒时间接收服务器消息
                Integer num = Integer.valueOf(clientSearch.resc());
                System.out.println("当前客户端连接数："+num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // ------- ends here
            try {
                Thread.sleep(timeInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
