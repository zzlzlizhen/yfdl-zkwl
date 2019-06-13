package com.remote.common.mail;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class SimpleMail {  

	public static MailInfo initMailInfo(MailInfo mailInfo){
		Properties prop = new Properties();
		try {
			String filePath = Class.class.getResource("/").getPath()+"mail-info.properties";
			FileInputStream fis = new FileInputStream(filePath);
			prop.load(fis);
			mailInfo.setValidate("1".equals(prop.getProperty("validate")));
			if(mailInfo.getMailServerHost()==null)
				mailInfo.setMailServerHost(prop.getProperty("mailServerHost"));
			if(mailInfo.getMailServerPort()==null)
				mailInfo.setMailServerPort(prop.getProperty("mailServerPort"));
			if(mailInfo.getUsername()==null)
				mailInfo.setUsername(prop.getProperty("username"));
			if(mailInfo.getPassword()==null)
				mailInfo.setPassword(prop.getProperty("password"));
			if(mailInfo.getFromAddress()==null)
				mailInfo.setFromAddress(prop.getProperty("fromAddress"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailInfo;
	}
    // 以文本格式发送邮件
    public static boolean sendTextMail(MailInfo mailInfo) {
    	mailInfo = initMailInfo(mailInfo);
    	//判断是否需要身份认证  
        MyAuthenticator authenticator = null;
        Properties properties = mailInfo.getProperties();  
        if (mailInfo.isValidate()) {  
            //如果需要身份认证，则创建一个密码验证器  
            authenticator = new MyAuthenticator(mailInfo.getUsername(), mailInfo.getPassword());
        }  
          
        //根据邮件会话属性和密码验证器构造一个发送邮件的session  
        Session sendMailSession = Session.getDefaultInstance(properties, authenticator);
        try {  
            Message mailMessage = new MimeMessage(sendMailSession);//根据session创建一个邮件消息
            Address from = new InternetAddress(mailInfo.getFromAddress());//创建邮件发送者地址
            mailMessage.setFrom(from);//设置邮件消息的发送者  
            Address to = new InternetAddress(mailInfo.getToAddress());//创建邮件的接收者地址
            mailMessage.setRecipient(Message.RecipientType.TO, to);//设置邮件消息的接收者
            mailMessage.setSubject(mailInfo.getSubject());//设置邮件消息的主题  
            mailMessage.setSentDate(new Date());//设置邮件消息发送的时间  
            //mailMessage.setText(mailInfo.getContent());//设置邮件消息的主要内容  
              
            //MimeMultipart类是一个容器类，包含MimeBodyPart类型的对象  
            Multipart mainPart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();//创建一个包含附件内容的MimeBodyPart
            //设置HTML内容  
            messageBodyPart.setContent(mailInfo.getContent(),"text/html; charset=utf-8");  
            mainPart.addBodyPart(messageBodyPart);  
  
            //存在附件  
            String[] filePaths = mailInfo.getAttachFileNames();  
            if (filePaths != null && filePaths.length > 0) {  
                for(String filePath:filePaths){  
                    messageBodyPart = new MimeBodyPart();
                    File file = new File(filePath);   
                    if(file.exists()){//附件存在磁盘中  
                        FileDataSource fds = new FileDataSource(file);//得到数据源  
                        messageBodyPart.setDataHandler(new DataHandler(fds));//得到附件本身并至入BodyPart  
                        messageBodyPart.setFileName(file.getName());//得到文件名同样至入BodyPart  
                        mainPart.addBodyPart(messageBodyPart);  
                    }  
                }  
            }  
              
            //将MimeMultipart对象设置为邮件内容     
            mailMessage.setContent(mainPart);  
            Transport.send(mailMessage);//发送邮件
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();    
        }  
        return false;  
    }     
      
    // 以HTML格式发送邮件   
    public static boolean sendHtmlMail(MailInfo mailInfo) {
    	mailInfo = initMailInfo(mailInfo);
    	//判断是否需要身份认证  
        MyAuthenticator authenticator = null;
        Properties properties = mailInfo.getProperties();  
        if (mailInfo.isValidate()) {  
            // 如果需要身份认证，则创建一个密码验证器  
            authenticator = new MyAuthenticator(mailInfo.getUsername(), mailInfo.getPassword());
        }  
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session  
        Session sendMailSession = Session.getDefaultInstance(properties, authenticator);
        try{  
            Message mailMessage = new MimeMessage(sendMailSession);//根据session创建一个邮件消息
            Address from = new InternetAddress(mailInfo.getFromAddress());//创建邮件发送者地址
            mailMessage.setFrom(from);//设置邮件消息的发送者  
            Address to = new InternetAddress(mailInfo.getToAddress());//创建邮件的接收者地址
            mailMessage.setRecipient(Message.RecipientType.TO, to);//设置邮件消息的接收者
            mailMessage.setSubject(mailInfo.getSubject());//设置邮件消息的主题  
            mailMessage.setSentDate(new Date());//设置邮件消息发送的时间  
              
            //MimeMultipart类是一个容器类，包含MimeBodyPart类型的对象  
            Multipart mainPart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();//创建一个包含HTML内容的MimeBodyPart
            //设置HTML内容  
            messageBodyPart.setContent(mailInfo.getContent(),"text/html; charset=utf-8");  
            mainPart.addBodyPart(messageBodyPart);  
            
            //HTML中图片文件
            Map<String,String> maps = mailInfo.getHtmlIdFileName();
            if(maps!=null){
            	for(Entry<String,String> e:maps.entrySet()){
            		messageBodyPart = new MimeBodyPart();
            		FileDataSource fds = new FileDataSource(e.getValue());
            		messageBodyPart.setDataHandler(new DataHandler(fds));//得到附件本身并至入BodyPart  
                    messageBodyPart.setHeader("Content-ID",e.getKey());
                    mainPart.addBodyPart(messageBodyPart); 
            	}
            }
            
            //存在附件  
            String[] filePaths = mailInfo.getAttachFileNames();  
            if (filePaths != null && filePaths.length > 0) {  
                for(String filePath:filePaths){  
                    messageBodyPart = new MimeBodyPart();
                    File file = new File(filePath);   
                    if(file.exists()){//附件存在磁盘中  
                        FileDataSource fds = new FileDataSource(file);//得到数据源  
                        messageBodyPart.setDataHandler(new DataHandler(fds));//得到附件本身并至入BodyPart  
                        messageBodyPart.setFileName(file.getName());//得到文件名同样至入BodyPart  
                        mainPart.addBodyPart(messageBodyPart);  
                    }  
                }  
            }  
              
            //将MimeMultipart对象设置为邮件内容     
            mailMessage.setContent(mainPart);  
            Transport.send(mailMessage);//发送邮件
            return true;  
        }catch (Exception e) {  
            e.printStackTrace();  
        }  
        return false;  
    }  
}  
