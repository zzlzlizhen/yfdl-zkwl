
package com.remote.sys.controller;

import com.remote.common.msg.SendMqttService;
import com.remote.common.utils.R;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * 测试发布
 *
 * @author Mark sunlightcs@gmail.com
 */
@Controller
@RequestMapping("/sys/mqtt")
public class MqttController{
	@Autowired(required = false)
	@Qualifier("mqttSubTopic")
	private MqttTopic mqttSubTopic;

	@Autowired
	private SendMqttService sendMqttService;

	@ResponseBody
	@RequestMapping("/pub")
	public R list(@RequestParam Map<String, Object> params){

		params.put("message","hahh");
		if(params.get("message")!=null){
			return sendMqttService.publish(mqttSubTopic,(String)params.get("message"));
		}else{
			return sendMqttService.publish(mqttSubTopic,"不知道该发布什么内容");
		}
	}
	
}
