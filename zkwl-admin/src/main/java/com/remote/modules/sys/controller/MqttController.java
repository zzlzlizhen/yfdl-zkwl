
package com.remote.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.remote.common.msg.SendMqttService;
import com.remote.common.utils.R;
import com.remote.modules.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.modules.advancedsetting.service.AdvancedSettingService;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysUserService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


/**
 * 测试发布
 *
 * @author Mark sunlightcs@gmail.com
 */
@Controller
@RequestMapping("/sys/mqtt")
public class MqttController extends AbstractController{
	@Autowired(required = false)
	@Qualifier("mqttSubTopic")
	private MqttTopic mqttSubTopic;

	@Autowired
	private SendMqttService sendMqttService;
	@Autowired
	AdvancedSettingService advancedSettingService;

	@Autowired(required = false)
	@Qualifier("mqttPubClient")
	private MqttClient mqttPubClient;

	public static Map<String,MqttTopic> MAP_MQTT_TOPIC = new HashMap<String,MqttTopic>();


	@ResponseBody
	@RequestMapping("/pub")
	public R list(@RequestParam Map<String, Object> params){
		String groupId = "1cbc10c8-1e06-4507-b1c5-8db72c344f3b";
		AdvancedSettingEntity advancedSettingEntity = advancedSettingService.queryByDevOrGroupId(groupId,"0");
		String json = JSON.toJSONString(advancedSettingEntity,true);
		System.out.print(json);
		params.put("message",json);
		if(params.get("message")!=null){
			return sendMqttService.publish(mqttSubTopic,(String)params.get("message"));
		}else{
			return sendMqttService.publish(mqttSubTopic,"不知道该发布什么内容");
		}
	}

	/**
	 * 功能描述:动态的向任意的主题发布消息
	 * 通过mqttPubClient动态的获取主题对象mqttTopic，并缓存到jvm的内存Map里。服务不重启的话，mqttTopic对象可重复使用，无需每次都单独的创建主题
	 * @author lizhen
	 * @date 2019/7/10 17:18
	 * @param topic，message
	 * @return com.remote.common.utils.R
	 */
	@ResponseBody
	@RequestMapping("/dynamic_pub")
	public R dynamicPub(@RequestParam Map<String, Object> params){
		String topic = null;
		String message = null;
		if(params.get("topic") == null){
			return R.error("发布mqtt消息的topic参数不能为空");
		}else{
			topic = (String)params.get("topic");
		}
		if(params.get("message") == null){
			return R.error("发布mqtt消息的内容不能为空");
		}else{
			message = (String)params.get("message");
		}
		MqttTopic mqttTopic = MAP_MQTT_TOPIC.get(topic);
		if(mqttTopic == null){
			mqttTopic = mqttPubClient.getTopic(topic);
			MAP_MQTT_TOPIC.put(topic,mqttTopic);
		}
		return sendMqttService.publish(mqttTopic,message);
	}
	
}
