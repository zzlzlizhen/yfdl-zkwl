
package com.remote.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.remote.common.utils.PageUtils;
import com.remote.modules.sys.entity.SecurityEntity;
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

	/**
	 * 通过当前用户邮箱跟用户id查询用户
	 * */
	SysUserEntity queryByEmailAndUid(String email,Long userId);

	List<SysUserEntity> queryChild(SysUserEntity sysUserEntity);

	List<SysUserEntity> queryAllChild(SysUserEntity sysUserEntity);

	List<SysUserEntity> queryUserList(Map<String, Object> params,SysUserEntity currentUser);
	/**
	 * 通过用户id查询用户
	 * */
	SysUserEntity queryById(Long userId);
	/**
	 *通过当前用户手机号跟用户id查询用户是否存在
	 */
	SysUserEntity queryBySmsAndUid(String mobile,Long userId);
	/**
	 * 更新用户状态
	 * */
	boolean updateStatus(Long userId,Integer status);

	/**
	 * 通过用户id更新邮箱
	 * */
	boolean updateEmail(String email,Long userId);
	/**
	 * 通过用户id更新手机号
	 * */
	boolean updateMobile(String mobile,Long userId);
	int removeUser(Long id);
	void updatebaseInfo(SysUserEntity user,Long userId);
	SysUserEntity queryByIdEAndM(Long userId);
	/**
	 * 通过用户名修改密码
	 * */
	boolean updateUserName(String userName,String password);
	/**
     * 通过用户名查询盐值
     * */
	String selectSlat(String username);
	/**
	 * 通过联系方式查询用户id
	 * */
	Long getUidByContact(String contact);

}
