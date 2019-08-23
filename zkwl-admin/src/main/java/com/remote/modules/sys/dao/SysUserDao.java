
package com.remote.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import sun.rmi.runtime.Log;

import java.io.Serializable;
import java.util.List;

/**
 * 系统用户
 *
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/*
	 * @Author zhangwenping
	 * @Description 根据userid 查询所有下级用户
	 * @Date 16:57 2019/5/31
	 * @Param where
	 * @return List<SysUserEntity>
	 **/
	List<SysUserEntity> queryAllLevel(@Param("where")String where);

	/**
	 * 根据邮件查询对应用户信息
	 * */
	SysUserEntity queryByEmailAndUid(@Param("email")String email,@Param("userId")Long userId);

	/**
	 * 根据手机号查询对应的用户
	 * */
	SysUserEntity queryBySmsAndUid(@Param("mobile")String mobile, @Param("userId")Long userId);
	List<SysUserEntity> queryChild(SysUserEntity sysUserEntity);

	List<SysUserEntity> queryAllChild(SysUserEntity sysUserEntity);
	/**
	 * 通过用户名查询盐值
	 * */
	String selectSlat(@Param("username")String username);
	Long getUidByContact(@Param("contact")String contact);
	/*
	 * @Author zhangwenping
	 * @Description 根据ids查询详情
	 * @Date 9:48 2019/7/9
	 * @Param userIds
	 * @return List<SysUserEntity>
	 **/
    List<SysUserEntity> queryUserByUserIds(@Param("userIds")List<Long> userIds);

	List<SysUserEntity> getByEmailAndUid(@Param("email")String email,@Param("userId")Long userId);
	List<SysUserEntity> getByMobileAndUid(@Param("mobile")String mobile,@Param("userId")Long userId);
	SysUserEntity queryByIdEAndM(@Param("userId")Long userId);
	/**
	 *通过用户id查询出所有父id
	 * */
	String queryByUserId(@Param("userId")Long userId);

	/**
	 * 原项目数
	 * */
	List<Integer> queryProjectCount(@Param("userIds") List<Long> userIds);
	/**
	 * 原设备数
	 * */
	List<Integer> queryDeviceCount(@Param("userIds") List<Long> userIds);
	boolean updateProjdectCount(@Param("userIds")List<Long> userIds,@Param("count") int count);
	boolean updateDeviceCount(@Param("userIds")List<Long> userIds,@Param("count") int count);
}
