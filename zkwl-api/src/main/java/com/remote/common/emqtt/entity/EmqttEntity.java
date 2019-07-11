package com.remote.common.emqtt.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author zhangwenping
 * @Date 2019/7/10 14:19
 * @Version 1.0
 **/
@ConfigurationProperties("spring.mqtt")
@Component
@Data
public class EmqttEntity {
    private String username;

    private String mqpassword;

    private String hostUrl;

    private String clientId;

    private String defaultTopic;

    private Integer completionTimeout;
}
