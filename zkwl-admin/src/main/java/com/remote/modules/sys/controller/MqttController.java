
package com.remote.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.remote.common.msg.SendMqttService;
import com.remote.common.utils.R;
import com.remote.modules.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.modules.advancedsetting.service.AdvancedSettingService;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysUserService;
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
public class MqttController extends AbstractController{
	@Autowired(required = false)
	@Qualifier("mqttSubTopic")
	private MqttTopic mqttSubTopic;

	@Autowired
	private SendMqttService sendMqttService;
	@Autowired
	AdvancedSettingService advancedSettingService;
	@ResponseBody
	@RequestMapping("/pub")
	public R list(@RequestParam Map<String, Object> params){
		String projectId = "16b26934-6d1b-4149-80b6-ad94b0d40f35";
		AdvancedSettingEntity advancedSettingEntity = advancedSettingService.queryByProGroupId(projectId,"0");
		String json = JSON.toJSONString(advancedSettingEntity,true);
		System.out.print(json);
		params.put("message",json);
		if(params.get("message")!=null){
			return sendMqttService.publish(mqttSubTopic,(String)params.get("message"));
		}else{
			return sendMqttService.publish(mqttSubTopic,"不知道该发布什么内容");
		}
	}
	
}
