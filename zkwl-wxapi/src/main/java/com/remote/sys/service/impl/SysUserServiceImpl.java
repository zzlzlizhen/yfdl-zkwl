package com.remote.sys.service.impl;

import com.remote.sys.entity.SysUserEntity;
import com.remote.sys.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/6/29 13:58
 * @Version 1.0
 **/

@Service
public class SysUserServiceImpl implements SysUserService {
    @Override
    public List<SysUserEntity> queryAllLevel(Long userId) {
        return null;
    }
}
