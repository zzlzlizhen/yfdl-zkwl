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
    /**
     * 通过邮箱跟当前用户id查询用户信息是否存在
     * */
    SysUserEntity getByEmail(String email);
    /**
     * 通过手机号当前用户id检查该手机号是否已存在
     * */
    SysUserEntity getByMobile(String mobile);
    /**
     * 通过用户名查询该用户是否存在
     * */
    SysUserEntity getByUsername(String username);
    /*
     * @Author zhangwenping
     * @Description 根据ids查询详情
     * @Date 9:47 2019/7/9
     * @Param userIds
     * @return List<SysUserEntity>
     **/
    List<SysUserEntity> queryUserByUserIds(List<Long> userIds);

}
