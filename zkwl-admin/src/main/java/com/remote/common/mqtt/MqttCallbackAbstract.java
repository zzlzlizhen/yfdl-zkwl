package com.remote.common.mqtt;

import com.alibaba.fastjson.JSON;
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
        // 连接丢失后，一般在这里面进行重连
        System.out.println("连接断开，可以做重连");
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

 /*           System.out.println("接收消息主题 : " + topic);
            System.out.println("接收消息Qos : " + message.getQos());*/
            System.out.println("接收消息内容 : " + new String(message.getPayload(),"UTF-8"));
            byte[] bytes = message.getPayload();
            String str = toHexString(bytes);
            System.out.println("接收消息内容 : " + str);
            //接受到的消息处理，可以根据不同的topic做不同的处理
            this.exeuteSubMessage(topic,message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String toHexString(byte[] byteArray) {
        String str = null;
        if (byteArray != null && byteArray.length > 0) {
            StringBuffer stringBuffer = new StringBuffer(byteArray.length);
            for (byte byteChar : byteArray) {
                stringBuffer.append(String.format("%02X", byteChar));
            }
            str = stringBuffer.toString();
        }
        return str;
    }
    public abstract void exeuteSubMessage(String topic,MqttMessage mqttMessage) throws Exception;

    public abstract void excutePubMessage(IMqttDeliveryToken token) throws Exception;
}