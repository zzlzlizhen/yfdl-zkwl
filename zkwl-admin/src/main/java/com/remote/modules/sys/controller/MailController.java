package com.remote.modules.sys.controller;

import com.remote.common.utils.DateUtils;
import com.remote.common.utils.R;
import com.remote.modules.sys.entity.SecurityEntity;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.MailService;
import com.remote.modules.sys.service.SecurityService;
import com.remote.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Security;
import java.util.*;

@Controller
@RequestMapping(value = "mail")
public class MailController extends AbstractController{
    @Autowired
    private MailService mailService;
    @Autowired
    SecurityService securityService;
    @Autowired
    SysUserService sysUserService;
    @RequestMapping("/getCheckCode")
    @ResponseBody
    /**
     * 发送验证码并保存验证码到数据库中
     * */
    public R getCheckCode(String  email,int type) {
        Map<String,Object> code = new HashMap<String, Object>();
        String checkCode = String.valueOf(new Random().nextInt(89999) + 100000);
        if(type==1){
            if (email.equals(getUser().getEmail())) {
                System.out.print(getType() +"---------" + this.getUser().getType());
                if(this.getUser().getType() == 1){
                    return R.error("此邮箱以绑定");
                }
            }
            String message = "您绑定邮箱的验证码为:" + checkCode;
            try {
                mailService.sendSimpleMail(email, "绑定邮箱", message);
                SecurityEntity security = new SecurityEntity();
                security.setEmail(email);
/*            Date date = new Date();
            String da = DateUtils.format(date,DateUtils.DATE_TIME_PATTERN);*/
                security.setCreateTime(new Date());
                security.setType("bindEmail");
                security.setSecurityCode(Long.parseLong(checkCode));
                securityService.save(security);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{

        }

        code.put("checkCode",checkCode);
        code.put("type",getType());
        return R.ok().put("code",code);

    }
    @RequestMapping(value = "/sendSecurityCode")
    @ResponseBody
    public R sendSecurityCode(Long uid,String email){
        //通过邮箱跟用户id验证该用户是否是第一次发送验证码
        SecurityEntity security = null;
        try {

        }catch (Exception e){

        }
        return R.ok();
    }

}
