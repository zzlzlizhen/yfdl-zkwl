package com.remote.device.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.advancedsetting.service.AdvancedSettingService;
import com.remote.common.es.utils.ESUtil;
import com.remote.common.utils.CoodinateCovertor;
import com.remote.common.utils.LngLat;
import com.remote.device.dao.DeviceMapper;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.entity.DeviceQuery;
import com.remote.device.service.DeviceService;

import com.remote.district.entity.DistrictEntity;
import com.remote.district.service.DistrictService;
import com.remote.enums.CommunicationEnum;
import com.remote.enums.FaultlogEnum;
import com.remote.faultlog.entity.FaultlogEntity;
import com.remote.faultlog.service.FaultlogService;
import com.remote.group.entity.GroupEntity;
import com.remote.group.service.GroupService;
import com.remote.project.entity.ProjectEntity;
import com.remote.project.service.ProjectService;
import com.remote.utils.DeviceTypeMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.remote.utils.DeviceTypeMap.DEVICE_TYPE;

/**
 * @Author zhangwenping
 * @Date 2019/6/4 13:34
 * @Version 1.0
 **/
@Service
public class DeviceServiceImpl implements DeviceService {
    private Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);
    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private FaultlogService faultlogService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private GroupService groupService;
    @Autowired
    AdvancedSettingService advancedSettingService;

    @Autowired
    private ESUtil esUtil;

    @Override
    public List<DeviceEntity> queryDeviceByGroupCount(List<String> groupIds, String projectId, Integer deviceStatus) {
        return deviceMapper.queryDeviceByGroupCount(groupIds,projectId,deviceStatus);
    }

    @Override
    public List<DeviceEntity> queryDeviceByProjectCount(List<String> projectIds, Integer deviceStatus) {
        return deviceMapper.queryDeviceByProjectCount(projectIds,deviceStatus);
    }

    @Override
    public boolean addDevice(DeviceEntity deviceEntity) {
        logger.info("添加设备信息："+JSONObject.toJSONString(deviceEntity));
        //目前只有一种产品，2G 日后在添加其他产品
        deviceEntity.setDeviceType("1");
        deviceEntity.setDeviceTypeName(DeviceTypeMap.DEVICE_TYPE.get("1"));
        //添加分组名称
        String groupId = deviceEntity.getGroupId();
        GroupEntity groupEntity = groupService.queryGroupById(groupId);
        deviceEntity.setGroupName(groupEntity.getGroupName());
        deviceEntity.setCommunicationType(CommunicationEnum.NORMAL.getCode());
        //经纬度转换成百度地图经纬度start
        LngLat lngLat_bd = new LngLat(Double.valueOf(deviceEntity.getLatitude()),Double.valueOf(deviceEntity.getLongitude()));
        LngLat lngLat = CoodinateCovertor.bd_encrypt(lngLat_bd);
        deviceEntity.setLatitude(String.valueOf(lngLat.getLantitude()));
        deviceEntity.setLongitude(String.valueOf(lngLat.getLongitude()));
        //经纬度转换成百度地图经纬度end
        ProjectEntity projectEntity = projectService.queryProjectMap(deviceEntity.getProjectId());
        if(projectEntity.getCityId() == null){
            logger.info("所属项目没有城市");
        }else{
            DistrictEntity districtEntity = districtService.queryDistrictById(projectEntity.getCityId());
            deviceEntity.setCityName(districtEntity.getDistrictName());
        }
        deviceEntity.setUsrUser(deviceEntity.getCreateUser());
        deviceEntity.setCreateUser(projectEntity.getExclusiveUser());
        int insert = deviceMapper.insert(deviceEntity);
        if(insert > 0){
            //添加es start
            Map<String, Object> stringObjectMap = esUtil.convertAddES(deviceEntity);
            RestStatus restStatus = esUtil.addES(stringObjectMap,"device_index",deviceEntity.getDeviceId());
            //添加es end
        }
        AdvancedSettingEntity advancedSettingEntity = new AdvancedSettingEntity();
        if(StringUtils.isNotBlank(groupId)||!("undefined").equals(groupId)){
            advancedSettingEntity.setCreateTime(new Date());
            advancedSettingEntity.setGroupId(groupId);
            advancedSettingEntity.setDeviceCode(deviceEntity.getDeviceCode());
            advancedSettingEntity.setUid(deviceEntity.getCreateUser());
            advancedSettingEntity.setUpdateUser(deviceEntity.getUpdateUserName());
            initAdvSet(advancedSettingEntity);
            advancedSettingService.saveAdvSetDev(advancedSettingEntity);
        }
        return insert > 0 ? true : false;
    }


    @Override
    public PageInfo<DeviceEntity> queryDevice(DeviceQuery deviceQuery) {
        logger.info("查询设备信息："+JSONObject.toJSONString(deviceQuery));
        PageHelper.startPage(deviceQuery.getPageNum(),deviceQuery.getPageSize());
        List<DeviceEntity> list = deviceMapper.queryDevice(deviceQuery);
        PageInfo<DeviceEntity> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<DeviceEntity> queryDeviceNoPage(DeviceQuery deviceQuery) {
        return deviceMapper.queryDevice(deviceQuery);
    }

    @Override
    public boolean deleteDevice(DeviceQuery deviceQuery) {
        List<DeviceEntity> deviceEntityList = deviceMapper.queryDeviceByDeviceIds(deviceQuery.getDeviceList());
        List<String> deviceCodes = deviceEntityList.parallelStream().map(deviceEntity -> deviceEntity.getDeviceCode()).collect(Collectors.toCollection(ArrayList::new));
        if(CollectionUtils.isNotEmpty(deviceCodes) || deviceCodes.size()>0){
            advancedSettingService.deleteAdvSet(deviceCodes);
        }
        return deviceMapper.deleteDevice(deviceQuery) > 0 ? true : false;
    }

    @Override
    public boolean updateById(DeviceEntity deviceEntity) {
        logger.info("修改设备信息："+JSONObject.toJSONString(deviceEntity));
        if(StringUtils.isNotBlank(deviceEntity.getDeviceCode())|| deviceEntity.getDeviceCode() != ""){
            String oldGroupId = queryByDevCode(deviceEntity.getDeviceCode());
            if(!oldGroupId.equals(deviceEntity.getGroupId())){
                advancedSettingService.updateAdvancedByDeviceCode(deviceEntity.getDeviceCode(),deviceEntity.getGroupId(),oldGroupId);
            }
        }
        return deviceMapper.updateById(deviceEntity) > 0 ? true : false;
    }

    @Override
    public int updateOnOffByIds(DeviceQuery deviceQuery) {
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        //代表操作单个设备开关
        if(StringUtils.isNotEmpty(deviceQuery.getDeviceId())){
            DeviceEntity deviceEntity = new DeviceEntity();
            deviceEntity.setDeviceId(deviceQuery.getDeviceId());
            deviceEntityList.add(deviceEntity);
        }else{
            deviceEntityList = deviceMapper.queryDevice(deviceQuery);
        }
        StringBuffer sb = new StringBuffer();
        if(StringUtils.isNotEmpty(deviceQuery.getLightingDuration())){
            sb.append("亮灯时长,");
        }
        if(StringUtils.isNotEmpty(deviceQuery.getMorningHours())){
            sb.append("晨亮时长,");
        }
        if(StringUtils.isNotEmpty(deviceQuery.getLight())){
            sb.append("亮度,");
        }
        if(deviceQuery.getOnOff() != null){
            sb.append("开关,");
        }
        List<String> deviceList = new ArrayList<>();
        String userName = deviceQuery.getUpdateUserName();
        if(CollectionUtils.isNotEmpty(deviceEntityList)){
            for (DeviceEntity device : deviceEntityList){
                deviceList.add(device.getDeviceId());
                FaultlogEntity faultlogEntity = new FaultlogEntity();
                faultlogEntity.setProjectId(device.getProjectId());
                faultlogEntity.setFaultLogId(UUID.randomUUID().toString());
                faultlogEntity.setDeviceId(device.getDeviceId());
                faultlogEntity.setCreateTime(new Date());
                faultlogEntity.setCreateUserId(deviceQuery.getCreateUser());
                faultlogEntity.setLogStatus(FaultlogEnum.OPERATIONALLOG.getCode());
                faultlogEntity.setFaultLogDesc(userName+"操作了路灯"+sb.toString());
                faultlogService.addFaultlog(faultlogEntity);
            }
        }
        deviceQuery.setDeviceList(deviceList);
        return deviceMapper.updateOnOffByIds(deviceQuery);
    }

    @Override
    public int getDeviceByDeviceCode(String deviceCode) {
        return deviceMapper.getDeviceByDeviceCode(deviceCode);
    }

    @Override
    public DeviceEntity queryDeviceByGroupIdTopOne(String groupId) {
        return deviceMapper.queryDeviceByGroupIdTopOne(groupId);
    }
    /**
     * 通过当前用户id查询所有用户的设备数量
     * */
    @Override
    public Integer getDeviceCount(List<Long> curUids) {

        return this.deviceMapper.getDeviceCount(curUids);
    }

    @Override
    public String queryByDevCode(String deviceCode) {
        return deviceMapper.queryByDevCode(deviceCode);
    }
    @Override
    public List<Long> queryExclUserId(List<String> deviceIds) {
        return deviceMapper.queryExclUserId(deviceIds);
    }
    /**
     * 功能描述：初始化设备高级设置参数
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
        advancedSettingEntity.setICharge(20);
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
    }
}
