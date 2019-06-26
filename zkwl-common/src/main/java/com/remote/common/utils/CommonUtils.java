package com.remote.common.utils;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author zsm
 * @date 2019/6/26 17:53
 * @description:
 */
public class CommonUtils {

    public static MqttMessage build(String message){
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(2);
        mqttMessage.setRetained(true);
        mqttMessage.setPayload(message.getBytes());
        return mqttMessage;
    }
}
