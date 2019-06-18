
package com.remote.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.remote.common.utils.PageUtils;
import com.remote.modules.sys.entity.SysUserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserService extends IService<SysUserEntity> {

	PageUtils queryPage(Map<String, Object> params,SysUserEntity currUser);

	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);
	
	/**
	 * 保存用户
	 */
	void saveUser(SysUserEntity user,SysUserEntity currentUser);


	/**
	 * 修改用户
	 */
	void update(SysUserEntity user);

	/**
	 * 修改密码
	 * @param userId       用户ID
	 * @param password     原密码
	 * @param newPassword  新密码
	 */
	boolean updatePassword(Long userId, String password, String newPassword);
	/*
	 * @Author zhangwenping
	 * @Description 根据id 查询所有下级用户
	 * @Date 16:52 2019/5/31
	 * @Param userId
	 * @return List<SysUserEntity>
	 **/
	List<SysUserEntity> queryAllLevel(Long userId);

	SysUserEntity queryByEmail(String contactType);

	List<SysUserEntity> queryChild(SysUserEntity sysUserEntity);

	List<SysUserEntity> queryAllChild(SysUserEntity sysUserEntity);

	/**
	 * 通过当前用户id查询当前用户的手机号和邮箱
	 * */
	SysUserEntity queryById(Long userId);
}
