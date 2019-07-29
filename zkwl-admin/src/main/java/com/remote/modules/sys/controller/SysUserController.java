
package com.remote.modules.sys.controller;
import cn.hutool.core.util.StrUtil;
import com.remote.common.annotation.SysLog;
import com.remote.common.utils.CollectionUtils;
import com.remote.common.utils.DateUtils;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.R;
import com.remote.common.validator.Assert;
import com.remote.common.validator.ValidatorUtils;
import com.remote.common.validator.group.AddGroup;
import com.remote.common.validator.group.UpdateGroup;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysUserRoleService;
import com.remote.modules.sys.service.SysUserService;
import com.remote.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
	
	/**
     * 获取所有的用户名
     * */
	@RequestMapping("/nameList")
	public R list(@RequestParam Map<String, Object> params){
		List<SysUserEntity> sysUserEntities = sysUserService.queryUserList(params,getUser());
	/*	List<String> realName = new ArrayList<String>();
        for(SysUserEntity sysUserEntity:sysUserEntities){
        	if(sysUserEntity.getRealName().equals("")){
        		realName.add("");
			}else{
				realName.add(sysUserEntity.getRealName());
			}
        }*/
	    if(CollectionUtils.isEmpty(sysUserEntities)){
	    	return R.ok("用户信息不存在");
		}
        return R.ok().put("user",sysUserEntities);
	}
	/**
	 * 当前用户跟下级用户
	 */
	@RequestMapping(value = "/userList", method= RequestMethod.GET)
	public R userList(@RequestParam Map<String, Object> params){
		PageUtils page = sysUserService.queryPage(params,getUser());
		return R.ok().put("page", page);
	}

	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping("/info")
	public R info(){
		return R.ok().put("user", getUser());
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
		boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(!flag){
			return R.error("原密码不正确");
		}

		return R.ok();
	}
	/**
	 * 修改用户状态
	 * */
	@RequestMapping("/status")
	public R status(Long userId, Integer status){
        if(userId == getUserId()){
            return R.error("用户不能修改自身状态");
        }
        if(userId == 1l){
            return R.error("超级用户状态不能修改");
        }
		boolean falg = sysUserService.updateStatus(userId,status);
		if(!falg){
			R.error("修改失败");
		}
		return R.ok();
	}
	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
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
	@RequiresPermissions("sys:user:save")
	public R save(SysUserEntity user){
		Assert.isBlank(user.getPassword(), "密码不为能空");
		ValidatorUtils.validateEntity(user, AddGroup.class);
		if(StringUtils.isNotBlank(user.getUsername())){
			SysUserEntity sysUserEntity =	sysUserService.getByUsername(user.getUsername());
			if(sysUserEntity != null){
				return R.error("该用户名已存在");
			}
		}
		if(StringUtils.isNotBlank(user.getEmail())){
			SysUserEntity sysUserEntity =	sysUserService.getByEmail(user.getEmail());
			if(sysUserEntity != null){
				return R.error("该邮箱已存在");
			}
		}
		if(StringUtils.isNotBlank(user.getMobile())){
			SysUserEntity sysUserEntity = sysUserService.getByMobile(user.getMobile());
			if(sysUserEntity != null){
				return R.error("该手机号已存在");
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
		sysUserService.saveUser(user,this.getUser());
		return R.ok();
	}
	
	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@RequestMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);
		if(StringUtils.isNotBlank(user.getEmail())){
			List<SysUserEntity> sysUserEntities = sysUserService.getByEmailAndUid(user.getEmail(),user.getUserId());
			if(CollectionUtils.isNotEmpty(sysUserEntities)||sysUserEntities.size()>0){
				return R.error("该邮箱已存在");
			}
		}
		if(StringUtils.isNotBlank(user.getMobile())){
			List<SysUserEntity> sysUserEntities = sysUserService.getByMobileAndUid(user.getMobile(),user.getUserId());
			if(CollectionUtils.isNotEmpty(sysUserEntities)||sysUserEntities.size()>0){
				return R.error("该手机号已存在");
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
		sysUserService.update(user);
		return R.ok();
	}

	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(String ids){
		if(StringUtils.isEmpty(ids)){
			return R.error("数据不能为空");
		}
		long userIds[] = StrUtil.splitToLong(ids,",");
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}
		for(Long id:userIds){
			sysUserService.removeUser(id);
		}
		//sysUserService.removeByIds(Arrays.asList(userIds));
		
		return R.ok();
	}

	/**
	 * 通过当前用户id查询用户信息
	 * */
	@RequestMapping(value = "/queryUser",method = RequestMethod.GET)
	public R queryUser(){
		SysUserEntity user = sysUserService.queryById(getUserId());
		return R.ok().put("user",user);
	}

	/**
	 * 修改用户
	 */
	@SysLog("修改用户基本信息")
	@RequestMapping(value = "/updateBaseInfo",method = RequestMethod.POST)
	@RequiresPermissions("sys:user:update")
	public R updateBaseInfo(SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);
		if(StringUtils.isNotBlank(user.getEmail())){
			List<SysUserEntity> sysUserEntities = sysUserService.getByEmailAndUid(user.getEmail(),getUserId());
			if(CollectionUtils.isNotEmpty(sysUserEntities)||sysUserEntities.size()>0){
				return R.error("该邮箱已存在");
			}
		}
		if(StringUtils.isNotBlank(user.getMobile())){
			List<SysUserEntity> sysUserEntities = sysUserService.getByMobileAndUid(user.getMobile(),getUserId());
			if(CollectionUtils.isNotEmpty(sysUserEntities)||sysUserEntities.size()>0){
				return R.error("该手机号已存在");
			}
		}
		SysUserEntity sysUserEntity = sysUserService.queryByIdEAndM(this.getUserId());
		if(sysUserEntity != null){
			if(!StringUtils.isBlank(user.getEmail())&&!StringUtils.isBlank(sysUserEntity.getEmail())){
				if(!user.getEmail().equals(sysUserEntity.getEmail())){
					return R.error("此邮箱跟绑定邮箱不一致");
				}
			}
			if(!StringUtils.isBlank(user.getMobile())&&!StringUtils.isBlank(sysUserEntity.getMobile())){
				if(!user.getMobile().equals(sysUserEntity.getMobile())){
					return R.error("此手机号跟绑定手机号不一致");
				}
			}
		}
		sysUserService.updatebaseInfo(user,getUserId());
		return R.ok();
	}

	/**
	 * 用户信息
	 */
	@RequestMapping("/baseInfo")
	@RequiresPermissions("sys:user:info")
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
			return R.error("用户名或密码不能为空");
		}
        String slat =  sysUserService.selectSlat(username);
		if(StringUtils.isNotBlank(slat)){
            password = ShiroUtils.sha256(password, slat);
        }

		boolean falg = sysUserService.updateUserName(username,password);
		if(!falg){
			R.error("修改失败");
		}
		return R.ok();
	}
}
