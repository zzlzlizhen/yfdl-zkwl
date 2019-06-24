package com.remote.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zsm
 * @date 2019/6/24 17:25
 * @description:
 */
@Component
@ConfigurationProperties(prefix="sms")
@PropertySource(value = {"classpath:sms-info.properties"}, encoding = "utf-8")
public class SendPhoneConfig{

    private String accessKeyId;

    private String secret;

    private String domain;

    private String signName;

    private String action;

    private String version;

    private String forgotPasswordTemplateCode;

    private String bindPhoneTemplateCode;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getForgotPasswordTemplateCode() {
        return forgotPasswordTemplateCode;
    }

    public void setForgotPasswordTemplateCode(String forgotPasswordTemplateCode) {
        this.forgotPasswordTemplateCode = forgotPasswordTemplateCode;
    }

    public String getBindPhoneTemplateCode() {
        return bindPhoneTemplateCode;
    }

    public void setBindPhoneTemplateCode(String bindPhoneTemplateCode) {
        this.bindPhoneTemplateCode = bindPhoneTemplateCode;
    }
}
