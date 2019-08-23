package com.remote.modules.device.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.remote.common.enums.AllEnum;
import com.remote.common.enums.DeviceEnum;
import com.remote.common.enums.RunStatusEnum;
import com.remote.common.enums.TransportEnum;
import com.remote.common.redis.CacheUtils;
import com.remote.common.utils.*;
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
import sun.rmi.transport.Transport;

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

    @Autowired
    private CacheUtils cacheUtils;

    @RequestMapping(value = "/queryDevice", method= RequestMethod.POST)
    public R queryDevice(@RequestBody DeviceQuery deviceQuery) throws Exception {
        String esFlag = cacheUtils.get("ES_FLAG").toString();
        // 1 代表 查询es
        if(esFlag.equals(esFlag)){
            Pager<Map<String, Object>> mapPager = deviceService.queryDevice(deviceQuery);
            if(mapPager != null){
                return R.ok(mapPager);
            }
            //2 代表 查询 mysql
        }else if(esFlag.equals(esFlag)){
            PageInfo<DeviceEntity> pageInfo = deviceService.queryDeviceByMysql(deviceQuery);
            if(pageInfo != null){
                return R.ok(pageInfo);
            }
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
        template.convertAndSend("topicExchange", "topic.upload", s);
    }

    @RequestMapping(value = "/updateVersion", method= RequestMethod.POST)
    public void updateVersion(@RequestBody DataUtils data){
        List<String> deviceCodes = new ArrayList<>();
        DeviceQuery deviceQuery = new DeviceQuery();
        if(StringUtils.isNotEmpty(data.getGroupId())){
            deviceQuery.setGroupId(data.getGroupId());
            deviceQuery.setNoRunState(DeviceEnum.OFFLINE.getCode());
            List<DeviceEntity> deviceEntities = deviceService.queryDeviceNoPage(deviceQuery);
            deviceCodes = deviceEntities.parallelStream().map(deviceEntity -> deviceEntity.getDeviceCode()).collect(Collectors.toCollection(ArrayList::new));
            data.setDeviceCodes(deviceCodes);
        }else{
            deviceCodes = data.getDeviceCodes();
        }
        if(CollectionUtils.isNotEmpty(deviceCodes)){
            deviceService.updateDeviceRunStatus(deviceCodes);
        }
        String s = JSONObject.toJSONString(data);
        logger.info("设备升级:"+s);
        template.convertAndSend("topicExchange", "topic.upgrade", s);
    }

    @RequestMapping(value = "/add", method= RequestMethod.POST)
    public R queryDevice(@RequestBody DeviceEntity deviceEntity) throws Exception {
        if(StringUtils.isEmpty(deviceEntity.getGroupId())){
            return R.error(201,"设备分组不能为空");
        }
        if(StringUtils.isEmpty(deviceEntity.getDeviceName())){
            return R.error(201,"设备名称不能为空");
        }
        if(StringUtils.isEmpty(deviceEntity.getDeviceCode())){
            return R.error(201,"设备编号不能为空");
        }
        SysUserEntity user = getUser();
        deviceEntity.setUsrUser(user.getUserId()); // 创建人 和 使用人 存反
        deviceEntity.setIsDel(AllEnum.NO.getCode());
        deviceEntity.setCreateName(user.getRealName());
        deviceEntity.setCreateTime(new Date());
        deviceEntity.setDeviceId(UUID.randomUUID().toString());
        deviceEntity.setOnOff(AllEnum.NO.getCode());
        deviceEntity.setRunState(RunStatusEnum.OFFLINE.getCode());
        deviceEntity.setSignalState(0);
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
        deviceQuery.setIsDel(AllEnum.YES.getCode());//删除标记  0未删除  1已删除
        deviceQuery.setUpdateUser(user.getUserId());
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
        R r = deviceService.updateById(deviceEntity);
        return r;
    }

    @RequestMapping(value = "/getDeviceByGroupIdNoPage", method= RequestMethod.GET)
    public R getDeviceByGroupIdNoPage(String groupId,String projectId,Integer status,String deviceCode){
        DeviceQuery deviceQuery = new DeviceQuery();
        if(StringUtils.isNotEmpty(groupId)){
            deviceQuery.setGroupId(groupId);
        }
        if(StringUtils.isNotEmpty(projectId)){
            deviceQuery.setProjectId(projectId);
        }
        if(StringUtils.isNotEmpty(deviceCode)){
            deviceQuery.setDeviceCode(deviceCode);
        }
        if(!status.equals(new Integer(0))){
            deviceQuery.setRunState(status);
        }
        return R.ok(deviceService.queryDeviceNoPage(deviceQuery));
    }



    @RequestMapping(value = "/getDeviceByGroupIdNoPageLike", method= RequestMethod.GET)
    public R getDeviceByGroupIdNoPage(Integer status,String deviceCode){
        DeviceQuery deviceQuery = new DeviceQuery();
        if(StringUtils.isNotEmpty(deviceCode)){
            deviceQuery.setDeviceCode(deviceCode);
        }
        if(!status.equals(new Integer(0))){
            deviceQuery.setRunState(status);
        }
        return R.ok(deviceService.getDeviceByGroupIdNoPageLike(deviceQuery));
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
        deviceQuery.setUpdateUserName(user.getRealName());
        return R.ok(deviceService.updateOnOffByIds(deviceQuery));
    }


    @RequestMapping(value = "/getDeviceType", method= RequestMethod.GET)
    public R getDeviceType(){
        Set<Map.Entry<String, String>> entries = DeviceTypeMap.DEVICE_TYPE.entrySet();
        List<DeviceEntity> list = new ArrayList<>();
        for(Map.Entry<String, String> entry : entries){
            DeviceEntity deviceEntity = new DeviceEntity();
            deviceEntity.setDeviceType(entry.getKey());
            deviceEntity.setDeviceTypeName(entry.getValue());
            list.add(deviceEntity);
        }
        return R.ok(list);
    }


    @RequestMapping(value = "/updateEsFlag", method= RequestMethod.GET)
    public R updateEsFlag(String esFlag){
        //esFlag  1 代表 查询es
        cacheUtils.set("ES_FLAG",esFlag);
        return R.ok();
    }

    @RequestMapping(value = "/updateLive", method= RequestMethod.GET)
    public R updateLive(String live){
        //live 秒
        cacheUtils.set("LIVE",live);
        return R.ok();
    }


}
