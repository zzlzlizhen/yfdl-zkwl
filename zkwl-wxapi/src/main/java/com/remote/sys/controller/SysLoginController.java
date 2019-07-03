
package com.remote.sys.controller;



import com.remote.common.ShiroUtils;
import com.remote.common.utils.R;

import com.remote.common.utils.StringUtils;
import com.remote.sys.entity.SysUserEntity;
import com.remote.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;


/**
 * 登录相关
 */
@RestController
@RequestMapping("/sys/user")
public class SysLoginController {
	@Autowired
	SysUserService sysUserService;
	/**
	 * 登录
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public R login(String username, String password) {
		//判断用户名密码不能为空
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
			return R.error("用户名或密码不能为空");
		}
		SysUserEntity user = sysUserService.queryByUsername(username);
		if(user == null){
			return R.error("用户名不存在");
		}
		password = ShiroUtils.sha256(password,user.getSalt());
		//判断用户名密码是否存在
		SysUserEntity sysUserEntity = sysUserService.queryByUnameAndPwd(username,password);
		if(sysUserEntity == null){
			return R.error("密码不存在");
		}
		return R.ok().put("user",sysUserEntity);
	}
}
