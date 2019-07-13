package com.remote.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.remote.common.utils.PageUtils;
import com.remote.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

public interface SysUserService extends IService<SysUserEntity> {

    /*
     * @Author zhangwenping
     * @Description 根据id 查询所有下级用户
     * @Date 16:52 2019/5/31
     * @Param userId
     * @return List<SysUserEntity>
     **/
    List<SysUserEntity> queryAllLevel(Long userId);
    SysUserEntity queryByUsername(String username);
    SysUserEntity queryByUnameAndPwd(String username, String password);

    PageUtils queryPage(Map<String, Object> params);

    List<SysUserEntity> queryChild(SysUserEntity sysUserEntity);

    List<SysUserEntity> queryAllChild(SysUserEntity sysUserEntity);

    List<SysUserEntity> queryUserList(Map<String, Object> params);
    /**
     * 保存用户
     */
    void saveUser(SysUserEntity user);
    int removeUser(Long uid);

}
