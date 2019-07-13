
package com.remote.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.remote.common.utils.DateUtils;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.R;


import com.remote.common.validator.ValidatorUtils;
import com.remote.common.validator.group.AddGroup;
import com.remote.sys.entity.SysUserEntity;
import com.remote.sys.service.SysUserService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
		List<SysUserEntity> sysUserEntities = sysUserService.queryUserList(params);
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
		PageUtils page = sysUserService.queryPage(params);
		return R.ok().put("page", page);
	}
	/**
	 * 保存用户
	 */
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public R save(SysUserEntity user){
		Date d = null;
		if(user.getTermOfValidity() >= 1){
			d = DateUtils.addDateYears(new Date(),user.getTermOfValidity());
		}else{
			d = DateUtils.addDateMonths(new Date(),6);
		}
		user.setFlag(1);

		user.setDeadline(d);
		sysUserService.saveUser(user);
		return R.ok();
	}
	/**
	 * 删除用户
	 */
	@RequestMapping(value = "/delete" ,method = RequestMethod.POST)
	public R delete(Long userId,Long curUserId){
		if(userId==1L){
			return R.error("系统管理员不能删除");
		}
		if(userId == curUserId){
			return R.error("当前用户不能删除");
		}
		int falg = sysUserService.removeUser(userId);
		if(falg < 0){
			return R.error("删除失败");
		}
		return R.ok();
	}

}
