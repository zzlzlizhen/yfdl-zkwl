package com.remote.common.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * @Author zhangwneping
 * @Date 2019/6/20 16:45
 * @Version 1.0
 **/
@Configuration
public class DirectRabbitConfig {

    //队列
    @Bean
    public Queue CalonDirectQueue() {
        return new Queue("CalonDirectQueue",true);
    }

    //Direct交换机
    @Bean
    DirectExchange CalonDirectExchange() {
        return new DirectExchange("CalonDirectExchange");
    }

    //绑定
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(CalonDirectQueue()).to(CalonDirectExchange()).with("CalonDirectRouting");
    }

}
