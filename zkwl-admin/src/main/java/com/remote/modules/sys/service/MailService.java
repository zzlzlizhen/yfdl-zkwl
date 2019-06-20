package com.remote.modules.sys.service;


public interface MailService {
    boolean sendSimpleMail(String to,String title,String content);
}
