package com.remote.modules.sys.controller;

import com.remote.common.config.SendEmailConfig;
import com.remote.common.config.SendEmailSecurityCode;
import com.remote.common.config.SendPhoneConfig;
import com.remote.common.config.SendPhoneSecurityCode;
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
    /**
     * 发送验证码并保存验证码到数据库中(綁定邮箱)
     * */
    @RequestMapping("/sendBindEmail")
    @ResponseBody
    public R sendBindEmail(String email){
        boolean isBind = checkBindEmail(email,"isEmail") ;
        if(isBind){
            return R.error("该邮箱已绑定");
        }
        String securityCode = StringUtils.getSecurityCode(6);
        SendEmailSecurityCode sendEmailSecurityCode = SendSecurityCodeUtils.buildSendEmailSecurityCode(sendEmailConfig,email,securityCode,"1");
        R r = sendEmailService.sendEmailSecurityCode(sendEmailSecurityCode);
        if(!r.isOK()){
            return R.error("邮件发送失败");
        }
        saveSecurity(email,"bindEmail",securityCode);
        return R.ok();
    }

    /**
     * 发送验证码到手机并把验证码保存到数据库
     * */
    @RequestMapping("/sendBindMobile")
    @ResponseBody
    public R sendBindSms(String mobile){
        boolean falg = checkBindEmail(mobile,"isMobile");
        String securityCode = StringUtils.getSecurityCode(6);
        if(falg){
            return R.error("该手机号已绑定");
        }
        SendPhoneSecurityCode sendPhoneSecurityCode = SendSecurityCodeUtils.bulidSendPhoneSecurityCode(sendPhoneConfig,mobile,securityCode,"1");
        R r = sendSmsService.sendSmsSecurityCode(sendPhoneSecurityCode);
        if(!r.isOK()){
            R.error("手机发送失败");
        }
        saveSecurity(mobile,"bindMobile",securityCode);
        return R.ok();
    }
    /**
     * 绑定手机
     * */
    @RequestMapping("/checkBindMobile")
    @ResponseBody
    public R checkBindMobile(String mobile,String securityCode){
        checkEmailAndMob(mobile,securityCode,"isMobile","bindMobile");
        return R.ok();
    }
    /**
     * 找回密码
     * */
    @RequestMapping("/checkRePwdMobile")
    @ResponseBody
    public R checkRePwdMobile(String mobile,String securityCode){
        checkEmailAndMob(mobile,securityCode,"isMobile","rePwdMobile");
        return R.ok();
    }
    /**
     * 绑定邮箱
     * */
    @RequestMapping(value="/checkBindEmail")
    @ResponseBody
    public R checkSBindEmail(String email,String securityCode){
        checkEmailAndMob(email,securityCode,"isEmail","bindEmail");
        return R.ok();
    }
    /**
     * 找回密码
     * */
    @RequestMapping(value="/checkRePwdEmail")
    @ResponseBody
    public R checkRePwdEmail(String email,String securityCode){
        checkEmailAndMob(email,securityCode,"isEmail","rePwdEmail");
        return R.ok();
    }
    /**
     * 通过邮箱加类型发送验证码 并保存到数据库中
     * */
    @RequestMapping(value = "/sendForPwd")
    public R sendRePwd(String contact,String fPwdType){
        /**
         * 邮箱发送验证码 找回密码
         * */
        if("isEmail".equals(fPwdType)){//1邮箱忘记密码
            boolean flag = checkBindEmail(contact,"isEmail");
            if(!flag){
                return R.error("该邮箱不存在");
            }
            String securityCode = StringUtils.getSecurityCode(6);

            SendEmailSecurityCode sendEmailSecurityCode = SendSecurityCodeUtils.buildSendEmailSecurityCode(sendEmailConfig,contact,securityCode,"2");
            R r = sendEmailService.sendEmailSecurityCode(sendEmailSecurityCode);
            if(!r.isOK()){
                return R.error("邮件发送失败");
            }
            saveSecurity(contact,"rePwdEmail",securityCode);
            return R.ok();
        }else{
            //如果忘记密码类型为手机忘记密码
            boolean flag = checkBindEmail(contact,"isMobile");
            if(!flag){
                R.error("手机号不存在");
            }
            String securityCode = StringUtils.getSecurityCode(6);
            SendPhoneSecurityCode sendPhoneSecurityCode = SendSecurityCodeUtils.bulidSendPhoneSecurityCode(sendPhoneConfig,contact,securityCode,"2");
            R r = sendSmsService.sendSmsSecurityCode(sendPhoneSecurityCode);
            if(!r.isOK()){
                R.error("手机发送失败");
            }
            saveSecurity(contact,"rePwdMobile",securityCode);
            return R.ok();

        }
    }

    /**
     *保存验证码
     */
    public void saveSecurity(String contact,String securityCode,String sendSecurityType){
        SecurityEntity securityEntity = new SecurityEntity();
        String content="";
        if("bindEmail".equals(sendSecurityType)){//绑定邮箱
            securityEntity.setType("bindEmail");
            securityEntity.setEmail(contact);
            content = getUser().getUsername() + "绑定邮箱";

        }else if("rePwdEmail".equals(sendSecurityType)){//邮箱找回密码
            securityEntity.setType("rePwdEmail");
            securityEntity.setEmail(contact);
            content = getUser().getUsername() + "邮箱找回密码";

        }else if("bindMobile".equals(sendSecurityType)){//绑定手机
            securityEntity.setType("bindMobile");
            content = getUser().getUsername() + "绑定手机";
            securityEntity.setPhone(contact);
        }else if("rePwdMobile".equals(sendSecurityType)){//手机号找回密码
            securityEntity.setType("rePwdMobile");
            content = getUser().getUsername() + "手机找回密码";
            securityEntity.setPhone(contact);
        }
        securityEntity.setContent(content);
        securityEntity.setUserId(getUserId());
        securityEntity.setCreateTime(new Date());
        securityEntity.setSecurityCode(securityCode);
        securityService.save(securityEntity);
    }

    //查看该邮箱(手机号)是否存在(綁定邮箱)
    public boolean checkBindEmail(String contact,String isEmailAndMob){
        boolean isExist = false;
        SysUserEntity userEntity = null;
        if("isEmail".equals(isEmailAndMob)){//0是邮箱
            userEntity = sysUserService.queryByEmailAndUid(contact,getUserId());
            if(null != userEntity){
                isExist = true;
            }
        }else if("isMobile".equals(isEmailAndMob)){//1是手机号
            userEntity = sysUserService.queryBySmsAndUid(contact,getUserId());
            if(userEntity != null){
                isExist = true;
            }
        }
        return isExist;
    }
    /**
     * 检查手机号，邮箱，验证码输入是否正确 是绑定，保存数据库，如果是发送验证码，不做处理
     * */
    public R checkEmailAndMob(String contact,String securityCode,String isEmailAndMobile,String isType){
        SecurityEntity security =null;
        if("isEmail".equals(isEmailAndMobile)){//0代表邮箱
            //查询邮箱验证码是否正确
            security = securityService.querySecurity(contact,securityCode,getUserId());
            if (security == null){
                return R.error("验证码错误");
            }
            if("bindEmail".equals(isType)){ //绑定邮箱
                /**
                 * 更新邮箱到用户表中
                 * */
                boolean flag = sysUserService.updateEmail(contact,getUserId());
                if(!flag){
                    return R.error("邮箱信息保存失败");
                }
            }
        }else if ("isMobile".equals(isEmailAndMobile)){ //1代表手机号
            //查询手机号验证码是否正确
            security = securityService.querySmsSecurity(contact,securityCode,getUserId());
            if (security == null){
                return R.error("验证码错误");
            }
            if("bindMobile".equals(isType)){
                boolean isBind = sysUserService.updateMobile(contact,getUserId());
                if(!isBind){
                    return R.error("手机号保存失败");
                }
            }
        }
        return R.ok();
    }
}
