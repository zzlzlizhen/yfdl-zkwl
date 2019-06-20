
package com.remote.modules.sys.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.remote.common.annotation.SysLog;
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


import java.util.ArrayList;
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
	private SysUserRoleService sysUserRoleService;
	
	/**
     * 获取所有的用户名
     * */
	@RequestMapping("/nameList")
	public R list(@RequestParam Map<String, Object> params){
		List<SysUserEntity> sysUserEntities = sysUserService.queryUserList(params,getUser());
		List<String> realName = new ArrayList<String>();
        for(SysUserEntity sysUserEntity:sysUserEntities){
        	if(sysUserEntity.getRealName().equals("")){
        		realName.add("");
			}else{
				realName.add(sysUserEntity.getRealName());
			}
        }
        return R.ok().put("realName",realName);
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
		ValidatorUtils.validateEntity(user, AddGroup.class);
		user.setFlag(1);
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
	//判断输入的账号是否有效
	public R backPassword(@RequestParam("email") String email){
		SysUserEntity userEntity = sysUserService.queryByEmail(email);
		if(null == userEntity){
			return 	R.error("该邮箱不存在");
		}
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
}
