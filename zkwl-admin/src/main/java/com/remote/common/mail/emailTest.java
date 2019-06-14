package com.remote.common.mail;

import java.util.HashMap;
import java.util.Map;

public class emailTest {  
/*    public static void main(String[] args) {
    *//*	if(sendHtmlAttachmentMail())
    		System.out.println("俩类邮件均发送成功");*//*
    }*/
    /**
     * 普通文本文件
     * @return true 发送成功。false 发送失败
     */
    public static boolean sendCommonMail(String to, String title, String content){
        MailInfo mailInfo = new MailInfo();
        mailInfo.setToAddress(to);
        mailInfo.setContent(content);
        mailInfo.setSubject(title);
        return SimpleMail.sendTextMail(mailInfo);
    }

    /**
     * 模板邮件
     * @return
     */
    public static boolean sendHtmlAttachmentMail(){
        String result = "http://www.yuanfang.com:8080/remote-admin/login.html";
    	MailInfo mailInfo = new MailInfo();
        mailInfo.setToAddress("1648925727@qq.com");
        mailInfo.setSubject("密码找回邮件");

        StringBuffer demo = new StringBuffer();  
        demo.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">")  
        .append("<html>")  
        .append("<head>")  
        .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")  
        .append("<title>密码找回邮件</title>")
        .append("<style type=\"text/css\">")  
        .append(".test{font-family:\"Microsoft Yahei\";font-size: 18px;color: red;}")  
        .append("</style>")  
        .append("</head>")  
        .append("<body>")  
        .append("<span class=\"test\">找回密码的链接为:</span><a href='#{result}' target='_blank'>http://www.yuanfang.com:8080/remote-admin/login.html</a>该邮件三十分钟有效，请尽快处理")
        .append("</body>")  
        .append("</html>");  
        mailInfo.setContent(demo.toString());
        return SimpleMail.sendHtmlMail(mailInfo);
    }
}  
