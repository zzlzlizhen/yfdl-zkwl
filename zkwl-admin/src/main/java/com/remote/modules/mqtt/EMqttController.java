package com.remote.modules.mqtt;

import com.remote.common.emqtt.service.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhangwenping
 * @Date 2019/7/10 14:23
 * @Version 1.0
 **/
@RestController
@RequestMapping("/mqtt")
public class EMqttController {

    @Autowired
    private MqttGateway mqttGateway;

    @RequestMapping(value = "/send", method= RequestMethod.GET)
    public String sendMqtt(String  sendData){
        mqttGateway.sendToMqtt(sendData,"hahahh");
        return "OK";
    }

}
