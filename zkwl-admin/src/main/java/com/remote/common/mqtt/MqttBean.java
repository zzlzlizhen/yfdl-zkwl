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
 * @author zsm
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
        // host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
        MqttClient client = new MqttClient(mqttInfoConfig.getHost(),clientId , new MemoryPersistence());
        // MQTT的连接设置
        MqttConnectOptions options = new MqttConnectOptions();
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，设置为true表示每次连接到服务器都以新的身份连接
        options.setCleanSession(true);
        // 设置连接的用户名
        options.setUserName(mqttInfoConfig.getUsername());
        // 设置连接的密码
        options.setPassword(mqttInfoConfig.getPassword().toCharArray());
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(mqttInfoConfig.getConnectionTimeout());
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(mqttInfoConfig.getKeepAliveInterval());
        // 设置回调
        client.setCallback(mqttCallback);
        if(StringUtils.isNotEmpty(subTopic)){
            MqttTopic topic = client.getTopic(subTopic);
            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
            //遗嘱
            options.setWill(topic, "close".getBytes(), 2, true);
        }
        client.connect(options);
        return client;
    }
}
