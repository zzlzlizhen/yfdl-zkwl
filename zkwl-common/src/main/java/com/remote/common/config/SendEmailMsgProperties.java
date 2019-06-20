package com.remote.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zsm
 * @date 2019/6/20 19:21
 * @description:
 */
@Component
@ConfigurationProperties(prefix="mail")
@PropertySource(value = {"classpath:mail-info.properties"},encoding = "utf-8")
public class SendEmailMsgProperties {

    private String phoneFrom;

    private String titleEmailBind;

    private String contentEmailBindTemplate;

    private String titleEmailForgotPassword;

    private String contentEmailForgotPasswordTemplate;

    public String getPhoneFrom() {
        return phoneFrom;
    }

    public void setPhoneFrom(String phoneFrom) {
        this.phoneFrom = phoneFrom;
    }

    public String getTitleEmailBind() {
        return titleEmailBind;
    }

    public void setTitleEmailBind(String titleEmailBind) {
        this.titleEmailBind = titleEmailBind;
    }

    public String getContentEmailBindTemplate(String code) {
        return contentEmailBindTemplate.replace("code",code);
    }

    public void setContentEmailBindTemplate(String contentEmailBindTemplate) {
        this.contentEmailBindTemplate = contentEmailBindTemplate;
    }

    public String getTitleEmailForgotPassword() {
        return titleEmailForgotPassword;
    }

    public void setTitleEmailForgotPassword(String titleEmailForgotPassword) {
        this.titleEmailForgotPassword = titleEmailForgotPassword;
    }

    public String getContentEmailForgotPasswordTemplate(String code) {
        return contentEmailForgotPasswordTemplate.replace("code",code);
    }

    public void setContentEmailForgotPasswordTemplate(String contentEmailForgotPasswordTemplate) {
        this.contentEmailForgotPasswordTemplate = contentEmailForgotPasswordTemplate;
    }
}
