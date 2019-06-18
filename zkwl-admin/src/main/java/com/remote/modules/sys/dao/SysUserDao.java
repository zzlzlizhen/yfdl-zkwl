
package com.remote.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
	SysUserEntity queryByContact(String contact);

	List<SysUserEntity> queryChild(SysUserEntity sysUserEntity);

	List<SysUserEntity> queryAllChild(SysUserEntity sysUserEntity);
}
