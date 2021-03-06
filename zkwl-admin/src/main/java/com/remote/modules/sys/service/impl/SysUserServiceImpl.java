
package com.remote.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.remote.common.annotation.DataFilter;

import com.remote.common.utils.PageUtils;
import com.remote.common.utils.Query;

import com.remote.modules.device.service.DeviceService;
import com.remote.modules.project.service.ProjectService;
import com.remote.modules.sys.dao.SysUserDao;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysDeptService;
import com.remote.modules.sys.service.SysUserRoleService;
import com.remote.modules.sys.service.SysUserService;
import com.remote.modules.sys.shiro.ShiroUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
	public PageUtils queryPage(Map<String, Object> params,SysUserEntity currentUser) throws Exception{

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
//								return sysUserEntityQueryWrapper.likeRight(StringUtils.isNotBlank(allParentId),"all_parent_id",allParentId+",").or().eq("user_id",userId);
								return sysUserEntityQueryWrapper.likeRight(StringUtils.isNotBlank(allParentId),"all_parent_id",allParentId+",");
							}
						}).orderByDesc("create_time")
		);
		return new PageUtils(page);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveUser(SysUserEntity user,SysUserEntity currentUser) throws Exception{
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
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update(SysUserEntity user) throws Exception{
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
		sysUserRoleService.saveOrUpdate(user.getUserId(), Arrays.asList(user.getRoleId()));
	}
    /**
	 * 更新用户基本信息
	 * */
	@Override
	public void updatebaseInfo(SysUserEntity user,Long userId) throws Exception{
		this.baseMapper.update(user,
				new QueryWrapper<SysUserEntity>().eq("user_id", userId));
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), Arrays.asList(user.getRoleId()));
	}
	/**
     * 更新用户状态 通过用户id更用户状态
     * */
	@Override
	public boolean updateStatus(Long userId,Integer status) throws Exception{
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
	public int removeUser(Long uid) throws Exception{
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setFlag(0);
		userEntity.setUpdateUser(uid);
		userEntity.setUpdateTime(new Date());
		return this.baseMapper.update(userEntity,
				new QueryWrapper<SysUserEntity>().eq("user_id", uid));
	}

	@Override
	public boolean updatePassword(Long userId, String password, String newPassword) throws Exception{
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
        	new QueryWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
    }

	@Override
	public List<SysUserEntity> queryAllLevel(Long userId) {
	    SysUserEntity sysUserEntity = this.baseMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("flag",1).eq("user_id",userId));
        return sysUserDao.queryAllChild(sysUserEntity);
	}

	/**
	 *根据邮箱验证用户
	 * */
	@Override
	public SysUserEntity queryByEmailAndUid(String email,Long userId) throws Exception{
		return sysUserDao.queryByEmailAndUid(email,userId);
	}
	/**
	 * 通过用户名查询该用户是否存在
	 * */
	@Override
	public SysUserEntity getByUsername(String username) throws Exception{
		return this.baseMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("flag",1).eq("username",username));
	}
	/**
	 * 通过邮箱查询该用户是否存在
	 * */
	@Override
	public SysUserEntity getByEmail(String email) throws Exception{
		return this.baseMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("flag",1).eq("email",email));
	}
	/**
	 * 通过邮箱查询该用户是否存在
	 * */
	@Override
	public SysUserEntity getByMobile(String mobile) throws Exception{
		return this.baseMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("flag",1).eq("mobile",mobile));
	}
	/**
	 * 通过邮箱查询该用户是否存在
	 * */
	@Override
	public List<SysUserEntity> getByEmailAndUid(String email,Long userId) throws Exception{
		return this.sysUserDao.getByEmailAndUid(email,userId);
		//return this.baseMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("email",email).notIn("user_id",curUid));
	}
	/**
	 * 通过邮箱查询该用户是否存在
	 * */
	@Override
	public List<SysUserEntity> getByMobileAndUid(String mobile,Long userId) throws Exception{
		return this.sysUserDao.getByMobileAndUid(mobile,userId);
	}
	/**
	 *根据手机号验证用户
	 * */
	@Override
	public SysUserEntity queryBySmsAndUid(String mobile,Long userId) throws Exception{
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
	public List<SysUserEntity> queryUserList(Map<String, Object> params,SysUserEntity currentUser) throws Exception{
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
	public SysUserEntity queryById(Long userId) throws Exception{
		return this.baseMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("flag",1).eq("user_id",userId));
	}
	@Override
	public SysUserEntity queryByIdEAndM(Long userId) throws Exception{
	//	return this.baseMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("flag",1).eq("user_id",userId).eq("type",1).or().eq("is_bind_mtype",1));
		return this.sysUserDao.queryByIdEAndM(userId);
	}

	@Override
	public boolean updateUserName(String username, String password) throws Exception{

		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setPassword(password);
		return this.update(userEntity,
				new QueryWrapper<SysUserEntity>().eq("flag",1).eq("username", username));
	}
	/**
	 * 查询用户盐值用于加密通过用户名
	 * */
	@Override
	public String selectSlat(String username) throws Exception {
		return this.sysUserDao.selectSlat(username);
	}
	/**
	 * 通过联系方式查询用户id
	 * */
	@Override
	public Long getUidByContact(String contact) {
		return this.sysUserDao.getUidByContact(contact);
	}
	/**
	 * 通过用户ids查询用户列表
	 * */
	@Override
	public List<SysUserEntity> queryUserByUserIds(List<Long> userIds) {
		return sysUserDao.queryUserByUserIds(userIds);
	}

	/**
	 * 更新所属用户的所有父用户以及自身的项目数量根据项目所属用户id
	 * */
	@Override
	public boolean updateProCount(Long exclUserId,int count) {
		if(exclUserId != null){
			/**
			 * 通过所属用户id查询所有父id
			 * */
			List<Long> allParents = getUserId(exclUserId);
			if(CollectionUtils.isNotEmpty(allParents)||allParents.size()>0){
				return this.sysUserDao.updateProjdectCount(allParents,count);
			}
		}
		return false;
	}

	@Override
	public boolean updateDevCount(Long exclUserId,int count) {
		if(exclUserId != null){
			/**
			 * 通过所属用户id查询所有父id
			 * */
			List<Long> allParents = getUserId(exclUserId);
			if(CollectionUtils.isNotEmpty(allParents)||allParents.size()>0){
				return this.sysUserDao.updateDeviceCount(allParents,count);
			}
		}
		return false;
	}
    public List<Long> getUserId(Long exclUserId){
		String allParentId  = sysUserDao.queryByUserId(exclUserId);
		List<String>  allParentIdList = Arrays.asList( allParentId.split(","));
		List<Long> allParents = new ArrayList<Long>();
		if(CollectionUtils.isNotEmpty(allParentIdList)||allParentIdList.size()>0){
			for(String userId:allParentIdList){
				allParents.add(Long.parseLong(userId));
			}
			if(CollectionUtils.isNotEmpty(allParents)||allParents.size()>0){
				return allParents;
			}
		}
		return allParents;
	}

}
