package com.remote.modules.sys.controller;

import com.remote.common.config.SendEmailConfig;
import com.remote.common.config.SendEmailSecurityCode;
import com.remote.common.config.SendPhoneConfig;
import com.remote.common.config.SendPhoneSecurityCode;
import com.remote.common.errorcode.ErrorCode;
import com.remote.common.errorcode.ErrorMsg;
import com.remote.common.msg.SendEmailService;
import com.remote.common.msg.SendSmsService;
import com.remote.common.utils.R;
import com.remote.common.utils.SendSecurityCodeUtils;
import com.remote.common.utils.StringUtils;
import com.remote.modules.sys.entity.SecurityEntity;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SecurityService;
import com.remote.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 绑定邮箱功能
 */
@Controller
@RequestMapping(value = "contact")
public class MailController extends AbstractController{
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    SecurityService securityService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    private SendSmsService sendSmsService;
    @Autowired
    private SendEmailConfig sendEmailConfig;

    @Autowired
    private SendPhoneConfig sendPhoneConfig;
   Class mc = MailController.class;
    /**
     * 发送验证码并保存验证码到数据库中(綁定邮箱)
     * */
    @RequestMapping("/sendBindEmail")
    @ResponseBody
    public R sendBindEmail(String email){
        if(StringUtils.isBlank(email)){
            return ErrorMsg.errorMsg(mc,ErrorCode.NOT_EMPTY,"邮箱号不能为空");
        }
        boolean isBind = checkBindEmail(email,"isEmail") ;
        if(isBind){
            return ErrorMsg.errorMsg(mc,ErrorCode.DATA_QUERY_CHECKING,"该邮箱已绑定");
        }
        String securityCode = StringUtils.getSecurityCode(6);
        SendEmailSecurityCode sendEmailSecurityCode = SendSecurityCodeUtils.buildSendEmailSecurityCode(sendEmailConfig,email,securityCode,"1");
        try {
            R r = sendEmailService.sendEmailSecurityCode(sendEmailSecurityCode);
            if(!r.isOK()){
                return ErrorMsg.errorMsg(mc,ErrorCode.FAIL,"邮件发送失败");
            }
        }catch (Exception e){

        }

        if(StringUtils.isBlank(securityCode)){
            return ErrorMsg.errorMsg(mc,ErrorCode.NOT_EMPTY,"验证码不能为空");
        }
        boolean falg = saveSecurity(email,"bindEmail",securityCode);
        if(!falg){
            return ErrorMsg.errorMsg(mc,ErrorCode.FAIL,"邮箱验证码保存失败");
        }
        return R.ok();
    }

    /**
     * 发送验证码到手机并把验证码保存到数据库
     * */
    @RequestMapping("/sendBindMobile")
    @ResponseBody
    public R sendBindSms(String mobile){
        if(StringUtils.isBlank(mobile)){
            return ErrorMsg.errorMsg(mc,ErrorCode.NOT_EMPTY,"手机号不能为空");
        }
        boolean falg = checkBindEmail(mobile,"isMobile");
        String securityCode = StringUtils.getSecurityCode(6);
        if(falg){
            return ErrorMsg.errorMsg(mc,ErrorCode.DATA_QUERY_CHECKING,"该手机号已绑定");
        }
        SendPhoneSecurityCode sendPhoneSecurityCode = SendSecurityCodeUtils.bulidSendPhoneSecurityCode(sendPhoneConfig,mobile,securityCode,"1");
        R r = sendSmsService.sendSmsSecurityCode(sendPhoneSecurityCode);
        if(!r.isOK()){
            return ErrorMsg.errorMsg(mc,ErrorCode.DATA_QUERY_CHECKING,"手机发送失败");
        }
        boolean flag = saveSecurity(mobile,"bindMobile",securityCode);
        if(!flag){
            return ErrorMsg.errorMsg(mc,ErrorCode.FAIL,"手机验证码保存失败");
        }
        return R.ok();
    }
    /**
     * 绑定手机
     * */
    @RequestMapping("/checkBindMobile")
    @ResponseBody
    public R checkBindMobile(String mobile,String securityCode){
        if(StringUtils.isBlank(mobile)||StringUtils.isBlank(securityCode)){
            return ErrorMsg.errorMsg(mc,ErrorCode.NOT_EMPTY,"手机号或验证码不能为空");
        }
        return checkEmailAndMob(mobile,securityCode,"isMobile","bindMobile");

    }

    /**
     * 绑定邮箱
     * */
    @RequestMapping(value="/checkBindEmail")
    @ResponseBody
    public R checkSBindEmail(String email,String securityCode){
        if(StringUtils.isBlank(email)||StringUtils.isBlank(securityCode)){
            return ErrorMsg.errorMsg(mc,ErrorCode.NOT_EMPTY,"邮箱或验证码不能为空");
        }
        return checkEmailAndMob(email,securityCode,"isEmail","bindEmail");

    }
    /**
     *保存验证码
     */
    public boolean saveSecurity(String contact,String sendSecurityType,String securityCode){
        SecurityEntity securityEntity = new SecurityEntity();
        String content="";
        if("bindEmail".equals(sendSecurityType)){//绑定邮箱
            securityEntity.setType("bindEmail");
            securityEntity.setEmail(contact);
            content = getUser().getUsername() + "绑定邮箱";
        }else if("bindMobile".equals(sendSecurityType)){//绑定手机
            securityEntity.setType("bindMobile");
            content = getUser().getUsername() + "绑定手机";
            securityEntity.setPhone(contact);
        }
        securityEntity.setContent(content);
        securityEntity.setUserId(getUserId());
        securityEntity.setCreateTime(new Date());
        securityEntity.setSecurityCode(securityCode);
        return securityService.save(securityEntity);
    }

    //查看该邮箱(手机号)是否存在(綁定邮箱)
    public boolean checkBindEmail(String contact,String isEmailAndMob){
        boolean isExist = false;
        SysUserEntity userEntity = null;
        if("isEmail".equals(isEmailAndMob)){//0是邮箱
            try {
                userEntity = sysUserService.queryByEmailAndUid(contact,getUserId());
                if(null != userEntity){
                    isExist = true;
                }
            }catch (Exception e){
                e.printStackTrace();
                ErrorMsg.errorMsg(mc,ErrorCode.ABNORMAL,"邮箱查询异常");

            }

        }else if("isMobile".equals(isEmailAndMob)){//1是手机号
            try {
                userEntity = sysUserService.queryBySmsAndUid(contact,getUserId());
                if(userEntity != null){
                    isExist = true;
                }
            }catch (Exception e){
                e.printStackTrace();
                ErrorMsg.errorMsg(mc,ErrorCode.ABNORMAL,"手机号查询异常");
            }

        }
        return isExist;
    }
    /**
     * 检查手机号，邮箱，验证码输入是否正确 是绑定，保存数据库，如果是发送验证码，不做处理
     * */
    public R checkEmailAndMob(String contact,String securityCode,String isEmailAndMobile,String isType){
        if(StringUtils.isBlank(contact)){
            return ErrorMsg.errorMsg(mc,ErrorCode.NOT_EMPTY,"联系方式不能为空");
        }
        if(StringUtils.isBlank(securityCode)){
            return ErrorMsg.errorMsg(mc,ErrorCode.NOT_EMPTY,"验证码不能为空");
        }
        if(StringUtils.isBlank(isType)){
            return ErrorMsg.errorMsg(mc,ErrorCode.NOT_EMPTY,"联系方式类型不能为空");
        }
        SecurityEntity security =null;
        if("isEmail".equals(isEmailAndMobile)){//0代表邮箱
            //查询邮箱验证码是否正确
            security = securityService.queryEmailSecurity(contact,securityCode,getUserId());
            if (security == null){
                return ErrorMsg.errorMsg(mc,ErrorCode.DATA_QUERY_CHECKING,"邮箱验证码错误");
            }
            if("bindEmail".equals(isType)){ //绑定邮箱
                /**
                 * 更新邮箱到用户表中
                 * */
                boolean flag = sysUserService.updateEmail(contact,getUserId());
                if(!flag){
                    return ErrorMsg.errorMsg(mc,ErrorCode.DATA_QUERY_CHECKING,"邮箱信息保存失败");
                }
            }
        }else if ("isMobile".equals(isEmailAndMobile)){ //1代表手机号
            //查询手机号验证码是否正确
            security = securityService.querySmsSecurity(contact,securityCode,getUserId());
            if (security == null){
                return ErrorMsg.errorMsg(mc,ErrorCode.DATA_QUERY_CHECKING,"手机验证码错误");
            }
            if("bindMobile".equals(isType)){
                boolean isBind = sysUserService.updateMobile(contact,getUserId());
                if(!isBind){
                    return ErrorMsg.errorMsg(mc,ErrorCode.DATA_QUERY_CHECKING,"手机号保存失败");
                }
            }
        }
        return R.ok();
    }
}
