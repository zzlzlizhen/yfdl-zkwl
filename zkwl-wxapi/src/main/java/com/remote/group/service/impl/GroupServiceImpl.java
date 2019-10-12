package com.remote.group.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.advancedsetting.service.AdvancedSettingService;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.entity.DeviceQuery;
import com.remote.device.service.DeviceService;
import com.remote.devicetype.entity.DeviceTypeEntity;
import com.remote.devicetype.service.DeviceTypeService;
import com.remote.enums.DeviceEnum;
import com.remote.group.dao.GroupMapper;
import com.remote.group.entity.GroupEntity;
import com.remote.group.entity.GroupQuery;
import com.remote.group.service.GroupService;
import com.remote.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author zhangwenping
 * @Date 2019/6/4 9:25
 * @Version 1.0
 **/
@Service
public class GroupServiceImpl implements GroupService {
    private Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);
    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    AdvancedSettingService advancedSettingService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceTypeService deviceTypeService;

    @Override
    public boolean addGroup(GroupEntity group) {
        logger.info("添加分组信息："+JSONObject.toJSONString(group));
        boolean flag = false;
        int n = groupMapper.insert(group);
        if(n > 0){
            flag = true;
            AdvancedSettingEntity advancedSettingEntity = new AdvancedSettingEntity();
            if(StringUtils.isNotBlank(group.getGroupId())||!("undefined").equals(group.getGroupId())){
                advancedSettingEntity.setCreateTime(new Date());
                advancedSettingEntity.setGroupId(group.getGroupId());
                advancedSettingEntity.setDeviceCode("0");
                advancedSettingEntity.setUid(group.getCreateUser());
                initAdvSet(advancedSettingEntity);
                advancedSettingService.saveAdvSetDev(advancedSettingEntity);
            }
        }
        return flag;

        /*return groupMapper.insert(group) > 0 ? true : false;*/
    }

    @Override
    public PageInfo<GroupEntity> queryGroupByName(GroupQuery groupQuery) {
        logger.info("查询分组信息："+JSONObject.toJSONString(groupQuery));
        Map<String,Integer> all = new HashMap<>();
        Map<String,Integer> alarm = new HashMap<>();
        Map<String,Integer> fault = new HashMap<>();
        PageHelper.startPage(groupQuery.getPageNum(),groupQuery.getPageSize());
        List<GroupEntity> list = groupMapper.queryGroupByName(groupQuery);
        if(CollectionUtils.isNotEmpty(list)){
            List<String> groupIds = list.parallelStream().map(groupEntity -> groupEntity.getGroupId()).collect(Collectors.toCollection(ArrayList::new));
            dataManager(groupIds,groupQuery.getProjectId(),all,alarm,fault);
            for (GroupEntity groupEntity : list){
                String groupId = groupEntity.getGroupId();
                groupEntity.setDeviceCount(0);
                groupEntity.setCallPoliceCount(0);
                groupEntity.setFaultCount(0);
                DeviceEntity deviceEntity = deviceService.queryDeviceByGroupIdTopOne(groupId);
                if(deviceEntity != null){
                    DeviceTypeEntity deviceType = deviceTypeService.getDeviceTypeByCode(deviceEntity.getDeviceType(), 1);
                    groupEntity.setDeviceTypeName(deviceType.getDeviceTypeName());
                }else{
                    groupEntity.setDeviceTypeName("");
                }
                if(all.get(groupEntity.getGroupId()) != null){
                    groupEntity.setDeviceCount(all.get(groupEntity.getGroupId()));
                }
                if(alarm.get(groupEntity.getGroupId()) != null){
                    groupEntity.setCallPoliceCount(alarm.get(groupEntity.getGroupId()));
                }
                if(fault.get(groupEntity.getGroupId()) != null){
                    groupEntity.setFaultCount(fault.get(groupEntity.getGroupId()));
                }
                DeviceQuery deviceQuery = new DeviceQuery();
                deviceQuery.setGroupId(groupEntity.getGroupId());
                //查询出项目下分组中所有设备
                List<DeviceEntity> deviceList = deviceService.queryDeviceNoPage(deviceQuery);
                toLongitude(deviceList,groupEntity);
            }
        }
        PageInfo<GroupEntity> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    private void toLongitude(List<DeviceEntity> list, GroupEntity groupEntity) {
        //定义经度总和
        BigDecimal longitudeSum = new BigDecimal(0);
        //定义纬度总和
        BigDecimal latitudeSum = new BigDecimal(0);
        if(CollectionUtils.isNotEmpty(list)){
            //经度list
            List<Double> longitudeList = new ArrayList<>();
            //纬度list
            List<Double> latitudeList = new ArrayList<>();
            //如果设备太多，只取前100条
            int size = list.size() > 99 ? 99 : list.size();
            //经度临时list
            List<Double> longitudeTempList = new ArrayList<>();
            //纬度临时list
            List<Double> latitudeTempList = new ArrayList<>();
            for(int i = 0; i < size;i++ ){
                if(StringUtils.isNotEmpty(list.get(i).getLongitude())){
                    double v = Double.parseDouble(list.get(i).getLongitude());
                    if(v != 0.0){
                        longitudeTempList.add(v);
                    }
                }
                if(StringUtils.isNotEmpty(list.get(i).getLatitude())){
                    double v = Double.parseDouble(list.get(i).getLatitude());
                    if(v != 0.0){
                        latitudeTempList.add(v);
                    }
                }
            }
            if(size > 5){
                Collections.sort(longitudeTempList);
                Collections.sort(latitudeTempList);

                for (int i = 1;i<longitudeTempList.size() - 1;i++){
                    longitudeList.add(longitudeTempList.get(i));
                }
                for (int i = 1;i<latitudeTempList.size() - 1;i++){
                    latitudeList.add(latitudeTempList.get(i));
                }
            }else{
                longitudeList.addAll(longitudeTempList);
                latitudeList.addAll(latitudeTempList);
            }


            for (Double temp : longitudeList){
                BigDecimal decimal = new BigDecimal(temp).setScale(4,BigDecimal.ROUND_HALF_DOWN);
                longitudeSum = longitudeSum.add(decimal);
            }

            for (Double temp : latitudeList){
                BigDecimal decimal = new BigDecimal(temp).setScale(4,BigDecimal.ROUND_HALF_DOWN);
                latitudeSum = latitudeSum.add(decimal);
            }

            //保存项目经度和纬度
            if(longitudeSum.compareTo(new BigDecimal(0)) == 1){ //判断是否大于0
                groupEntity.setLongitude(longitudeSum.divide(BigDecimal.valueOf(longitudeList.size()),4,BigDecimal.ROUND_HALF_UP).toString());
            }else{
                groupEntity.setLongitude(latitudeSum.toString());
            }
            if(latitudeSum.compareTo(new BigDecimal(0)) == 1){
                groupEntity.setLatitude(latitudeSum.divide(BigDecimal.valueOf(longitudeList.size()),4,BigDecimal.ROUND_HALF_UP).toString());
            }else{
                groupEntity.setLatitude(latitudeSum.toString());
            }

        }else{
            groupEntity.setLongitude(longitudeSum.toString());
            groupEntity.setLatitude(latitudeSum.toString());
        }
    }

    public void dataManager(List<String> groupIds,String projectId,Map<String,Integer> all,Map<String,Integer> alarm,Map<String,Integer> fault){
        List<DeviceEntity> deviceEntities = deviceService.queryDeviceByGroupCount(groupIds,projectId,DeviceEnum.ALL.getCode());//正常
        if(CollectionUtils.isNotEmpty(deviceEntities)){
            for(DeviceEntity deviceEntity : deviceEntities){
                all.put(deviceEntity.getGroupId(),deviceEntity.getCounts());
            }
        }
        List<DeviceEntity> deviceEntities1 = deviceService.queryDeviceByGroupCount(groupIds,projectId,DeviceEnum.ALARM.getCode());//报警
        if(CollectionUtils.isNotEmpty(deviceEntities1)){
            for(DeviceEntity deviceEntity : deviceEntities1){
                alarm.put(deviceEntity.getGroupId(),deviceEntity.getCounts());
            }
        }
        List<DeviceEntity> deviceEntities2 = deviceService.queryDeviceByGroupCount(groupIds,projectId,DeviceEnum.FAULT.getCode());//故障
        if(CollectionUtils.isNotEmpty(deviceEntities2)){
            for(DeviceEntity deviceEntity : deviceEntities2){
                fault.put(deviceEntity.getGroupId(),deviceEntity.getCounts());
            }
        }
    }




    @Override
    public String deleteGroup(List<String> groupList,String projectId) {
        StringBuffer sb = new StringBuffer();
        //查询所有分组信息
        GroupQuery groupQuery = new GroupQuery();
        groupQuery.setProjectId(projectId);
        List<GroupEntity> list = groupMapper.queryGroupByName(groupQuery);
        Map<String,String> map = new HashMap<>();
        if(CollectionUtils.isNotEmpty(list)){
            for (GroupEntity groupEntity : list){
                map.put(groupEntity.getGroupId(),groupEntity.getGroupName());
            }
        }
        for(String groupId : groupList){
            //根据跟组id 查询下面设备
            DeviceQuery deviceQuery = new DeviceQuery();
            deviceQuery.setPageNum(1);
            deviceQuery.setPageSize(10);
            deviceQuery.setGroupId(groupId);
            PageInfo<DeviceEntity> pageInfo = deviceService.queryDevice(deviceQuery);
            if(CollectionUtils.isNotEmpty(pageInfo.getList())){
                //查询出list 如果有把不可删除的分组名称追加到sb变量中
                GroupEntity groupEntity = groupMapper.queryGroupById(groupId);
                sb.append(groupEntity.getGroupName()).append(",");
            }else{
                groupMapper.deleteGroupById(groupId);
            }
        }
        return sb.toString();
    }

    @Override
    public boolean queryByName(String projectId,String groupName) {
        return groupMapper.queryByName(projectId,groupName) > 0 ? true : false;
    }

    @Override
    public List<GroupEntity> queryGroupNoPage(Long userId,String userName,String projectId) {
        GroupQuery groupQuery = new GroupQuery();
        groupQuery.setProjectId(projectId);
        List<GroupEntity> list = groupMapper.queryGroupByName(groupQuery);
        //如果有分组，则返回
        if(CollectionUtils.isNotEmpty(list)){
            return groupMapper.queryGroupByName(groupQuery);
        }
        //如果没有分组，新建一个默认分组 返回
        GroupEntity groupEntity = new GroupEntity(UUID.randomUUID().toString(),"默认分组");
        groupEntity.setProjectId(projectId);
        groupEntity.setCreateUser(userId);
        groupEntity.setCreateName(userName);
        groupEntity.setCreateTime(new Date());
        int insert = groupMapper.insert(groupEntity);
        if(insert > 0){
            List<GroupEntity> list1 = groupMapper.queryGroupByName(groupQuery);
            AdvancedSettingEntity advancedSettingEntity = new AdvancedSettingEntity();
            if(StringUtils.isNotBlank(groupEntity.getGroupId())||!("undefined").equals(groupEntity.getGroupId())){
                advancedSettingEntity.setCreateTime(new Date());
                advancedSettingEntity.setGroupId(groupEntity.getGroupId());
                advancedSettingEntity.setDeviceCode("0");
                advancedSettingEntity.setUid(groupEntity.getCreateUser());
                initAdvSet(advancedSettingEntity);
                advancedSettingService.saveAdvSetDev(advancedSettingEntity);
            }
            return groupMapper.queryGroupByName(groupQuery);
        }
        return new ArrayList<GroupEntity>();
    }

    @Override
    public List<GroupEntity> queryGroupIdNoPage(String projectId,String groupName) {
        Map<String,Integer> all = new HashMap<>();
        GroupQuery groupQuery = new GroupQuery();
        groupQuery.setProjectId(projectId);
        groupQuery.setGroupName(groupName);
        List<GroupEntity> list = groupMapper.queryGroupByName(groupQuery);
        if(CollectionUtils.isNotEmpty(list)){
            List<String> groupIds = list.parallelStream().map(groupEntity -> groupEntity.getGroupId()).collect(Collectors.toCollection(ArrayList::new));
            List<DeviceEntity> deviceEntities = deviceService.queryDeviceByGroupCount(groupIds,projectId,DeviceEnum.ALL.getCode());//正常
            if(CollectionUtils.isNotEmpty(deviceEntities)){
                for(DeviceEntity deviceEntity : deviceEntities){
                    all.put(deviceEntity.getGroupId(),deviceEntity.getCounts());
                }
            }
            for (GroupEntity groupEntity : list){
                if(all.get(groupEntity.getGroupId()) != null){
                    groupEntity.setDeviceCount(all.get(groupEntity.getGroupId()));
                }
                DeviceQuery deviceQuery = new DeviceQuery();
                deviceQuery.setGroupId(groupEntity.getGroupId());
                //查询出项目下所有设备
                List<DeviceEntity> deviceList = deviceService.queryDeviceNoPage(deviceQuery);
                //定义经度总和
                BigDecimal longitudeSum = new BigDecimal(0);
                //定义纬度总和
                BigDecimal latitudeSum = new BigDecimal(0);
                if(CollectionUtils.isNotEmpty(deviceList)){
                    //如果设备太多，只取前100条
                    int size = deviceList.size() > 99 ? 99 : deviceList.size();
                    for(int i = 0; i < size;i++ ){
                        if(StringUtils.isNotEmpty(deviceList.get(i).getLongitude())){
                            BigDecimal decimal = new BigDecimal(deviceList.get(i).getLongitude()).setScale(4,BigDecimal.ROUND_HALF_DOWN);
                            longitudeSum = longitudeSum.add(decimal);
                        }
                        if(StringUtils.isNotEmpty(deviceList.get(i).getLatitude())){
                            BigDecimal decimal = new BigDecimal(deviceList.get(i).getLatitude()).setScale(4,BigDecimal.ROUND_HALF_DOWN);
                            latitudeSum = latitudeSum.add(decimal);
                        }
                    }
                    //保存项目经度和纬度
                    if(longitudeSum.compareTo(new BigDecimal(0)) == 1){ //判断是否大于0
                        groupEntity.setLongitude(longitudeSum.divide(BigDecimal.valueOf(size),4,BigDecimal.ROUND_HALF_UP).toString());
                    }else{
                        groupEntity.setLongitude(latitudeSum.toString());
                    }
                    if(latitudeSum.compareTo(new BigDecimal(0)) == 1){
                        groupEntity.setLatitude(latitudeSum.divide(BigDecimal.valueOf(size),4,BigDecimal.ROUND_HALF_UP).toString());
                    }else{
                        groupEntity.setLatitude(latitudeSum.toString());
                    }

                }

            }
        }
        return list;
    }



    @Override
    public boolean updateGroup(GroupEntity groupEntity) {
        logger.info("修改分组信息："+JSONObject.toJSONString(groupEntity));
        return groupMapper.updateGroup(groupEntity) > 0 ? true : false;
    }

    @Override
    public GroupEntity queryGroupById(String groupId) {
        return groupMapper.queryGroupById(groupId);
    }
    /**
     * 功能描述：初始化组高级设置参数
     * @param advancedSettingEntity
     */
    public void initAdvSet(AdvancedSettingEntity advancedSettingEntity){

        advancedSettingEntity.setLoadWorkMode(5);
        advancedSettingEntity.setPowerLoad(500);
        advancedSettingEntity.setTimeTurnOn(1080);
        advancedSettingEntity.setTimeTurnOff(0);
        advancedSettingEntity.setTime1(10);
        advancedSettingEntity.setTime2(0);
        advancedSettingEntity.setTime3(0);
        advancedSettingEntity.setTime4(0);
        advancedSettingEntity.setTime5(0);
        advancedSettingEntity.setTimeDown(0);
        advancedSettingEntity.setPowerPeople1(100);
        advancedSettingEntity.setPowerPeople2(100);
        advancedSettingEntity.setPowerPeople3(100);
        advancedSettingEntity.setPowerPeople4(100);
        advancedSettingEntity.setPowerPeople5(100);
        advancedSettingEntity.setPowerDawnPeople(100);
        advancedSettingEntity.setPowerSensor1(0);
        advancedSettingEntity.setPowerSensor2(0);
        advancedSettingEntity.setPowerSensor3(0);
        advancedSettingEntity.setPowerSensor4(0);
        advancedSettingEntity.setPowerSensor5(0);
        advancedSettingEntity.setPowerSensorDown(0);
        advancedSettingEntity.setSavingSwitch(2);
        advancedSettingEntity.setAutoSleepTime(0);
        advancedSettingEntity.setVpv(500);
        advancedSettingEntity.setLigntOnDuration(5);
        advancedSettingEntity.setPvSwitch(1);
        advancedSettingEntity.setBatType(4);
        advancedSettingEntity.setBatStringNum(3);
        advancedSettingEntity.setVolOverDisCharge(900);
        advancedSettingEntity.setVolCharge(1260);
        advancedSettingEntity.setICharge(2000);
        advancedSettingEntity.setTempCharge(55395);
        advancedSettingEntity.setTempDisCharge(55395);
        advancedSettingEntity.setInspectionTime(5);
        advancedSettingEntity.setInductionSwitch(0);
        advancedSettingEntity.setInductionLightOnDelay(30);
        advancedSettingEntity.setFirDownPower(1100);
        advancedSettingEntity.setTwoDownPower(1050);
        advancedSettingEntity.setThreeDownPower(1000);
        advancedSettingEntity.setFirReducAmplitude(60);
        advancedSettingEntity.setTwoReducAmplitude(40);
        advancedSettingEntity.setThreeReducAmplitude(20);
        advancedSettingEntity.setSwitchDelayTime(15);
        advancedSettingEntity.setCustomeSwitch(0);
        advancedSettingEntity.setTemControlSwitch(0);
        advancedSettingEntity.setLowPowerConsumption(0);
    }

}
