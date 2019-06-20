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
import com.remote.common.config.SendPhoneMsgProperties;
import com.remote.common.constant.CommonConstants;
import com.remote.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zsm
 * @date 2019/6/20 16:32
 * @description:
 */
@Service("sendSmsService")
public class SendSmsService {

    @Autowired
    private SendPhoneMsgProperties msgProperties;

    public R sendSmsSecurityCode(String phone,String templateCode,String code){
        R r = R.ok();
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(msgProperties.getDomain());
        request.setVersion(msgProperties.getVersion());
        request.setAction(msgProperties.getAction());
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        request.putQueryParameter("SignName", msgProperties.getSignName());
        try {
            CommonResponse response = getClient().getCommonResponse(request);
            if(response.getHttpStatus() == CommonConstants.HTTP_SUCESS_CODE){
                String data = response.getData();
                JSONObject json =  (JSONObject) JSONUtil.parse(data);
                if(json.getStr("Code").equals(CommonConstants.OK)){
                    return r;
                }else{
                    r = R.error().put("errorMsg",json).put("description","调用参数异常");
                }
            }else{
                r = R.error().put("errorMsg","httpStatus="+response.getHttpStatus()).put("description","短信服务器返回的httpStatus异常");
            }
        } catch (ServerException e) {
            e.printStackTrace();
            r = R.error().put("errorMsg",e.getMessage()).put("description","ServerExcetion 异常");
        } catch (ClientException e) {
            e.printStackTrace();
            r = R.error().put("errorMsg",e.getMessage()).put("description","ClientException 异常");
        }
        return r;
    }

    private IAcsClient getClient(){
        if(CommonConstants.cache.get("sendSmsService.IAcsClient")!=null){
            return (IAcsClient)CommonConstants.cache.get("sendSmsService.IAcsClient");
        }
        DefaultProfile profile = DefaultProfile.getProfile("default", msgProperties.getAccessKeyId(), msgProperties.getSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonConstants.cache.put("sendSmsService.IAcsClient",client);
        return client;
    }



}
