package com.remote.common.config;

/**
 * @author zsm
 * @date 2019/6/20 19:21
 * @description:
 */

public class SendEmailSecurityCode {

    private String emailFrom;

    private String emailTo;

    private String titleEmail;

    private String contentEmailTemplate;

    private String securityCode;

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getTitleEmail() {
        return titleEmail;
    }

    public void setTitleEmail(String titleEmail) {
        this.titleEmail = titleEmail;
    }

    public String getContentEmailTemplate() {
        return contentEmailTemplate;
    }

    public void setContentEmailTemplate(String contentEmailTemplate) {
        this.contentEmailTemplate = contentEmailTemplate;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
}
