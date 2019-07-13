package com.remote.mqtt.client;

import com.remote.config.MqttInfoConfig;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author zsm
 * @date 2019/6/26 16:05
 * @description:
 */
@Service("mqttSub")
@Lazy(value = false)
public class MqttSub {

    @Autowired(required = false)
    @Qualifier("mqttSubClient")
    private MqttClient mqttSubClient;

    @Autowired
    private MqttInfoConfig mqttInfoConfig;

    @PostConstruct
    public void init() {
        try {
            //订阅消息
            int[] Qos = {2};
            String[] topic1 = {mqttInfoConfig.getClientTopic()};
            if(mqttSubClient!=null){
                mqttSubClient.subscribe(topic1,Qos);
            }
        }catch (Exception e){
            //mqttSubClient.reconnect();
            e.printStackTrace();
        }
    }
}
