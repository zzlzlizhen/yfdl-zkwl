package com.remote.device.controller;


import com.github.pagehelper.PageInfo;
import com.remote.common.enums.AllEnum;
import com.remote.common.enums.RunStatusEnum;
import com.remote.common.enums.TransportEnum;
import com.remote.common.utils.R;
import com.remote.common.utils.StringUtils;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.entity.DeviceQuery;
import com.remote.device.service.DeviceService;
import com.remote.sys.controller.AbstractController;

import com.remote.sys.entity.SysUserEntity;
import com.remote.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
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
    @Autowired
    private SysUserService sysUserService;


    @RequestMapping(value = "/add", method= RequestMethod.POST)
    public R queryDevice(@RequestBody DeviceEntity deviceEntity){
        if(StringUtils.isEmpty(deviceEntity.getGroupId())){
            return R.error(201,"设备分组不能为空");
        }
        if(StringUtils.isEmpty(deviceEntity.getDeviceName())){
            return R.error(201,"设备名称不能为空");
        }
        deviceEntity.setIsDel(AllEnum.NO.getCode());
        deviceEntity.setCreateTime(new Date());
        deviceEntity.setDeviceId(UUID.randomUUID().toString());
        deviceEntity.setOnOff(AllEnum.NO.getCode());
        deviceEntity.setSignalState(0);
        deviceEntity.setLight("100");
        deviceEntity.setRunState(RunStatusEnum.OFFLINE.getCode());
        deviceEntity.setTransport(TransportEnum.NO.getCode());
        int i = deviceService.getDeviceByDeviceCode(deviceEntity.getDeviceCode());
        if(i > 0){
            return R.error(400,"设备编号重复");
        }
        boolean flag = deviceService.addDevice(deviceEntity);
        if(!flag){
            return R.error(400,"添加设备失败");
        }else{
            sysUserService.updateDevCount(deviceEntity.getCreateUser(),1);

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
        List<Long> exclUserIds = deviceService.queryExclUserId(deviceList);
        boolean flag = deviceService.deleteDevice(deviceQuery);
        if(!flag){
            return R.error(400,"删除设备失败");
        }else{
            if(CollectionUtils.isNotEmpty(exclUserIds)||exclUserIds.size()>0){
                for(Long exclUserId:exclUserIds){
                    sysUserService.updateDevCount(exclUserId,-1);
                }
            }
        }
        return R.ok();
    }


    @RequestMapping(value = "/updateDevice", method= RequestMethod.POST)
    public R updateDevice(@RequestBody DeviceEntity deviceEntity){
        deviceEntity.setUpdateTime(new Date());
        boolean flag = deviceService.updateById(deviceEntity);
        if(!flag){
            return R.error(400,"修改设备失败");
        }
        return R.ok();
    }


    @RequestMapping(value = "/updateOnOffByIds", method= RequestMethod.POST)
    public R updateOnOffByIds(@RequestBody DeviceQuery deviceQuery){
        deviceQuery.setUpdateTime(new Date());
        return R.ok(deviceService.updateOnOffByIds(deviceQuery));
    }

}
