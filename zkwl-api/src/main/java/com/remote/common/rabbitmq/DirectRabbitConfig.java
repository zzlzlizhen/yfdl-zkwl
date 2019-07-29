package com.remote.common.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Author zhangwneping
 * @Date 2019/6/20 16:45
 * @Version 1.0
 **/
@Configuration
public class DirectRabbitConfig {
    //升级队列
    @Bean
    public Queue upgradeQueue() {
        return new Queue("topic.upgrade");
    }

    //上传队列
    @Bean
    public Queue upLoadQueue() {
        return new Queue("topic.upload");
    }

    //Direct交换机
    @Bean
    TopicExchange exchange() {
        return new TopicExchange("topicExchange");
    }

    //绑定 升级队列
    @Bean
    Binding bindingUpgradeDirect() {
        return BindingBuilder.bind(upgradeQueue()).to(exchange()).with("topic.upgrade");
    }
    //绑定 上传队列
    @Bean
    Binding bindingUpLoadDirect() {
        return BindingBuilder.bind(upLoadQueue()).to(exchange()).with("topic.upload");
    }

}
