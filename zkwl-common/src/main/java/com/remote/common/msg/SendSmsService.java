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
import com.remote.common.utils.R;
import org.springframework.stereotype.Service;

/**
 * @author zsm
 * @date 2019/6/20 16:32
 * @description:
 */
@Service("sendSmsService")
public class SendSmsService {

    public R sendSmsSecurityCode(SendPhoneSecurityCode sendPhoneSecurityCode){
        R r = R.ok();
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(sendPhoneSecurityCode.getDomain());
        request.setVersion(sendPhoneSecurityCode.getVersion());
        request.setAction(sendPhoneSecurityCode.getAction());
        request.putQueryParameter("PhoneNumbers", sendPhoneSecurityCode.getPhone());
        request.putQueryParameter("TemplateCode", sendPhoneSecurityCode.getTemplateCode());
        request.putQueryParameter("TemplateParam", "{\"code\":\""+sendPhoneSecurityCode.getSecurityCode()+"\"}");
        request.putQueryParameter("SignName", sendPhoneSecurityCode.getSignName());
        try {
            CommonResponse response = getClient(sendPhoneSecurityCode).getCommonResponse(request);
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

    private IAcsClient getClient(SendPhoneSecurityCode sendPhoneSecurityCode){
        if(CommonConstants.cache.get("sendSmsService.IAcsClient")!=null){
            return (IAcsClient)CommonConstants.cache.get("sendSmsService.IAcsClient");
        }
        DefaultProfile profile = DefaultProfile.getProfile("default", sendPhoneSecurityCode.getAccessKeyId(), sendPhoneSecurityCode.getSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonConstants.cache.put("sendSmsService.IAcsClient",client);
        return client;
    }



}
