package com.remote.sys.controller;
import com.remote.common.ShiroUtils;
import com.remote.common.errorcode.ErrorCode;
import com.remote.common.utils.R;
import com.remote.common.utils.StringUtils;
import com.remote.sys.entity.SysUserEntity;
import com.remote.sys.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;



/**
 * 登录相关
 */
@RestController
@RequestMapping("/sys/user")
public class SysLoginController {
	@Autowired
	SysUserService sysUserService;
	private String msg = "";
	Logger logger = LoggerFactory.getLogger(SysLoginController.class);
	/**
	 * 登录
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public R login(@RequestParam(value = "username") String username, @RequestParam(value = "password")String password) {
		//判断用户名密码不能为空
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
			msg = "用户名或密码不能为空";
			logger.error(ErrorCode.X_NOT_EMPTY+msg);
			return R.error(msg);
		}
		SysUserEntity user = null;
		try {
			user = sysUserService.queryByUsername(username);
			if(user == null){
				msg = "用户名不存在";
				logger.error(ErrorCode.X_DATA_QUERY_CHECKING+msg);
				return R.error(msg);
			}
		}catch (Exception e){
			msg = "用户名查询异常";
			logger.error(ErrorCode.X_ABNORMAL+msg);
			e.printStackTrace();
			return R.error(msg);
		}
		password = ShiroUtils.sha256(password,user.getSalt());
		//判断用户名密码是否存在
		SysUserEntity sysUserEntity = null;
		try {
			sysUserEntity = sysUserService.queryByUnameAndPwd(username,password);
			if(sysUserEntity == null){
				msg = "密码不存在";
				logger.error(ErrorCode.X_DATA_QUERY_CHECKING+msg);
				return R.error("密码不存在");
			}
			sysUserEntity.setCurUid(sysUserEntity.getUserId());
			sysUserEntity.setCurAllParentId(sysUserEntity.getAllParentId());
		}catch (Exception e){
			msg = "查询用户信息异常";
			logger.error(ErrorCode.X_ABNORMAL+msg);
			e.printStackTrace();
			return R.error(msg);
		}

		return R.ok().put("user",sysUserEntity);
	}
}
