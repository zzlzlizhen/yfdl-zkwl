
package com.remote.modules.sys.controller;
import cn.hutool.core.util.StrUtil;
import com.remote.common.annotation.SysLog;
import com.remote.common.errorcode.ErrorCode;
import com.remote.common.errorcode.ErrorMsg;
import com.remote.common.utils.*;
import com.remote.common.validator.Assert;
import com.remote.common.validator.ValidatorUtils;
import com.remote.common.validator.group.AddGroup;
import com.remote.common.validator.group.UpdateGroup;
import com.remote.modules.project.service.ProjectService;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysUserRoleService;
import com.remote.modules.sys.service.SysUserService;
import com.remote.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 系统用户
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private ProjectService projectService;
	private Class suc = SysUserController.class;
	/**
     * 获取所有的用户名
     * */
	@RequestMapping("/nameList")
	public R list(@RequestParam Map<String, Object> params){
		List<SysUserEntity> sysUserEntities = new ArrayList<SysUserEntity>();
		try {
			sysUserEntities = sysUserService.queryUserList(params,getUser());
			return R.ok().put("user",sysUserEntities);
		}catch (Exception e){
			e.printStackTrace();
			return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"获取用户信息异常");
		}
	}
	/**
	 * 当前用户跟下级用户
	 */
	@RequestMapping(value = "/userList", method= RequestMethod.GET)
	public R userList(@RequestParam Map<String, Object> params){
		PageUtils page = null;
		try {
			page = sysUserService.queryPage(params,getUser());
			return R.ok().put("page", page);
		}catch (Exception e){
			e.printStackTrace();
			return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL ,"获取用户列表异常");
		}
	}
	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping("/info")
	public R info(){
		SysUserEntity user = getUser();
		if(user == null){
			return ErrorMsg.errorMsg(suc,ErrorCode.FAIL,"用户信息获取失败");
		}
		return R.ok().put("user", user);
	}
	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@RequestMapping("/password")
	public R password(String password, String newPassword){
		Assert.isBlank(password, "密码不为能空");
		Assert.isBlank(newPassword, "新密码不为能空");
		//原密码
		password = ShiroUtils.sha256(password, getUser().getSalt());
		//新密码
		newPassword = ShiroUtils.sha256(newPassword, getUser().getSalt());
		//更新密码
		boolean flag = false;
		try {
			flag = sysUserService.updatePassword(getUserId(), password, newPassword);
			if(!flag){
				return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"原密码不正确");
			}
		}catch (Exception e){
			e.printStackTrace();
			return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"修改密码异常");
		}
		return R.ok();
	}
	/**
	 * 修改用户状态
	 * */
	@RequestMapping("/status")
	public R status(Long userId, Integer status){
        if(userId == getUserId()){

			return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"用户不能修改自身状态");
        }
        if(userId == 1l){
			return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"超级用户状态不能修改");
        }
		boolean falg = false;
        try {
        	falg = sysUserService.updateStatus(userId,status);
			if(!falg){
				return ErrorMsg.errorMsg(suc,ErrorCode.FAIL,"修改用户状态失败");
			}
		}catch (Exception e){
        	e.printStackTrace();
			return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"修改用户状态异常");
		}

		return R.ok();
	}
	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
/*	@RequiresPermissions("sys:user:info")*/
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.getById(userId);
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		return R.ok().put("user", user);
	}
	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@RequestMapping(value = "/save",method = RequestMethod.POST)
/*	@RequiresPermissions("sys:user:save")*/
	public R save(SysUserEntity user){
		ErrorMsg.rightMsg(suc,ErrorCode.LOGGER,user.toString());
		Assert.isBlank(user.getPassword(), "密码不为能空");
		ValidatorUtils.validateEntity(user, AddGroup.class);
		if(StringUtils.isNotBlank(user.getUsername())){
			SysUserEntity sysUserEntity = null;
			try {
				sysUserEntity = sysUserService.getByUsername(user.getUsername());
				if(sysUserEntity != null){
					return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"该用户名已存在");
				}
			}catch (Exception e){
				e.printStackTrace();
				return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"用户名查询异常");
			}

		}
		if(StringUtils.isNotBlank(user.getEmail())){
			SysUserEntity sysUserEntity = null;
			try {
				sysUserEntity =	sysUserService.getByEmail(user.getEmail());
				if(sysUserEntity != null){
					return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"该邮箱已存在");
				}
			}catch (Exception e){
				e.printStackTrace();
				return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"邮箱查询异常");
			}

		}
		if(StringUtils.isNotBlank(user.getMobile())){
			SysUserEntity sysUserEntity = null;
			try {
				sysUserEntity = sysUserService.getByMobile(user.getMobile());
				if(sysUserEntity != null){
					return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"该手机号已存在");
				}
			}catch(Exception e){
				e.printStackTrace();
				return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"手机号查询异常");
			}

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
			sysUserService.saveUser(user,this.getUser());
		}catch (Exception e){
			e.printStackTrace();
			return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"保存用户信息异常");
		}
		return R.ok();
	}

	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@RequestMapping("/update")
/*	@RequiresPermissions("sys:user:update")*/
	public R update(SysUserEntity user){
		ErrorMsg.rightMsg(suc,ErrorCode.LOGGER,user.toString());
		ValidatorUtils.validateEntity(user, UpdateGroup.class);
		List<SysUserEntity> sysUserEntities = new ArrayList<SysUserEntity>();
		if(StringUtils.isNotBlank(user.getUsername())){
			SysUserEntity sysUserEntity = null;
			try {
				SysUserEntity sue = sysUserService.queryById(user.getUserId());
				if(sue != null){
					if(!user.getUsername().equals(sue.getUsername())){
						sysUserEntity = sysUserService.getByUsername(user.getUsername());
						if(sysUserEntity != null){
							return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"该用户名已存在");
						}
					}
				}
			}catch (Exception e){
				e.printStackTrace();
				return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"用户名查询异常");
			}
		}
		if(StringUtils.isNotBlank(user.getEmail())&&user.getUserId()!=null){
			try{
				sysUserEntities = sysUserService.getByEmailAndUid(user.getEmail(),user.getUserId());
				if(CollectionUtils.isNotEmpty(sysUserEntities)||sysUserEntities.size()>0){
					return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"该邮箱已存在");
				}
			}catch (Exception e){ ;
				e.printStackTrace();
				return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"修改用户邮箱查询异常");
			}

		}
		if(StringUtils.isNotBlank(user.getMobile())){
			try{
				sysUserEntities = sysUserService.getByMobileAndUid(user.getMobile(),user.getUserId());
				if(CollectionUtils.isNotEmpty(sysUserEntities)||sysUserEntities.size()>0){
					return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"该手机号已存在");
				}
			}catch (Exception e){
				e.printStackTrace();
				return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"修改用户手机号查询异常");
			}

		}
		Date d = null;
		if(user.getTermOfValidity() == 6){
			d = DateUtils.addDateYears(new Date(),99);
		}else if(user.getTermOfValidity() >= 1){
			d = DateUtils.addDateYears(user.getCreateTime(),user.getTermOfValidity());
		}else{
			d = DateUtils.addDateMonths(user.getCreateTime(),6);
		}
		user.setDeadline(d);
		user.setFlag(1);
		user.setUpdateUser(getUserId());
		try {
			sysUserService.update(user);
		}catch (Exception e){
			e.printStackTrace();
			return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"修改用户信息异常");
		}

		return R.ok();
	}

	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
/*	@RequiresPermissions("sys:user:delete")*/
	public R delete(String ids){
		ErrorMsg.rightMsg(suc,ErrorCode.LOGGER,ids);
		if(StringUtils.isEmpty(ids)){
			return ErrorMsg.errorMsg(suc,ErrorCode.NOT_EMPTY,"数据不能为空");
		}
		long userIds[] = StrUtil.splitToLong(ids,",");
		if(ArrayUtils.contains(userIds, 1L)){
			return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"系统管理员不能删除");
		}
		if(ArrayUtils.contains(userIds, getUserId())){
			return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"当前用户不能删除");
		}
		SysUserEntity sysUserEntity = new SysUserEntity();
		for(Long id:userIds){
			sysUserEntity.setUserId(id);
			List<SysUserEntity> userList  = sysUserService.queryChild(sysUserEntity);
			if(CollectionUtils.isNotEmpty(userList)||userList.size()>0){
				if(userList.size() > 1){
					return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"当前用户有下级用户，不能删除");
				}
			}
			int proCount = projectService.queryProjectByUserCount(id);
			if(proCount != 0){
				return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"当前用户以及下级用户有项目，不能删除");
			}
			try {
				int i = sysUserService.removeUser(id);
				if(i == 0){
					return ErrorMsg.errorMsg(suc,ErrorCode.FAIL,"删除用户信息失败");
				}
			}catch (Exception e){
				e.printStackTrace();
				return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"删除用户信息异常");
			}
		}
		return R.ok();
	}
	/**
	 * 通过当前用户id查询用户信息
	 * */
	@RequestMapping(value = "/queryUser",method = RequestMethod.GET)
	public R queryUser(){
		SysUserEntity user = null;
		try {
			user = sysUserService.queryById(getUserId());
			if(user == null){
				return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"用户信息不存在");
			}
		}catch (Exception e){ ;
			e.printStackTrace();
			return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"用户信息查询异常");
		}
		return R.ok().put("user",user);
	}

	/**
	 * 修改用户
	 */
	@SysLog("修改用户基本信息")
	@RequestMapping(value = "/updateBaseInfo",method = RequestMethod.POST)
/*	@RequiresPermissions("sys:user:update")*/
	public R updateBaseInfo(SysUserEntity user){
		ErrorMsg.rightMsg(suc,ErrorCode.LOGGER,user.toString());
		ValidatorUtils.validateEntity(user, UpdateGroup.class);
		List<SysUserEntity> sysUserEntities = new ArrayList<SysUserEntity>();
		if(StringUtils.isNotBlank(user.getUsername())){
			SysUserEntity sysUserEntity = null;
			try {
				SysUserEntity sue = sysUserService.queryById(user.getUserId());
				if(sue != null){
					if(!user.getUsername().equals(sue.getUsername())){
						sysUserEntity = sysUserService.getByUsername(user.getUsername());
						if(sysUserEntity != null){
							return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"该用户名已存在");
						}
					}
				}
			}catch (Exception e){
				e.printStackTrace();
				return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"用户名查询异常");
			}
		}
		if(StringUtils.isNotBlank(user.getEmail())&&user.getUserId()!=null){
			try{
				sysUserEntities = sysUserService.getByEmailAndUid(user.getEmail(),user.getUserId());
				if(CollectionUtils.isNotEmpty(sysUserEntities)||sysUserEntities.size()>0){
					return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"该邮箱已存在");
				}
			}catch (Exception e){
				e.printStackTrace();
				return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"修改用户邮箱查询失败");
			}
		}
		if(StringUtils.isNotBlank(user.getMobile())&&user.getUserId()!=null){
			try{
				sysUserEntities = sysUserService.getByMobileAndUid(user.getMobile(),user.getUserId());
				if(CollectionUtils.isNotEmpty(sysUserEntities)||sysUserEntities.size()>0){
					return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"该手机号已存在");
				}
			}catch (Exception e){
				e.printStackTrace();
				return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"修改用户手机号查询失败");
			}
		}
		SysUserEntity sysUserEntity = null;
		try {
			sysUserEntity = sysUserService.queryByIdEAndM(this.getUserId());
			if(sysUserEntity != null){
				if(!StringUtils.isBlank(user.getEmail())&&!StringUtils.isBlank(sysUserEntity.getEmail())){
					if(!user.getEmail().equals(sysUserEntity.getEmail())){
						return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"此邮箱跟绑定邮箱不一致");
					}
				}
				if(!StringUtils.isBlank(user.getMobile())&&!StringUtils.isBlank(sysUserEntity.getMobile())){
					if(!user.getMobile().equals(sysUserEntity.getMobile())){
						return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"此手机号跟绑定手机号不一致");
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"查询邮箱跟手机号异常");
		}
		try {
			sysUserService.updatebaseInfo(user,getUserId());
		}catch (Exception e){
			e.printStackTrace();
			return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"修改用户基本信息异常");
		}
		return R.ok();
	}

	/**
	 * 用户信息
	 */
	@RequestMapping("/baseInfo")
	/*@RequiresPermissions("sys:user:info")*/
	public R baseInfo(){
		SysUserEntity user = sysUserService.getById(getUserId());
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(getUserId());
		if(CollectionUtils.isNotEmpty(roleIdList)&&roleIdList.size()>0){
            user.setRoleIdList(roleIdList);
            user.setRoleId(roleIdList.get(0));
        }
		return R.ok().put("user", user);
	}
	/**
	 * 修改用户状态
	 * */
	@RequestMapping(value = "/updatePwd",method = RequestMethod.POST)
	public R updatePwd(String username, String password){
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
			return ErrorMsg.errorMsg(suc,ErrorCode.NOT_EMPTY,"用户名或密码不能为空");
		}
        String slat =  "";
		try {
			slat = sysUserService.selectSlat(username);
			if(StringUtils.isBlank(slat)){
				return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"用户不能修改自身状态");
			}
			if(StringUtils.isNotBlank(slat)){
				password = ShiroUtils.sha256(password, slat);
			}
		}catch (Exception e){
			e.printStackTrace();
			return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"盐值查询异常");
		}
		boolean falg = false;
		try {
			falg = sysUserService.updateUserName(username,password);
			if(!falg){
				return ErrorMsg.errorMsg(suc,ErrorCode.DATA_QUERY_CHECKING,"修改用户状态失败");
			}
		}catch (Exception e){
			return ErrorMsg.errorMsg(suc,ErrorCode.ABNORMAL,"修改用户状态失败");
		}
		return R.ok();
	}
}
