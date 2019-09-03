package com.remote;

import com.alibaba.fastjson.JSON;
import com.remote.common.config.SendEmailSecurityCode;
import com.remote.common.config.SendPhoneSecurityCode;
import com.remote.common.msg.SendEmailService;
import com.remote.common.msg.SendSmsService;
import com.remote.common.utils.R;
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
    private SendEmailService sendEmailService;

    @Test
    public void testSendPhoneAndEmail(){
        this.testSendBindEmail();
        this.testSendBindPhone();
        this.testSendForgotPasswordPhone();
        this.testSendForgotPasswordEmail();
    }

    @Test
    public void testSendBindPhone(){
        SendPhoneSecurityCode sendPhoneSecurityCode = new SendPhoneSecurityCode();
        //sendPhoneSecurityCode需要自己设置相关的属性值
        R r = sendSmsService.sendSmsSecurityCode(sendPhoneSecurityCode);
        System.out.println(JSON.toJSONString(r));
    }

    @Test
    public void testSendForgotPasswordPhone(){
        SendPhoneSecurityCode sendPhoneSecurityCode = new SendPhoneSecurityCode();
        //sendPhoneSecurityCode需要自己设置相关的属性值
        R r  = sendSmsService.sendSmsSecurityCode(sendPhoneSecurityCode);
        System.out.println(JSON.toJSONString(r));
    }

    @Test
    public void testSendBindEmail(){
        SendEmailSecurityCode sendEmailSecurityCode = new SendEmailSecurityCode();
        //sendEmailSecurityCode需要自己设置相关的属性值
        R r = null;
        try {
            r = sendEmailService.sendEmailSecurityCode(sendEmailSecurityCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(r));
    }

    @Test
    public void testSendForgotPasswordEmail(){
        SendEmailSecurityCode sendEmailSecurityCode = new SendEmailSecurityCode();
        //sendEmailSecurityCode需要自己设置相关的属性值
        R r = null;
        try {
            r = sendEmailService.sendEmailSecurityCode(sendEmailSecurityCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
