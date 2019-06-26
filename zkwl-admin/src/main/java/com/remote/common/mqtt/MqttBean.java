package com.remote.common.mqtt;

import com.remote.common.config.MqttInfoConfig;
import com.remote.common.mqtt.callback.PubTopicCallBack;
import com.remote.common.mqtt.callback.SubTopicCallBack;
import com.remote.common.utils.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lizhen
 * @date 2019/6/26 15:14
 * @description:
 */
@Configuration
public class MqttBean {

    @Autowired
    private MqttInfoConfig mqttInfoConfig;

    @Bean
    public MqttClient mqttPubClient(){
        MqttClient client = null;
        try {
            client = initMqttClient(null,mqttInfoConfig.getServerClientId(),new PubTopicCallBack());
        }catch (Exception e){
            e.printStackTrace();
        }
        return client;
    }

    @Bean
    public MqttClient mqttSubClient(){
        MqttClient client = null;
        try {
            client = initMqttClient(mqttInfoConfig.getClientTopic(),mqttInfoConfig.getClientClientId(),new SubTopicCallBack());
        }catch (Exception e){
            e.printStackTrace();
            //client = new MqttClient();
        }
        return client;
    }

    /**
     * 功能描述:同一个server需要发布多个主题，只需要建立一次连接即可，初始化对应的MqttTopic对象即可使用
     * @author zsm
     * @date 2019/6/26 17:38
     * @param
     * @return org.eclipse.paho.client.mqttv3.MqttTopic
     */
    @Bean
    public MqttTopic mqttSubTopic(){
        MqttClient mqttClient = this.mqttPubClient();
        if(mqttClient!=null){
            return mqttClient.getTopic(mqttInfoConfig.getServerTopic());
        }
        return null;
    }
    /**
     * 功能描述:初始化mqttClient对象，发布or订阅
     * @author zsm
     * @date 2019/6/26 15:44
     * @param
     * @return org.eclipse.paho.client.mqttv3.MqttClient
     */
    private MqttClient initMqttClient(String subTopic, String clientId, MqttCallback mqttCallback) throws Exception{
        MqttClient client = new MqttClient(mqttInfoConfig.getHost(),clientId , new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(mqttInfoConfig.getUsername());
        options.setPassword(mqttInfoConfig.getPassword().toCharArray());
        options.setConnectionTimeout(mqttInfoConfig.getConnectionTimeout());
        options.setKeepAliveInterval(mqttInfoConfig.getKeepAliveInterval());
        client.setCallback(mqttCallback);
        if(StringUtils.isNotEmpty(subTopic)){
            MqttTopic topic = client.getTopic(subTopic);
            options.setWill(topic, "close".getBytes(), 2, true);
        }
        client.connect(options);
        return client;
    }
}
