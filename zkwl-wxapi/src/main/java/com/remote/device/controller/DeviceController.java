package com.remote.device.controller;


import com.github.pagehelper.PageInfo;
import com.remote.common.utils.R;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.entity.DeviceQuery;
import com.remote.device.service.DeviceService;
import com.remote.sys.controller.AbstractController;

import com.remote.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author zhangwenping
 * @Date 2019/6/4 13:33
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fun/device")
public class DeviceController extends AbstractController {

    @Autowired
    private DeviceService deviceService;


    @RequestMapping(value = "/add", method= RequestMethod.POST)
    public R queryDevice(@RequestBody DeviceEntity deviceEntity){
        SysUserEntity user = getUser();
        deviceEntity.setCreateUser(user.getUserId());
        deviceEntity.setIsDel(0);
        deviceEntity.setCreateName(user.getUsername());
        deviceEntity.setCreateTime(new Date());
        deviceEntity.setDeviceId(UUID.randomUUID().toString());
        deviceEntity.setOnOff(0);
        deviceEntity.setSignalState(0);
        boolean flag = deviceService.addDevice(deviceEntity);
        if(!flag){
            return R.error(400,"添加设备失败");
        }
        return R.ok();
    }


    @RequestMapping(value = "/queryDevice", method= RequestMethod.POST)
    public R queryDevice(@RequestBody DeviceQuery deviceQuery){
        PageInfo<DeviceEntity> pageInfo = deviceService.queryDevice(deviceQuery);
        if(pageInfo != null){
            return R.ok(pageInfo);
        }
        return R.error(400,"查询设备失败");
    }
}
