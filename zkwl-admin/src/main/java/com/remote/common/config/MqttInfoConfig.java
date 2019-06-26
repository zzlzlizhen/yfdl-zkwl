package com.remote.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zsm
 * @date 2019/6/18 17:08
 * @description:
 */
@Component
@ConfigurationProperties(prefix = "mqtt")
@PropertySource(value = {"classpath:mqtt-info.properties"}, encoding = "utf-8")
public class MqttInfoConfig {

    private String host;

    private Integer connectionTimeout;

    private Integer keepAliveInterval;

    private String username;

    private String password;

    private String serverClientId;

    private String clientClientId;

    private String serverTopic;

    private String clientTopic;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getKeepAliveInterval() {
        return keepAliveInterval;
    }

    public void setKeepAliveInterval(Integer keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerClientId() {
        return serverClientId;
    }

    public void setServerClientId(String serverClientId) {
        this.serverClientId = serverClientId;
    }

    public String getClientClientId() {
        return clientClientId;
    }

    public void setClientClientId(String clientClientId) {
        this.clientClientId = clientClientId;
    }

    public String getServerTopic() {
        return serverTopic;
    }

    public void setServerTopic(String serverTopic) {
        this.serverTopic = serverTopic;
    }

    public String getClientTopic() {
        return clientTopic;
    }

    public void setClientTopic(String clientTopic) {
        this.clientTopic = clientTopic;
    }
}
