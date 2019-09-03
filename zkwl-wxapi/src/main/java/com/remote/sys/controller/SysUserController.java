
package com.remote.sys.controller;

import com.remote.common.errorcode.ErrorCode;
import com.remote.common.utils.DateUtils;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.R;



import com.remote.device.service.DeviceService;
import com.remote.project.service.ProjectService;
import com.remote.sys.entity.SysUserEntity;
import com.remote.sys.service.SysUserService;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 系统用户
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private DeviceService deviceService;
	Logger logger = LoggerFactory.getLogger(SysUserController.class);
	private String msg = "";
	/**
     * 获取所有的用户名
     * */
/*	@RequestMapping("/nameList")
	public R list(@RequestParam Map<String, Object> params){
		List<SysUserEntity> sysUserEntities = sysUserService.queryUserList(params);
		List<String> realName = new ArrayList<String>();
        for(SysUserEntity sysUserEntity:sysUserEntities){
        	if(sysUserEntity.getRealName().equals("")){
        		realName.add("");
			}else{
				realName.add(sysUserEntity.getRealName());
			}
        }
        return R.ok().put("realName",realName);
	}*/
	/**
	 * 获取所有的用户名
	 * */
	@RequestMapping("/nameList")
	public R list(@RequestParam Map<String, Object> params){
		List<SysUserEntity> sysUserEntities = new ArrayList<SysUserEntity>();
		try {
			sysUserEntities = sysUserService.queryUserList(params);
			if(CollectionUtils.isEmpty(sysUserEntities)){
				msg = "用户信息不存在";
				logger.error(ErrorCode.X_DATA_QUERY_CHECKING + msg);
				return R.ok(msg);
			}
		}catch (Exception e){
			msg = "用户信息查询异常";
			logger.error(ErrorCode.X_ABNORMAL + msg);
			e.printStackTrace();
			return R.ok(msg);
		}
	/*	List<String> realName = new ArrayList<String>();
        for(SysUserEntity sysUserEntity:sysUserEntities){
        	if(sysUserEntity.getRealName().equals("")){
        		realName.add("");
			}else{
				realName.add(sysUserEntity.getRealName());
			}
        }*/

		return R.ok().put("user",sysUserEntities);
	}
	/**
	 * 当前用户跟下级用户
	 */
	@RequestMapping(value = "/userList", method= RequestMethod.GET)
	public R userList(@RequestParam Map<String, Object> params){
		PageUtils page = null;
		try {
			page = sysUserService.queryPage(params);
			if(page == null){
				logger.info(ErrorCode.X_DATA_QUERY_CHECKING + "没有用户信息");
			}
		}catch (Exception e){
			msg = "用户信息列表查询异常";
			logger.error(ErrorCode.X_ABNORMAL + msg);
			e.printStackTrace();
			return R.ok(msg);
		}
		return R.ok().put("page", page);
	}
	/**
	 * 保存用户
	 */
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public R save(SysUserEntity user){

		if(StringUtils.isNotBlank(user.getUsername())){
			SysUserEntity sysUserEntity =null;
			try {
				sysUserEntity =	sysUserService.getByUsername(user.getUsername());
				if(sysUserEntity != null){
					msg = "该用户名已存在";
					logger.error(ErrorCode.X_DATA_QUERY_CHECKING + msg);
					return R.error(msg);
				}
			}catch (Exception e){
				msg = "用户信息查询异常";
				logger.error(ErrorCode.X_ABNORMAL + msg);
				e.printStackTrace();
				return R.ok(msg);
			}

		}
		if(StringUtils.isNotBlank(user.getEmail())){

			SysUserEntity sysUserEntity =null;
			try {
				sysUserEntity =	sysUserService.getByEmail(user.getEmail());
				if(sysUserEntity != null){
					msg = "该邮箱已存在";
					logger.error(ErrorCode.X_DATA_QUERY_CHECKING + msg);
					return R.error(msg);
				}
			}catch (Exception e){
				msg = "用户信息查询异常";
				logger.error(ErrorCode.X_ABNORMAL + msg);
				e.printStackTrace();
				return R.ok(msg);
			}
		}
		if(StringUtils.isNotBlank(user.getMobile())){
			SysUserEntity sysUserEntity =null;
			try {
				sysUserEntity =sysUserService.getByMobile(user.getMobile());
				if(sysUserEntity != null){
					msg = "该手机号已存在";
					logger.error(ErrorCode.X_DATA_QUERY_CHECKING + msg);
					return R.error(msg);
				}
			}catch (Exception e){
				msg = "用户信息查询异常";
				logger.error(ErrorCode.X_ABNORMAL + msg);
				e.printStackTrace();
				return R.ok(msg);
			}
		}
		if(user.getTermOfValidity()==null){
			msg = "账号有效期不能为空";
			logger.error(ErrorCode.X_NOT_EMPTY+msg);
			return R.error(msg);
		}
		Date d = null;
		if(user.getTermOfValidity() == 6){
			d = DateUtils.addDateYears(new Date(),99);
		}else if(user.getTermOfValidity() >= 1){
			d = DateUtils.addDateYears(new Date(),user.getTermOfValidity());
		}else{
			d = DateUtils.addDateMonths(new Date(),6);
		}
		user.setFlag(1);

		user.setDeadline(d);
		try {
			sysUserService.saveUser(user);
		}catch (Exception e){
			msg = "保存用户异常";
			logger.error(ErrorCode.X_ABNORMAL+msg);
			return R.error(msg);
		}


		return R.ok();
	}
	/**
	 * 删除用户
	 */
	@RequestMapping(value = "/delete" ,method = RequestMethod.POST)
	public R delete(Long userId,Long curUserId){
		if(userId==null){
			msg = "数据不能为空";
			logger.error(ErrorCode.X_NOT_EMPTY + msg);
			return R.error(msg);
		}
		if(userId==1L){
			msg = "系统管理员不能删除";
			logger.error(ErrorCode.X_CAN_NOT_BE_MODIFIED + msg);
			return R.error(msg);
		}
		if(userId == curUserId){
			msg = "当前用户不能删除";
			logger.error(ErrorCode.X_CAN_NOT_BE_MODIFIED + msg);
			return R.error(msg);
		}
		int falg = 0;
		try {
			falg = sysUserService.removeUser(userId);
			if(falg <= 0){
				msg = "删除失败";
				logger.error(ErrorCode.X_DATA_QUERY_CHECKING + msg);
				return R.error(msg);
			}
		}catch (Exception e){
			msg = "删除用户异常";
			logger.error(ErrorCode.X_ABNORMAL+msg);
			return R.error(msg);
		}
		return R.ok();
	}

}
