package com.remote;

import com.alibaba.fastjson.JSON;
import com.remote.common.config.SendEmailMsgProperties;
import com.remote.common.config.SendPhoneMsgProperties;
import com.remote.common.msg.SendEmailService;
import com.remote.common.msg.SendSmsService;
import com.remote.common.utils.R;
import com.remote.common.utils.StringUtils;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestServiceMethod {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SendSmsService sendSmsService;

    @Autowired
    private SendPhoneMsgProperties sendPhoneMsgProperties;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private SendEmailMsgProperties sendEmailMsgProperties;

    @Test
    public void testSendPhoneAndEmail(){
        this.testSendBindEmail();
        this.testSendBindPhone();
        this.testSendForgotPasswordPhone();
        this.testSendForgotPasswordEmail();
    }

    @Test
    public void testSendBindPhone(){
       R r = sendSmsService.sendSmsSecurityCode("15810669164",sendPhoneMsgProperties.getBindPhoneTemplateCode(), StringUtils.getSecurityCode(6));
       System.out.println(JSON.toJSONString(r));
    }

    @Test
    public void testSendForgotPasswordPhone(){
        R r  = sendSmsService.sendSmsSecurityCode("15810669164",sendPhoneMsgProperties.getForgotPasswordTemplateCode(),StringUtils.getSecurityCode(6));
        System.out.println(JSON.toJSONString(r));
    }

    @Test
    public void testSendBindEmail(){
        R r = sendEmailService.send(sendEmailMsgProperties.getPhoneFrom(),"1648925727@qq.com",sendEmailMsgProperties.getTitleEmailBind(),sendEmailMsgProperties.getContentEmailBindTemplate(StringUtils.getSecurityCode(6)));
        System.out.println(JSON.toJSONString(r));
    }

    @Test
    public void testSendForgotPasswordEmail(){
        R r = sendEmailService.send(sendEmailMsgProperties.getPhoneFrom(),"1648925727@qq.com",sendEmailMsgProperties.getTitleEmailForgotPassword(),sendEmailMsgProperties.getContentEmailForgotPasswordTemplate(StringUtils.getSecurityCode(6)));
        System.out.println(JSON.toJSONString(r));
    }

  @Test
    public void test1(){
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUserId(1L);
        sysUserEntity.setParentId(1L);
        sysUserEntity.setAllParentId("1");
        List<SysUserEntity> sysUserEntityList = sysUserService.queryChild(sysUserEntity);
        System.out.println("----------------------------------------------------");
        System.out.println(JSON.toJSONString(sysUserEntityList));
        System.out.println("----------------------------------------------------");
    }

    @Test
    public void test2(){
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUserId(1L);
        sysUserEntity.setParentId(1L);
        sysUserEntity.setAllParentId("1");
        List<SysUserEntity> sysUserEntityList = sysUserService.queryAllChild(sysUserEntity);
        System.out.println("----------------------------------------------------");
        System.out.println(JSON.toJSONString(sysUserEntityList));
        System.out.println("----------------------------------------------------");
    }
}
