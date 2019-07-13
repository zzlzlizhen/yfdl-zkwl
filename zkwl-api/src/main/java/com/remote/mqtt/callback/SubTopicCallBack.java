package com.remote.mqtt.callback;

import com.remote.SpringBeanFactory;
import com.remote.mqtt.MqttCallbackAbstract;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @author zsm
 * @date 2019/6/26 17:33
 * @description:
 */
@Service
@Lazy(value = true)
public class SubTopicCallBack extends MqttCallbackAbstract {

    @Autowired
    private SpringBeanFactory springBeanFactory;

    @Override
    public void exeuteSubMessage(String topic, MqttMessage mqttMessage) throws Exception {
        System.out.println(this.getClass().getName() + "----"+this);
    }

    @Override
    public void excutePubMessage(IMqttDeliveryToken token) throws Exception {

    }

    @Override
    public void reConnection() throws Exception {
    /*    MqttClient mqttClient = springBeanFactory.getBeanById("mqttSubClient");
        if(null != mqttClient && !mqttClient.isConnected()){
            mqttClient.reconnect();
            System.out.println("重连成功");
        }*/

    }

}
