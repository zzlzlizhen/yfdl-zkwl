package com.remote.modules.device.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.remote.common.utils.DataUtils;
import com.remote.common.utils.R;
import com.remote.common.utils.StringUtils;
import com.remote.common.utils.MapUtils;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.entity.DeviceQuery;
import com.remote.modules.device.service.DeviceService;
import com.remote.modules.sys.controller.AbstractController;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired SysUserService sysUserService;

    @Autowired
    private RabbitTemplate template;

    @RequestMapping(value = "/queryDevice", method= RequestMethod.POST)
    public R queryDevice(@RequestBody DeviceQuery deviceQuery) throws Exception {
        PageInfo<DeviceEntity> pageInfo = deviceService.queryDevice(deviceQuery);
        if(pageInfo != null){
            return R.ok(pageInfo);
        }
        return R.error(400,"查询设备失败");
    }

    @RequestMapping(value = "/change", method= RequestMethod.POST)
    public void change(@RequestBody DataUtils data){
        List<String> deviceCodes = new ArrayList<>();
        DeviceQuery deviceQuery = new DeviceQuery();
        if(StringUtils.isNotEmpty(data.getGroupId())){
            deviceQuery.setGroupId(data.getGroupId());
            List<DeviceEntity> deviceEntities = deviceService.queryDeviceNoPage(deviceQuery);
            deviceCodes = deviceEntities.parallelStream().map(deviceEntity -> deviceEntity.getDeviceCode()).collect(Collectors.toCollection(ArrayList::new));
            data.setDeviceCodes(deviceCodes);
        }
        if(StringUtils.isNotEmpty(data.getProjectId())){
            deviceQuery.setGroupId(data.getGroupId());
            List<DeviceEntity> deviceEntities = deviceService.queryDeviceNoPage(deviceQuery);
            deviceCodes = deviceEntities.parallelStream().map(deviceEntity -> deviceEntity.getDeviceCode()).collect(Collectors.toCollection(ArrayList::new));
            data.setDeviceCodes(deviceCodes);
        }
        List<Integer> key = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(data.getQaKey())){
            for(String str : data.getQaKey()){
                key.add(MapUtils.map.get(str));
            }
        }
        data.setKey(key);
        String s = JSONObject.toJSONString(data);
        logger.info("操作设备:"+s);
        template.convertAndSend("CalonDirectExchange", "CalonDirectRouting", s);
    }

    @RequestMapping(value = "/updateVersion", method= RequestMethod.POST)
    public void updateVersion(@RequestBody DataUtils data){
        String s = JSONObject.toJSONString(data);
        logger.info("设备升级:"+s);
        template.convertAndSend("CalonDirectExchange", "CalonDirectRouting", s);
    }

    @RequestMapping(value = "/add", method= RequestMethod.POST)
    public R queryDevice(@RequestBody DeviceEntity deviceEntity) throws Exception {
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
        }else{
            sysUserService.updateDevCount(user);
        }
        return R.ok();
    }

    @RequestMapping(value = "/queryCountGroupByCity", method= RequestMethod.GET)
    public R queryCountGroupByCity(){
        SysUserEntity user = getUser();
        return R.ok(deviceService.queryCountGroupByCity(user.getUserId()));
    }

    @RequestMapping(value = "/delete", method= RequestMethod.GET)
    public R queryDevice(String deviceIds){
        SysUserEntity user = getUser();
        List<String> deviceList = Arrays.asList(deviceIds.split(","));
        DeviceQuery deviceQuery = new DeviceQuery();
        deviceQuery.setDeviceList(deviceList);
        deviceQuery.setIsDel(1);//删除标记  0未删除  1已删除
        deviceQuery.setUpdateUser(user.getUserId());
        deviceQuery.setUpdateTime(new Date());
        boolean flag = deviceService.deleteDevice(deviceQuery);
        if(!flag){
            return R.error(400,"删除设备失败");
        }else{
            sysUserService.updateDevCount(getUser());
        }
        return R.ok();
    }

    @RequestMapping(value = "/moveGroup", method= RequestMethod.GET)
    public R moveGroup(String deviceIds,String groupId){
        SysUserEntity user = getUser();
        List<String> devList = Arrays.asList(deviceIds.split(","));
        DeviceQuery deviceQuery = new DeviceQuery();
        deviceQuery.setDeviceList(devList);
        deviceQuery.setGroupId(groupId);
        deviceQuery.setUpdateUser(user.getUserId());
        deviceQuery.setUpdateTime(new Date());
        boolean flag = deviceService.moveGroup(deviceQuery);
        if(!flag){
            return R.error(400,"移动分组失败");
        }
        return R.ok();
    }

    @RequestMapping(value = "/updateDevice", method= RequestMethod.POST)
    public R updateDevice(@RequestBody DeviceEntity deviceEntity) throws Exception {
        SysUserEntity user = getUser();
        deviceEntity.setUpdateUser(user.getUserId());
        deviceEntity.setUpdateTime(new Date());
        deviceEntity.setUpdateUserName(user.getUsername());
        boolean flag = deviceService.updateById(deviceEntity);
        if(!flag){
            return R.error(400,"修改设备失败");
        }else{
            sysUserService.updateDevCount(getUser());
        }
        return R.ok();
    }

    @RequestMapping(value = "/getDeviceByGroupIdNoPage", method= RequestMethod.GET)
    public R getDeviceByGroupIdNoPage(String groupId){
        DeviceQuery deviceQuery = new DeviceQuery();
        deviceQuery.setGroupId(groupId);
        return R.ok(deviceService.queryDeviceNoPage(deviceQuery));
    }

    @RequestMapping(value = "/getDeviceById", method= RequestMethod.GET)
    public R getDeviceById(String deviceId){
        return R.ok(deviceService.queryDeviceByDeviceId(deviceId));
    }

    @RequestMapping(value = "/getDeviceByProjectIdNoPage", method= RequestMethod.GET)
    public R getDeviceByProjectIdNoPage(String projectId){
        DeviceQuery deviceQuery = new DeviceQuery();
        deviceQuery.setGroupId(projectId);
        return R.ok(deviceService.queryDeviceNoPage(deviceQuery));
    }


    @RequestMapping(value = "/updateOnOffByIds", method= RequestMethod.POST)
    public R updateOnOffByIds(@RequestBody DeviceQuery deviceQuery){
        SysUserEntity user = getUser();
        deviceQuery.setUpdateUser(user.getUserId());
        deviceQuery.setUpdateTime(new Date());
        deviceQuery.setUpdateUserName(user.getUsername());
        return R.ok(deviceService.updateOnOffByIds(deviceQuery));
    }




}
