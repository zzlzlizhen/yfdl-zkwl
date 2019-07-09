package com.remote.common.mqtt.callback;

import com.remote.common.mqtt.MqttCallbackAbstract;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

/**
 * @author zsm
 * @date 2019/6/26 17:33
 * @description:
 */
@Service
public class SubTopicCallBack extends MqttCallbackAbstract {

    @Override
    public void exeuteSubMessage(String topic, MqttMessage mqttMessage) throws Exception {//发布消息
        System.out.println(this.getClass().getName() + "----"+this);
    }

    @Override
    public void excutePubMessage(IMqttDeliveryToken token) throws Exception {

    }
}
