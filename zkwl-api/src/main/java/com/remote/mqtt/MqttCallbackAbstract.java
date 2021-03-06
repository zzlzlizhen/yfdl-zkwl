package com.remote.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * 发布消息的回调类
 * <p>
 * 必须实现MqttCallback的接口并实现对应的相关接口方法CallBack 类将实现 MqttCallBack。
 * 每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。
 * 在回调中，将它用来标识已经启动了该回调的哪个实例。
 * 必须在回调类中实现三个方法：
 * <p>
 * public void messageArrived(MqttTopic topic, MqttMessage message)接收已经预订的发布。
 * <p>
 * public void connectionLost(Throwable cause)在断开连接时调用。
 * <p>
 * public void deliveryComplete(MqttDeliveryToken token))
 * 接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。
 * 由 MqttClient.connect 激活此回调。
 */
public abstract class MqttCallbackAbstract implements MqttCallback {

    public void connectionLost(Throwable cause) {
        while (true){
            try {
                this.reConnection();
                break;
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        try {
            System.out.println("deliveryComplete---------" + token.isComplete());
            if(token.isComplete()){
                //发布消息完成之后操作。比如记录发布日志，调用其他日志等等
                this.excutePubMessage(token);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        try {
            System.out.println("接收消息主题 : " + topic);
            System.out.println("接收消息Qos : " + message.getQos());
            System.out.println("接收消息内容 : " + new String(message.getPayload()));
            //接受到的消息处理，可以根据不同的topic做不同的处理
            this.exeuteSubMessage(topic,message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public abstract void exeuteSubMessage(String topic,MqttMessage mqttMessage) throws Exception;

    public abstract void excutePubMessage(IMqttDeliveryToken token) throws Exception;

    public abstract void reConnection() throws Exception;
}