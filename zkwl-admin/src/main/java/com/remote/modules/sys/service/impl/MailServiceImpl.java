package com.remote.modules.sys.service.impl;

import com.remote.common.mail.MailInfo;
import com.remote.common.mail.SimpleMail;
import com.remote.modules.sys.service.MailService;
import org.springframework.stereotype.Service;

@Service("mailService")
public class MailServiceImpl implements MailService{
    @Override
    public boolean sendSimpleMail(String to, String title, String content) {
        MailInfo mailInfo = new MailInfo();
        mailInfo.setToAddress(to);
        mailInfo.setContent(content);
        mailInfo.setSubject(title);
        return SimpleMail.sendTextMail(mailInfo);
    }
}
