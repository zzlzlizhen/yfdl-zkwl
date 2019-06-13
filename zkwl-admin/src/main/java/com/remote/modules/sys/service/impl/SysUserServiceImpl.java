
package com.remote.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.remote.common.annotation.DataFilter;
import com.remote.common.utils.Constant;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.Query;
import com.remote.modules.sys.dao.SysUserDao;
import com.remote.modules.sys.entity.SysDeptEntity;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysDeptService;
import com.remote.modules.sys.service.SysUserRoleService;
import com.remote.modules.sys.service.SysUserService;
import com.remote.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


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

	@Autowired
	private SysUserService sysUserService;

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPage(Map<String, Object> params,SysUserEntity currentUser) {
		String username = (String)params.get("username");
		String parentId = currentUser.getUserId().toString();
		IPage<SysUserEntity> page = this.page(
			new Query<SysUserEntity>().getPage(params),
			new QueryWrapper<SysUserEntity>()
				.like(StringUtils.isNotBlank(username),"username", username).like("parent_id",parentId).or().like("parent_id",parentId+",%")
				.apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
		);
		for(SysUserEntity sysUserEntity : page.getRecords()){
			sysUserService.queryAllLevel(currentUser.getUserId());
		}
		/*for(SysUserEntity sysUserEntity : page.getRecords()){
			SysDeptEntity sysDeptEntity = sysDeptService.getById(sysUserEntity.getDeptId());
			sysUserEntity.setDeptName(sysDeptEntity.getName());
		}*/

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
		this.updateById(user);

		//保存用户与角色关系
		//sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
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
		String where = "parent_id='"+userId+"' or parent_id like '%,"+userId+"' or parent_id like '%,"+userId+",%' or parent_id like '"+userId+",%'";
		return sysUserDao.queryAllLevel(where);
	}

	@Override
	public SysUserEntity queryByEmail(String email){
		return sysUserDao.queryByEmail(email);
	}

	@Override
	public List<SysUserEntity> queryChild(SysUserEntity sysUserEntity) {
		return sysUserDao.queryChild(sysUserEntity);
	}

	@Override
	public List<SysUserEntity> queryAllChild(SysUserEntity sysUserEntity) {
		return sysUserDao.queryAllChild(sysUserEntity);
	}


}
