package com.remote.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.remote.common.ShiroUtils;
import com.remote.common.annotation.DataFilter;
import com.remote.common.utils.PageUtils;
import com.remote.device.service.DeviceService;
import com.remote.project.service.ProjectService;
import com.remote.sys.dao.SysUserDao;
import com.remote.sys.entity.SysUserEntity;
import com.remote.sys.service.SysUserRoleService;
import com.remote.sys.service.SysUserService;
import com.remote.utils.Query;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

/**
 * @Author zhangwenping
 * @Date 2019/6/29 13:58
 * @Version 1.0
 **/

@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService  {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private ProjectService projectService;
    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String)params.get("username");
        String realName = (String) params.get("realName");
        String status = (String)params.get("status");
        String uid = (String)params.get("userId");
        String allParentId =(String)params.get("allParentId");
        long userId = Long.parseLong(params.get("curUserId")+"") ;//当前登录用户id

        IPage<SysUserEntity> page = this.page(new Query<SysUserEntity>().getPage(params), new QueryWrapper<SysUserEntity>().like(StringUtils.isNotBlank(username),"username", username)
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
    public List<SysUserEntity> queryAllLevel(Long userId) {
        SysUserEntity sysUserEntity = this.baseMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("flag",1).eq("user_id",userId));
        return sysUserDao.queryAllChild(sysUserEntity);
    }

    @Override
    public SysUserEntity queryByUnameAndPwd(String username, String password) {
        return this.sysUserDao.queryByUnameAndPwd(username,password);
    }
    @Override
    public SysUserEntity queryByUsername(String username){
        return this.sysUserDao.queryByUname(username);
    }

    /**
     * 查询用户名列表
     * */
    @Override
    public List<SysUserEntity> queryUserList(Map<String, Object> params) {
        String allParentId =(String)params.get("allParentId");
        long userId = Long.parseLong(params.get("curUserId")+"") ;//当前登录用户id
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

    /*
    * 通过用户id查询所有的子用户
    * */
    @Override
    public List<SysUserEntity> queryChild(SysUserEntity sysUserEntity) {
        //List<SysUserEntity> list = this.baseMapper.selectList(new QueryWrapper<SysUserEntity>().eq("parent_id",sysUserEntity.getUserId()));
        return sysUserDao.queryChild(sysUserEntity);
    }
    /**
     * 通过用户id 删除用户
     * */
    @Override
    public int removeUser(Long userId) {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setFlag(0);
        return this.baseMapper.update(userEntity,
                new QueryWrapper<SysUserEntity>().eq("user_id", userId));
    }

    @Override
    public List<SysUserEntity> queryUserByUserIds(List<Long> userIds) {
        return sysUserDao.queryUserByUserIds(userIds);
    }

    /*
    * 通过用户id查询所有自身和子孙用户
    * */
    @Override
    public List<SysUserEntity> queryAllChild(SysUserEntity sysUserEntity) {
        //List<SysUserEntity> list = this.baseMapper.selectList(new QueryWrapper<SysUserEntity>().like("all_parent_id",sysUserEntity.getAllParentId()+",%").or().eq("user_id",sysUserEntity.getUserId()));
        return sysUserDao.queryAllChild(sysUserEntity);
    }
    @Override
    public void saveUser(SysUserEntity user) {
        user.setCreateTime(new Date());
       /* user.setDeviceCount(0);
        user.setProjectCount(0);*/
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
        user.setParentId(user.getCurUid());
        this.save(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), Arrays.asList(user.getRoleId()));

        //更新新增用户的pid
        SysUserEntity temp = new SysUserEntity();
        temp.setUserId(user.getUserId());
        temp.setAllParentId(user.getCurAllParentId()+","+user.getUserId());
        this.updateById(temp);
    }
    /**
     * 通过邮箱查询该用户是否存在
     * */
    @Override
    public SysUserEntity getByUsername(String username) {
        return this.baseMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("flag",1).eq("username",username));
    }
    /**
     * 通过邮箱查询该用户是否存在
     * */
    @Override
    public SysUserEntity getByEmail(String email) {
        return this.baseMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("flag",1).eq("email",email));
    }
    /**
     * 通过邮箱查询该用户是否存在
     * */
    @Override
    public SysUserEntity getByMobile(String mobile) {
        return this.baseMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("flag",1).eq("mobile",mobile));
    }
    @Override
    public boolean updateDevCount(SysUserEntity curUser) {
        SysUserEntity userEntity = new SysUserEntity();
        List<SysUserEntity> userEntityList =  queryAllChild(curUser);
        List<Long> userIds = new ArrayList<Long>();
        if(CollectionUtils.isNotEmpty(userEntityList)&&userEntityList.size()>0){
            for(SysUserEntity sysUserEntity: userEntityList){
                userIds.add(sysUserEntity.getUserId());
            }
            Integer devCount = null;
            if(CollectionUtils.isNotEmpty(userIds)&&userIds.size()>0){
                devCount = deviceService.getDeviceCount(userIds);
                if(devCount == null){
                    devCount =0;
                }else{
                    devCount = devCount;
                }
            }
            userEntity.setDeviceCount(devCount);
        }
        return this.update(userEntity,
                new QueryWrapper<SysUserEntity>().eq("user_id", curUser.getUserId()));
    }
    @Override
    public boolean updateProCount(Long curUid) {
        SysUserEntity userEntity = new SysUserEntity();
        Integer proCount = projectService.queryProjectByUserCount(curUid);
        if(proCount == null){
            proCount = 0;
        }else{
            proCount = proCount;
        }
        userEntity.setProjectCount(proCount);
        return this.update(userEntity,
                new QueryWrapper<SysUserEntity>().eq("user_id", curUid));
    }
    @Override
    public String queryByUid(Long curUid){
        return this.sysUserDao.queryByUid(curUid);
    }
}
