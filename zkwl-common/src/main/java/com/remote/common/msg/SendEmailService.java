package com.remote.common.msg;

import com.remote.common.config.SendEmailSecurityCode;
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

    public R sendEmailSecurityCode(SendEmailSecurityCode sendEmailSecurityCode){
        R r = null;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sendEmailSecurityCode.getEmailFrom());
            message.setTo(sendEmailSecurityCode.getEmailTo());
            message.setSubject(sendEmailSecurityCode.getTitleEmail());
            message.setText(sendEmailSecurityCode.getContentEmailTemplate().replace("code",sendEmailSecurityCode.getSecurityCode()));
            mailSender.send(message);
            r = R.ok();
        }catch (Exception e){
            e.printStackTrace();
            r = R.error().put("errorMsg",e.getMessage());
        }
        return r;
    }
}
