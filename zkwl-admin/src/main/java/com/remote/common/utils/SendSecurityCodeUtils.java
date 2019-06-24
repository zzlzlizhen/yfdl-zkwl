package com.remote.common.utils;

import com.remote.common.config.SendEmailConfig;
import com.remote.common.config.SendEmailSecurityCode;
import com.remote.common.config.SendPhoneConfig;
import com.remote.common.config.SendPhoneSecurityCode;

/**
 * @author zsm
 * @date 2019/6/24 18:07
 * @description:
 */
public class SendSecurityCodeUtils {

    /**
     * 功能描述:组装发送邮件验证码的实体对象
     * 1、绑定时发生验证码。2、找回密码时发生验证码
     * @author zsm
     * @date 2019/6/24 18:13
     * @param
     */
    public static SendEmailSecurityCode buildSendEmailSecurityCode(SendEmailConfig sendEmailConfig,String emailTo,String securityCode,String type){
        SendEmailSecurityCode sendEmailSecurityCode = new SendEmailSecurityCode();
        sendEmailSecurityCode.setEmailFrom(sendEmailConfig.getEmailFrom());
        sendEmailSecurityCode.setEmailTo(emailTo);
        sendEmailSecurityCode.setSecurityCode(securityCode);
        if(type.equals("1")){
            sendEmailSecurityCode.setTitleEmail(sendEmailConfig.getTitleEmailBind());
            sendEmailSecurityCode.setContentEmailTemplate(sendEmailConfig.getContentEmailBindTemplate());
        }else if (type.equals("2")){
            sendEmailSecurityCode.setTitleEmail(sendEmailConfig.getTitleEmailForgotPassword());
            sendEmailSecurityCode.setContentEmailTemplate(sendEmailConfig.getContentEmailForgotPasswordTemplate());
        }else{
            return null;
        }
        return sendEmailSecurityCode;
    }

    /**
     * 功能描述:组装发送手机短信验证码的实体对象
     * 1、绑定时发生验证码。2、找回密码时发生验证码
     * @author zsm
     * @date 2019/6/24 18:13
     * @param
     */
    public static SendPhoneSecurityCode bulidSendPhoneSecurityCode(SendPhoneConfig sendPhoneConfig,String phoneTo,String securityCode,String type){
        SendPhoneSecurityCode sendPhoneSecurityCode = new SendPhoneSecurityCode();
        sendPhoneSecurityCode.setAccessKeyId(sendPhoneConfig.getAccessKeyId());
        sendPhoneSecurityCode.setAction(sendPhoneConfig.getAction());
        sendPhoneSecurityCode.setDomain(sendPhoneConfig.getDomain());
        sendPhoneSecurityCode.setSecret(sendPhoneConfig.getSecret());
        sendPhoneSecurityCode.setSignName(sendPhoneConfig.getSignName());
        sendPhoneSecurityCode.setVersion(sendPhoneConfig.getVersion());

        sendPhoneSecurityCode.setPhone(phoneTo);
        sendPhoneSecurityCode.setSecurityCode(securityCode);
        if(type.equals("1")){
            sendPhoneSecurityCode.setTemplateCode(sendPhoneConfig.getBindPhoneTemplateCode());
        }else if (type.equals("2")){
            sendPhoneSecurityCode.setTemplateCode(sendPhoneConfig.getForgotPasswordTemplateCode());
        }else{
            return null;
        }
        return sendPhoneSecurityCode;
    }
}
