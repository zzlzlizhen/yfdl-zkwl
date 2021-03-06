
package com.remote.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.remote.common.utils.PageUtils;
import com.remote.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserService extends IService<SysUserEntity> {

	PageUtils queryPage(Map<String, Object> params,SysUserEntity currUser) throws Exception;

	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);
	
	/**
	 * 保存用户
	 */
	void saveUser(SysUserEntity user,SysUserEntity currentUser) throws Exception;


	/**
	 * 修改用户
	 */
	void update(SysUserEntity user) throws Exception;

	/**
	 * 修改密码
	 * @param userId       用户ID
	 * @param password     原密码
	 * @param newPassword  新密码
	 */
	boolean updatePassword(Long userId, String password, String newPassword) throws Exception;
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
	SysUserEntity queryByEmailAndUid(String email,Long userId) throws Exception;

	List<SysUserEntity> queryChild(SysUserEntity sysUserEntity);

	List<SysUserEntity> queryAllChild(SysUserEntity sysUserEntity);

	List<SysUserEntity> queryUserList(Map<String, Object> params,SysUserEntity currentUser) throws Exception;
	/**
	 * 通过用户id查询用户
	 * */
	SysUserEntity queryById(Long userId) throws Exception;
	/**
	 *通过当前用户手机号跟用户id查询用户是否存在
	 */
	SysUserEntity queryBySmsAndUid(String mobile,Long userId) throws Exception;
	/**
	 * 更新用户状态
	 * */
	boolean updateStatus(Long userId,Integer status) throws Exception;

	/**
	 * 通过用户id更新邮箱
	 * */
	boolean updateEmail(String email,Long userId);
	/**
	 * 通过用户id更新手机号
	 * */
	boolean updateMobile(String mobile,Long userId);
	int removeUser(Long id) throws Exception;
	void updatebaseInfo(SysUserEntity user,Long userId) throws Exception;
	SysUserEntity queryByIdEAndM(Long userId) throws Exception;
	/**
	 * 通过用户名修改密码
	 * */
	boolean updateUserName(String userName,String password) throws Exception;
	/**
     * 通过用户名查询盐值
     * */
	String selectSlat(String username) throws Exception;
	/**
	 * 通过联系方式查询用户id
	 * */
	Long getUidByContact(String contact);
	/*
	 * @Author zhangwenping
	 * @Description 根据ids查询详情
	 * @Date 9:47 2019/7/9
	 * @Param userIds
	 * @return List<SysUserEntity>
	 **/
	List<SysUserEntity> queryUserByUserIds(List<Long> userIds);
	/**
	 * 通过当前用户id修改项目数量
	 * */
	boolean updateProCount(Long userId,int count);

	/**
	 * 通过当前用户id修改设备数量
	 * */
	boolean updateDevCount(Long exclUserId,int count);
	/**
	 * 通过邮箱查询用户信息是否存在
	 * */
	SysUserEntity getByEmail(String email) throws Exception;
	/**
	 * 通过手机号检查该手机号是否已存在
	 * */
	SysUserEntity getByMobile(String mobile) throws Exception;
	/**
	 * 通过邮箱跟当前用户id查询用户信息是否存在
	 * */
	List<SysUserEntity> getByEmailAndUid(String email,Long userId) throws Exception;
	/**
	 * 通过手机号当前用户id检查该手机号是否已存在
	 * */
	List<SysUserEntity> getByMobileAndUid(String mobile,Long userId) throws Exception;
	/**
	 * 通过用户名查询该用户是否存在
	 * */
	SysUserEntity getByUsername(String username) throws Exception;

}
