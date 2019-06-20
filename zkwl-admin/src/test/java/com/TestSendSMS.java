package com;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.remote.common.utils.StringUtils;

public class TestSendSMS {
    public static void main(String[] args) {
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAIcvb6YcShaKlw", "hMH6YFEVwsz7ux8X1hFqNAah83HTxp");
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", "15810669164");
        request.putQueryParameter("TemplateCode", "SMS_168415387");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+ StringUtils.getSecurityCode(6) +"\"}");
        request.putQueryParameter("SignName", "远方动力");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}