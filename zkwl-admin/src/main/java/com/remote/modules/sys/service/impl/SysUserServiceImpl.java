
package com.remote.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.remote.common.annotation.DataFilter;
import com.remote.common.utils.Constant;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.Query;
import com.remote.modules.sys.dao.SysUserDao;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysDeptService;
import com.remote.modules.sys.service.SysUserRoleService;
import com.remote.modules.sys.service.SysUserService;
import com.remote.modules.sys.shiro.ShiroUtils;
import javafx.animation.Interpolatable;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysUserDao sysUserDao;

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPage(Map<String, Object> params,SysUserEntity currentUser) {
		String username = (String)params.get("username");
		String realName = (String) params.get("realName");
		String status = (String)params.get("status");
		String uid = (String)params.get("userId");
		String allParentId = currentUser.getAllParentId();
		long userId = currentUser.getUserId();
		IPage<SysUserEntity> page = this.page(
			new Query<SysUserEntity>().getPage(params),
				new QueryWrapper<SysUserEntity>().like(StringUtils.isNotBlank(username),"username", username)
						.like(StringUtils.isNotBlank(realName),"real_name", realName)
						.eq(StringUtils.isNotBlank(status),"status",StringUtils.isNotBlank(status)?Integer.parseInt(status):null)
						.eq(StringUtils.isNotBlank(uid),"user_id",uid).eq("flag",1)
						.and(new Function<QueryWrapper<SysUserEntity>, QueryWrapper<SysUserEntity>>() {
							@Override
							public QueryWrapper<SysUserEntity> apply(QueryWrapper<SysUserEntity> sysUserEntityQueryWrapper) {
								return sysUserEntityQueryWrapper.likeRight("all_parent_id",allParentId+",").or().eq("user_id",userId);
							}
						})
		);
		return new PageUtils(page);
	}

	@Override
	public void saveUser(SysUserEntity user,SysUserEntity currentUser) {
		user.setCreateTime(new Date());
		user.setDeviceCount(0);
		user.setProjectCount(0);
		user.setDeptId(1L);
		if(!StringUtils.isBlank(user.getRealName())){
			user.setRealName(user.getRealName());
		}else{
			user.setRealName("");
		}
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setSalt(salt);
		user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
		user.setParentId(currentUser.getUserId());
		this.save(user);

		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), Arrays.asList(user.getRoleId()));

		//更新新增用户的pid
		SysUserEntity temp = new SysUserEntity();
		temp.setUserId(user.getUserId());
		temp.setAllParentId(currentUser.getAllParentId()+","+user.getUserId());
		this.updateById(temp);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysUserEntity user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			SysUserEntity userEntity = this.getById(user.getUserId());
			user.setPassword(ShiroUtils.sha256(user.getPassword(), userEntity.getSalt()));
		}
		if(!StringUtils.isBlank(user.getRealName())){
			user.setRealName(user.getRealName());
		}else{
			user.setRealName("");
		}
		this.baseMapper.update(user,
				new QueryWrapper<SysUserEntity>().eq("user_id", user.getUserId()));
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}
    /**
	 * 更新用户基本信息
	 * */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updatebaseInfo(SysUserEntity user) {
		this.baseMapper.update(user,
				new QueryWrapper<SysUserEntity>().eq("user_id", user.getUserId()));
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}
	/**
     * 更新用户状态 通过用户id更用户状态
     * */
	@Override
	public boolean updateStatus(Long userId,Integer status){
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setStatus(status);
		return this.update(userEntity,
				new QueryWrapper<SysUserEntity>().eq("user_id", userId));
	}

	/**
	 *通过用户id更新邮箱
	 * */
	@Override
	public boolean updateEmail(String email,Long userId) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setEmail(email);
		userEntity.setType(1);
		return this.update(userEntity,
				new QueryWrapper<SysUserEntity>().eq("user_id", userId));
	}

	/**
	 *通过用户id更新手机号
	 * */
	@Override
	public boolean updateMobile(String mobile,Long userId) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setMobile(mobile);
		userEntity.setIsBindMtype(1);
		return this.update(userEntity,
				new QueryWrapper<SysUserEntity>().eq("user_id", userId));
	}
	/**
     * 通过用户id 删除用户
     * */
	@Override
	public int removeUser(Long uid) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setFlag(0);
		return this.baseMapper.update(userEntity,
				new QueryWrapper<SysUserEntity>().eq("user_id", uid));
	}

	@Override
	public boolean updatePassword(Long userId, String password, String newPassword) {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
        	new QueryWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
    }

	@Override
	public List<SysUserEntity> queryAllLevel(Long userId) {
	    SysUserEntity sysUserEntity = this.baseMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("user_id",userId));

		//String where = "parent_id='"+userId+"' or parent_id like '%,"+userId+"' or parent_id like '%,"+userId+",%' or parent_id like '"+userId+",%'";
		//return sysUserDao.queryAllLevel(where);
        return sysUserDao.queryAllChild(sysUserEntity);
	}

	/**
	 *根据邮箱验证用户
	 * */
	@Override
	public SysUserEntity queryByEmailAndUid(String email,Long userId){
		return sysUserDao.queryByEmailAndUid(email,userId);
	}
	/**
	 *根据手机号验证用户
	 * */
	@Override
	public SysUserEntity queryBySmsAndUid(String mobile,Long userId){
		return sysUserDao.queryBySmsAndUid(mobile,userId);
	}
   /*
   * 通过用户id查询所有的子用户
   * */
	@Override
	public List<SysUserEntity> queryChild(SysUserEntity sysUserEntity) {
		//List<SysUserEntity> list = this.baseMapper.selectList(new QueryWrapper<SysUserEntity>().eq("parent_id",sysUserEntity.getUserId()));
		return sysUserDao.queryChild(sysUserEntity);
	}

	/*
	* 通过用户id查询所有自身和子孙用户
	* */
	@Override
	public List<SysUserEntity> queryAllChild(SysUserEntity sysUserEntity) {
		//List<SysUserEntity> list = this.baseMapper.selectList(new QueryWrapper<SysUserEntity>().like("all_parent_id",sysUserEntity.getAllParentId()+",%").or().eq("user_id",sysUserEntity.getUserId()));
		return sysUserDao.queryAllChild(sysUserEntity);
	}

	/**
     * 查询用户名列表
     * */
	@Override
	public List<SysUserEntity> queryUserList(Map<String, Object> params,SysUserEntity currentUser) {
		String allParentId = currentUser.getAllParentId();
		long userId = currentUser.getUserId();
		String realName = (String)params.get("realName");
		return this.baseMapper.selectList(new QueryWrapper<SysUserEntity>().eq("flag",1)
                .and(new Function<QueryWrapper<SysUserEntity>, QueryWrapper<SysUserEntity>>() {
					@Override
					public QueryWrapper<SysUserEntity> apply(QueryWrapper<SysUserEntity> sysUserEntityQueryWrapper) {
						return sysUserEntityQueryWrapper.likeRight("all_parent_id",allParentId+",").or().eq("user_id",userId);
					}
				}).like(StringUtils.isNotBlank(realName),"real_name", realName)
		);
	}

	@Override
	public SysUserEntity queryById(Long userId) {
		return this.baseMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("user_id",userId));
	}


}
