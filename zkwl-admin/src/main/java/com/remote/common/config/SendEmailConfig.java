package com.remote.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zsm
 * @date 2019/6/24 17:26
 * @description:
 */
@Component
@ConfigurationProperties(prefix="mail")
@PropertySource(value = {"classpath:mail-info.properties"},encoding = "utf-8")
public class SendEmailConfig{
    private String emailFrom;

    private String titleEmailBind;

    private String contentEmailBindTemplate;

    private String titleEmailForgotPassword;

    private String contentEmailForgotPasswordTemplate;

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getTitleEmailBind() {
        return titleEmailBind;
    }

    public void setTitleEmailBind(String titleEmailBind) {
        this.titleEmailBind = titleEmailBind;
    }

    public String getContentEmailBindTemplate() {
        return contentEmailBindTemplate;
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

    public String getContentEmailForgotPasswordTemplate() {
        return contentEmailForgotPasswordTemplate;
    }

    public void setContentEmailForgotPasswordTemplate(String contentEmailForgotPasswordTemplate) {
        this.contentEmailForgotPasswordTemplate = contentEmailForgotPasswordTemplate;
    }
}
