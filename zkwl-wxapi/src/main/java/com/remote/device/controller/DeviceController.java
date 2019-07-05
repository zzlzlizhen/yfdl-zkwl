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
        deviceEntity.setCreateUser(deviceEntity.getUserId());
        deviceEntity.setIsDel(0);
        deviceEntity.setCreateName(deviceEntity.getUserName());
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

    @RequestMapping(value = "/delete", method= RequestMethod.GET)
    public R queryDevice(String deviceIds,Long userId){
        List<String> deviceList = Arrays.asList(deviceIds.split(","));
        DeviceQuery deviceQuery = new DeviceQuery();
        deviceQuery.setDeviceList(deviceList);
        deviceQuery.setIsDel(1);//删除标记  0未删除  1已删除
        deviceQuery.setUpdateUser(userId);
        deviceQuery.setUpdateTime(new Date());
        boolean flag = deviceService.deleteDevice(deviceQuery);
        if(!flag){
            return R.error(400,"删除设备失败");
        }
        return R.ok();
    }


    @RequestMapping(value = "/updateDevice", method= RequestMethod.POST)
    public R updateDevice(@RequestBody DeviceEntity deviceEntity){
        deviceEntity.setUpdateUser(deviceEntity.getUserId());
        deviceEntity.setUpdateTime(new Date());
        deviceEntity.setUpdateUserName(deviceEntity.getUserName());
        boolean flag = deviceService.updateById(deviceEntity);
        if(!flag){
            return R.error(400,"修改设备失败");
        }
        return R.ok();
    }


    @RequestMapping(value = "/updateOnOffByIds", method= RequestMethod.POST)
    public R updateOnOffByIds(@RequestBody DeviceQuery deviceQuery){
        deviceQuery.setUpdateUser(deviceQuery.getUserId());
        deviceQuery.setUpdateTime(new Date());
        deviceQuery.setUpdateUserName(deviceQuery.getUserName());
        return R.ok(deviceService.updateOnOffByIds(deviceQuery));
    }


}
