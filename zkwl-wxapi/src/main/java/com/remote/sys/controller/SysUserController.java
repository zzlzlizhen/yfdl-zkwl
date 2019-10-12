
package com.remote.sys.controller;

import com.remote.common.errorcode.ErrorCode;
import com.remote.common.errorcode.ErrorMsg;
import com.remote.common.utils.*;
import com.remote.oss.entity.SysOssEntity;
import com.remote.oss.service.SysOssService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;


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
import org.springframework.web.multipart.MultipartFile;

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
	private SysOssService sysOssService;
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
			return R.error(msg);
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
			return R.error(msg);
		}
		return R.ok().put("page", page);
	}
	/**
	 * 保存用户
	 */
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public R save(SysUserEntity user){
		logger.info("小程序保存用户入参:" + user.toString());
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
				return R.error(msg);
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
				return R.error(msg);
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
				return R.error(msg);
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
		user.setUpdateUser(user.getCurUid());
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
	@RequestMapping(value = "/delete" ,method = RequestMethod.GET)
	public R delete(Long userId,Long curUserId){
		logger.info("小程序删除用户入参" + userId + "---" + curUserId );
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
		SysUserEntity sysUserEntity = new SysUserEntity();
		try {
			sysUserEntity.setUserId(userId);
			List<SysUserEntity> userList  = sysUserService.queryChild(sysUserEntity);
			if(CollectionUtils.isEmpty(userList)||userList.size()>0){
				if(userList.size() > 1){
					msg = "当前用户有下级用户，不能删除";
					logger.error(ErrorCode.X_ABNORMAL+msg);
					return R.error(msg);
				}
			}
			int proCount = projectService.queryProjectByUserCount(userId);
			if(proCount != 0){
				msg = "当前用户以及下级用户有项目，不能删除";
				logger.error(ErrorCode.X_ABNORMAL+msg);
				return R.error(msg);
			}
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
	@Value("${server.file.path}")
	private String fileSpace;

	@ApiOperation(value="用户上传头像",notes="用户上传头像的接口")
	@PostMapping(value="/uploadFace")
	public R uploadFace(@ApiParam(value="图片",required=true) MultipartFile file) {
	/*	if (userId == null) {
			return R.error("用户id不能为空...");
		}*/
		// 文件保存的命名空间
		String fileName = file.getOriginalFilename();
		// 保存到数据库中的相对路径
		String path = "";
		try {
			path = FileUtil.uploadFile(file.getBytes(), fileSpace, FileUtil.renameToUUID(fileName));
		} catch (Exception e) {
			e.getStackTrace();
			return R.error(e.getMessage());
		}
		SysOssEntity sysOssEntity = new SysOssEntity();
		sysOssEntity.setCreateDate(new Date());
		sysOssEntity.setUrl(path);
		boolean falg = sysOssService.save(sysOssEntity);
		if(!falg){
			return R.error("图片保存失败");
		}
		return R.ok().put("path",path);
	}
	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
/*	@RequiresPermissions("sys:user:info")*/
	public R info(@PathVariable("userId") Long userId){
		if(userId == null){
			return R.error("用户id不能为空");
		}
		SysUserEntity user = sysUserService.getById(userId);
		if(user == null){
			return R.error("用户不存在");
		}
		//获取用户所属的角色列表
		/*List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);*/
		return R.ok().put("user", user);
	}
}
