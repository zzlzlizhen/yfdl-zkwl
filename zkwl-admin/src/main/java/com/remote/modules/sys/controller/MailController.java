package com.remote.modules.sys.controller;

import com.remote.common.config.SendEmailMsgProperties;
import com.remote.common.msg.SendEmailService;
import com.remote.common.msg.SendSmsService;
import com.remote.common.utils.DateUtils;
import com.remote.common.utils.R;
import com.remote.common.utils.StringUtils;
import com.remote.modules.sys.entity.SecurityEntity;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.MailService;
import com.remote.modules.sys.service.SecurityService;
import com.remote.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Security;
import java.util.*;

@Controller
@RequestMapping(value = "mail")
public class MailController extends AbstractController{
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    SecurityService securityService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    private SendEmailMsgProperties sendEmailMsgProperties;

    /**
     * 发送验证码并保存验证码到数据库中
     * */
    @RequestMapping("/sendBindEmail")
    @ResponseBody
    public R sendBindEmail(String email){
        String emailForm = "";
        String titleEmailBind="";
        String contentEmailBind="";
        String scurityCode = "";
        boolean isBind = checkBindEmail(email) ;
        if(isBind){
            return R.error("该邮箱已绑定");
        }
        /**
         * 发送人
         * */
        emailForm = sendEmailMsgProperties.getEmailFrom();
        /**
         * 接收人
         * */
       /* email = "1648925727@qq.com";*/
        /**
         * 发送邮件标题
         * */
        titleEmailBind = sendEmailMsgProperties.getTitleEmailBind();
        /**
         * 验证码
         * */
        scurityCode = StringUtils.getSecurityCode(6);
        /**
         * 发送邮件内容
         * */
        contentEmailBind = sendEmailMsgProperties.getContentEmailBindTemplate(scurityCode);
        R r  = sendEmailService.send(emailForm,email,titleEmailBind,contentEmailBind);
        if(!r.isOK()){
           return R.error("邮件发送失败");
        }
        /**
         * 邮件发送成功
         * */
        String content = getUser().getUsername()+titleEmailBind;
        SecurityEntity security = new SecurityEntity();
        security.setEmail(email);
        security.setCreateTime(new Date());
        security.setType("bindEmail");
        security.setSecurityCode(Long.parseLong(scurityCode));
        security.setContent(content);
        security.setUserId(getUserId());
        securityService.save(security);
        return R.ok();
    }
    /**
     * 判断验证码输入是否正确并把邮箱保存到用户表中
     * */
    @RequestMapping(value = "/checkScurityCode")
    public R checkScurityCode(String email,String securityCode){
        Long userId = getUserId();
        SecurityEntity securityEntity = securityService.querySecurity(email,securityCode,userId);
        if(securityEntity == null){
           return R.error("验证码输入错误");
        }
        /**
         * 更新邮箱到用户表中
         * */
        boolean flag = sysUserService.updateEmail(email,userId);
        if(!flag){
            return R.error("邮箱信息保存失败");
        }
        return R.ok();
    }
    //查看该邮箱是否绑定
    public boolean checkBindEmail(String email){
        boolean isBind = false;
        Long userId = getUserId();
        SysUserEntity userEntity = sysUserService.queryByEmailAndUid(email,userId);
        if(null != userEntity){
            isBind = true;
        }
        return isBind;
    }
}
