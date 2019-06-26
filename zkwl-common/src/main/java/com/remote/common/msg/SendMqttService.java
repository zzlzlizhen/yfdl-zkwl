package com.remote.common.msg;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.remote.common.config.SendPhoneSecurityCode;
import com.remote.common.constant.CommonConstants;
import com.remote.common.utils.CommonUtils;
import com.remote.common.utils.R;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

/**
 * @author zsm
 * @date 2019/6/20 16:32
 * @description:
 */
@Service("sendMqttService")
public class SendMqttService {

    public R publish(MqttTopic topic,String message){
        try {
            MqttDeliveryToken token = topic.publish(CommonUtils.build(message));
            token.waitForCompletion();
            if(token.isComplete()){
                return R.ok();
            }else{
                return R.error().put("errorMsg","token.isComplete()="+token.isComplete());
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error().put("errorMsg",e.getMessage());
        }
    }


}
