package com.remote.sys.service;

import com.remote.sys.entity.SysUserEntity;

import java.util.List;

public interface SysUserService {

    /*
     * @Author zhangwenping
     * @Description 根据id 查询所有下级用户
     * @Date 16:52 2019/5/31
     * @Param userId
     * @return List<SysUserEntity>
     **/
    List<SysUserEntity> queryAllLevel(Long userId);
}
