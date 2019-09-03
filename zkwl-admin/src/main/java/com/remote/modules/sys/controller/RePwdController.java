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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 找回密码绑定邮箱
 */
@RestController
@RequestMapping(value = "/repwd")
public class RePwdController {
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
    private Class rpc = RePwdController.class;
    /**
     * 找回密码
     * */
    @RequestMapping(value = "/checkRePwdMobile",method = RequestMethod.GET)
    @ResponseBody
    public R checkRePwdMobile(String mobile, String securityCode){
        if(StringUtils.isBlank(mobile)){
            return ErrorMsg.errorMsg(rpc,ErrorCode.NOT_EMPTY,"手机号不能为空");
        }
        if(StringUtils.isBlank(securityCode)){
            return ErrorMsg.errorMsg(rpc,ErrorCode.NOT_EMPTY,"验证码不能为空");
        }
        return checkEmailAndMob(mobile,securityCode,"isMobile","rePwdMobile");
    }
    /**
     * 找回密码
     * */
    @RequestMapping(value="/checkRePwdEmail",method = RequestMethod.GET)
    @ResponseBody
    public R checkRePwdEmail(String email,String securityCode){
        if(StringUtils.isBlank(email)){
            return ErrorMsg.errorMsg(rpc,ErrorCode.NOT_EMPTY,"邮箱不能为空");
        }
        if(StringUtils.isBlank(securityCode)){
            return ErrorMsg.errorMsg(rpc,ErrorCode.NOT_EMPTY,"验证码不能为空");
        }
        return checkEmailAndMob(email,securityCode,"isEmail","rePwdEmail");
    }
    /**
     * 通过邮箱加类型发送验证码 并保存到数据库中
     * */
    @RequestMapping(value = "/sendForPwd",method = RequestMethod.GET)
    public R sendRePwd(String contact,String fPwdType){
        if(StringUtils.isBlank(contact)){
            return ErrorMsg.errorMsg(rpc,ErrorCode.NOT_EMPTY,"联系方式不能为空");
        }
        if(StringUtils.isBlank(fPwdType)){
            return ErrorMsg.errorMsg(rpc,ErrorCode.NOT_EMPTY,"联系方式类型不能为空");
        }
        /**
         * 邮箱发送验证码 找回密码
         * */
        if("isEmail".equals(fPwdType)){//1邮箱忘记密码
            boolean flag = checkBindEmail(contact,"isEmail");
            if(!flag){
                return ErrorMsg.errorMsg(rpc,ErrorCode.DATA_QUERY_CHECKING,"该邮箱不存在");
            }
            String securityCode = StringUtils.getSecurityCode(6);

            SendEmailSecurityCode sendEmailSecurityCode = SendSecurityCodeUtils.buildSendEmailSecurityCode(sendEmailConfig,contact,securityCode,"2");
            try {
                R r = sendEmailService.sendEmailSecurityCode(sendEmailSecurityCode);
                if(!r.isOK()){
                    return ErrorMsg.errorMsg(rpc,ErrorCode.FAIL,"邮件发送失败");
                }
            }catch (Exception e){
                return ErrorMsg.errorMsg(rpc,ErrorCode.ABNORMAL,"邮件发送异常");
            }
            saveSecurity(contact,"rePwdEmail",securityCode);
            return R.ok();
        }else{
            //如果忘记密码类型为手机忘记密码
            boolean flag = checkBindEmail(contact,"isMobile");
            if(!flag){
                return ErrorMsg.errorMsg(rpc,ErrorCode.DATA_QUERY_CHECKING,"手机号不存在");
            }
            String securityCode = StringUtils.getSecurityCode(6);
            SendPhoneSecurityCode sendPhoneSecurityCode = SendSecurityCodeUtils.bulidSendPhoneSecurityCode(sendPhoneConfig,contact,securityCode,"2");
            R r = sendSmsService.sendSmsSecurityCode(sendPhoneSecurityCode);
            if(!r.isOK()){
                return ErrorMsg.errorMsg(rpc,ErrorCode.FAIL,"手机发送失败");
            }
            saveSecurity(contact,"rePwdMobile",securityCode);
            return R.ok();

        }
    }
    /**
     *保存验证码
     */
    public void saveSecurity(String contact,String sendSecurityType,String securityCode){
        SecurityEntity securityEntity = new SecurityEntity();
        String content="";
       if("rePwdEmail".equals(sendSecurityType)){//邮箱找回密码
            securityEntity.setType("rePwdEmail");
            securityEntity.setEmail(contact);
            content ="邮箱找回密码";
        }else if("rePwdMobile".equals(sendSecurityType)){//手机号找回密码
            securityEntity.setType("rePwdMobile");
            content =  "手机找回密码";
            securityEntity.setPhone(contact);
        }
        Long userId = sysUserService.getUidByContact(contact);
        if(userId == null){
            userId = 0l;
        }
        securityEntity.setContent(content);
        securityEntity.setUserId(userId);
        securityEntity.setCreateTime(new Date());
        securityEntity.setSecurityCode(securityCode);
        securityService.save(securityEntity);
    }
    //查看该邮箱(手机号)是否存在(綁定邮箱)
    public boolean checkBindEmail(String contact,String isEmailAndMob){
        boolean isExist = false;
        Long userId = sysUserService.getUidByContact(contact);

        SysUserEntity userEntity = null;
        if("isEmail".equals(isEmailAndMob)){//0是邮箱
            try {
                userEntity = sysUserService.queryByEmailAndUid(contact,userId);
                if(null != userEntity){
                    isExist = true;
                }
            }catch (Exception e){
                ErrorMsg.errorMsg(rpc,ErrorCode.ABNORMAL,"邮箱用户信息查询异常");
            }

        }else if("isMobile".equals(isEmailAndMob)){//1是手机号
            try {
                userEntity = sysUserService.queryBySmsAndUid(contact,userId);
                if(userEntity != null){
                    isExist = true;
                }
            }catch (Exception e){
                ErrorMsg.errorMsg(rpc,ErrorCode.ABNORMAL,"手机用户信息查询异常");
            }

        }
        return isExist;
    }
    /**
     * 检查手机号，邮箱，验证码输入是否正确 是绑定，保存数据库，如果是发送验证码，不做处理
     * */
    public R checkEmailAndMob(String contact,String securityCode,String isEmailAndMobile,String isType){

        Long userId = sysUserService.getUidByContact(contact);
        if(userId == null){
            return ErrorMsg.errorMsg(rpc,ErrorCode.DATA_QUERY_CHECKING,"用户名不能为空");
        }
        if("isEmail".equals(isEmailAndMobile)){//0代表邮箱
            //查询邮箱验证码是否正确
            SecurityEntity security = securityService.queryEmailSecurity(contact.trim(),securityCode.trim(),userId);
            if (security == null){
                return ErrorMsg.errorMsg(rpc,ErrorCode.DATA_QUERY_CHECKING,"邮箱验证码错误");
            }
            if("bindEmail".equals(isType)){ //绑定邮箱
                /**
                 * 更新邮箱到用户表中
                 * */
                boolean flag = sysUserService.updateEmail(contact,userId);
                if(!flag){
                    return ErrorMsg.errorMsg(rpc,ErrorCode.DATA_QUERY_CHECKING,"邮箱信息保存失败");
                }
            }
        }else if ("isMobile".equals(isEmailAndMobile)){ //1代表手机号
            //查询手机号验证码是否正确
            SecurityEntity security = securityService.querySmsSecurity(contact.trim(),securityCode.trim(),userId);
            if (security == null){
                return ErrorMsg.errorMsg(rpc,ErrorCode.DATA_QUERY_CHECKING,"验证码错误");

            }
            if("bindMobile".equals(isType)){
                boolean isBind = sysUserService.updateMobile(contact,userId);
                if(!isBind){
                    return ErrorMsg.errorMsg(rpc,ErrorCode.DATA_QUERY_CHECKING,"手机号保存失败");
                }
            }
        }
        return R.ok();
    }
}
