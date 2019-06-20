package com.remote.common.msg;

import com.remote.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * @author zsm
 * @date 2019/6/20 19:09
 * @description:
 */
@Service("sendEmailService")
public class SendEmailService {

    @Autowired
    private MailSender mailSender;

    public R send(String phoneFrom,String phoneTo,String title,String content){
        R r = null;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(phoneFrom);
            message.setTo(phoneTo);
            message.setSubject(title);
            message.setText(content);
            mailSender.send(message);
            r = R.ok();
        }catch (Exception e){
            e.printStackTrace();
            r = R.error().put("errorMsg",e.getMessage());
        }
        return r;
    }
}
