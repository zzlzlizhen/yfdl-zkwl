package com.remote.device.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.advancedsetting.service.AdvancedSettingService;
import com.remote.cjdevice.entity.CjDevice;
import com.remote.cjdevice.service.CjDeviceService;
import com.remote.common.errorcode.ErrorCode;
import com.remote.common.es.utils.ESUtil;
import com.remote.common.utils.CoodinateCovertor;
import com.remote.common.utils.LngLat;
import com.remote.common.utils.R;
import com.remote.common.utils.XYmatch;
import com.remote.device.dao.DeviceMapper;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.entity.DeviceQuery;
import com.remote.device.entity.DeviceResult;
import com.remote.device.service.DeviceService;

import com.remote.district.entity.DistrictEntity;
import com.remote.district.service.DistrictService;
import com.remote.enums.FaultlogEnum;
import com.remote.faultlog.entity.FaultlogEntity;
import com.remote.faultlog.service.FaultlogService;
import com.remote.group.entity.GroupEntity;
import com.remote.group.service.GroupService;
import com.remote.project.entity.ProjectEntity;
import com.remote.project.service.ProjectService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


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
    private CjDeviceService cjDeviceService;

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

        CjDevice cjDevice = cjDeviceService.queryCjDeviceByDeviceCode(deviceEntity.getDeviceCode());

        if(cjDevice != null){
            //添加设备分类名称
            deviceEntity.setDeviceType(cjDevice.getDeviceTypeCode());
            deviceEntity.setDeviceTypeName(cjDevice.getDeviceTypeName());
            //目前只有一种产品，2G 日后在添加其他产品
            deviceEntity.setCommunicationType(cjDevice.getCommunicationType());
        }

        //添加分组名称
        String groupId = deviceEntity.getGroupId();
        GroupEntity groupEntity = groupService.queryGroupById(groupId);
        deviceEntity.setGroupName(groupEntity.getGroupName());

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

        AdvancedSettingEntity advancedSettingEntity = new AdvancedSettingEntity();
        if(StringUtils.isNotBlank(groupId)||!("undefined").equals(groupId)){
            advancedSettingEntity.setCreateTime(new Date());
            advancedSettingEntity.setGroupId(groupId);
            advancedSettingEntity.setDeviceCode(deviceEntity.getDeviceCode());
            advancedSettingEntity.setUid(deviceEntity.getCreateUser());
            advancedSettingEntity.setUpdateUser(deviceEntity.getUpdateUserName());
            if(deviceEntity.getDeviceType().equals("1")){
                initAdvSet(advancedSettingEntity,1);
            }else if(deviceEntity.getDeviceType().equals("2")){
                initAdvSet(advancedSettingEntity,2);
            }
            boolean falg =  advancedSettingService.saveAdvSetDev(advancedSettingEntity);
            if(falg){
                List<String> deviceCodes = deviceMapper.queryByGroupId(groupId);
                if(CollectionUtils.isNotEmpty(deviceCodes)&&deviceCodes.size() > 0){
                    if(deviceCodes.size() == 1){
                        advancedSettingEntity.setDeviceCode("0");
                        advancedSettingService.updateAdvanceByGAndDId(advancedSettingEntity);
                    }
                }
            }
        }
        if(deviceEntity.getCjFlag().equals(new Integer(0))){
            if(insert > 0){
                //添加es start
                Map<String, Object> stringObjectMap = esUtil.convertAddES(deviceEntity);
                RestStatus restStatus = esUtil.addES(stringObjectMap,"device_index",deviceEntity.getDeviceId());
                //添加es end
            }
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
        List<Map<String, Object>> temp = new ArrayList<>();
        List<String> deviceCodes = deviceEntityList.parallelStream().map(deviceEntity -> deviceEntity.getDeviceCode()).collect(Collectors.toCollection(ArrayList::new));
        if(CollectionUtils.isNotEmpty(deviceCodes) || deviceCodes.size()>0){
            advancedSettingService.deleteAdvSet(deviceCodes);
        }
        int i = deviceMapper.deleteDevice(deviceQuery);
        if(i > 0){
            //修改ES start
            List<String> deviceList = deviceQuery.getDeviceList();
            for (String str : deviceList){
                DeviceEntity deviceEntity = new DeviceEntity();
                deviceEntity.setDeviceId(str);
                deviceEntity.setIsDel(deviceQuery.getIsDel());
                deviceEntity.setUpdateTime(deviceQuery.getUpdateTime());
                Map<String, Object> stringObjectMap = esUtil.convertUpdateES(deviceEntity);
                temp.add(stringObjectMap);
            }
            esUtil.updateListES(temp,"device_index","deviceId");
            //修改ES end
        }
        return i > 0 ? true : false;
    }

    @Override
    public R updateById(DeviceEntity deviceEntity) {
        logger.info("修改设备信息："+JSONObject.toJSONString(deviceEntity));
        DeviceEntity deviceTemp = deviceMapper.queryDeviceByDeviceId(deviceEntity.getDeviceId());
        String groupId = deviceEntity.getGroupId();
        GroupEntity groupEntity = groupService.queryGroupById(groupId);
        deviceEntity.setDeviceCode(deviceTemp.getDeviceCode());
        deviceEntity.setGroupName(groupEntity.getGroupName());
        DeviceEntity device = deviceMapper.queryDeviceByDeviceId(deviceEntity.getDeviceId());
        if(deviceEntity.getLatitude() != null && deviceEntity.getLongitude() != null){
            double distance = XYmatch.getDistance(Double.valueOf(device.getLatitude()), Double.valueOf(device.getLongitude()),Double.valueOf(deviceEntity.getLatitude()) ,Double.valueOf(deviceEntity.getLongitude()));
            if((distance / 1000) > 5){
                return R.error(203,"移动设备不能大于5公里");
            }
        }else{
            if(StringUtils.isNotBlank(deviceEntity.getDeviceCode())|| deviceEntity.getDeviceCode() != ""){
                String oldGroupId = queryByDevCode(deviceEntity.getDeviceCode());
                if(!oldGroupId.equals(deviceEntity.getGroupId())){
                    advancedSettingService.updateAdvancedByDeviceCode(deviceEntity.getDeviceCode(),deviceEntity.getGroupId(),oldGroupId);
                }
            }
        }

        DeviceQuery deviceQuery = new DeviceQuery();
        deviceQuery.setGroupId(groupId);
        List<DeviceEntity> deviceList = deviceMapper.queryDevice(deviceQuery);
        if(CollectionUtils.isNotEmpty(deviceList)){
            String deviceType = deviceList.get(0).getDeviceType();
            if(!deviceType.equals(device.getDeviceType())){
                //代表不是一种设备类型，不允许修改
                return R.error(201,"设备类型不一致，不允许修改。");
            }
        }

        int i = deviceMapper.updateById(deviceEntity);
        if( i > 0 ){
            //修改ES start
            Map<String, Object> stringObjectMap = esUtil.convertUpdateES(deviceEntity);
            RestStatus restStatus = esUtil.updateES(stringObjectMap, deviceEntity.getDeviceId(),"device_index");
            //修改ES end
        }
        return R.ok( i > 0 ? true : false);
    }

    @Override
    public int updateOnOffByIds(DeviceQuery deviceQuery) {
        logger.info("修改设备添加日志："+JSONObject.toJSONString(deviceQuery));
        DeviceEntity oldDeviceEntity = deviceMapper.queryDeviceByDeviceId(deviceQuery.getDeviceId());
        Integer sum = 0;
        AdvancedSettingEntity advancedSettingEntity = null;
        try {
            advancedSettingEntity = advancedSettingService.queryByDevOrGroupId(deviceQuery.getGroupId(), deviceQuery.getDeviceCode());
            if(advancedSettingEntity != null){
                if(advancedSettingEntity.getTime1() != null){
                    sum+=advancedSettingEntity.getTime1();
                }
                if(advancedSettingEntity.getTime2() != null){
                    sum+=advancedSettingEntity.getTime2();
                }
                if(advancedSettingEntity.getTime3() != null){
                    sum+=advancedSettingEntity.getTime3();
                }
                if(advancedSettingEntity.getTime4() != null){
                    sum+=advancedSettingEntity.getTime4();
                }
                if(advancedSettingEntity.getTime5() != null){
                    sum+=advancedSettingEntity.getTime5();
                }
            }
        }catch (Exception e){
            String msg = "修改设备添加日志";
            logger.error(ErrorCode.ABNORMAL+msg);
            e.printStackTrace();
        }


        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        //代表操作单个设备开关
        if(StringUtils.isNotEmpty(deviceQuery.getDeviceId())){
            DeviceEntity deviceEntity = deviceMapper.queryDeviceByDeviceId(deviceQuery.getDeviceId());
            deviceEntity.setUpdateTime(new Date());
            deviceEntity.setUpdateUser(deviceQuery.getCreateUser());
            deviceEntity.setOnOff(deviceQuery.getOnOff());
            deviceEntity.setLight(deviceQuery.getLight());
            if(deviceQuery.getTransport() != null){
                deviceEntity.setTransport(deviceQuery.getTransport());
            }
            deviceEntity.setLightingDuration(deviceQuery.getLightingDuration());
            deviceEntity.setMorningHours(deviceQuery.getMorningHours());
            deviceEntityList.add(deviceEntity);
        }else{
            if(StringUtils.isNotEmpty(deviceQuery.getDeviceCode())){
                if(deviceQuery.getDeviceCode().equals("0")){
                    deviceQuery.setDeviceCode(null);
                }
            }
            //代表组设置
            deviceEntityList = deviceMapper.queryDevice(deviceQuery);
            for(DeviceEntity deviceEntity : deviceEntityList){
                deviceEntity.setUpdateTime(new Date());
                deviceEntity.setUpdateUser(deviceQuery.getCreateUser());
                deviceEntity.setOnOff(deviceQuery.getOnOff());
                deviceEntity.setLight(deviceQuery.getLight());
                if(deviceQuery.getTransport() != null){
                    deviceEntity.setTransport(deviceQuery.getTransport());
                }
                deviceEntity.setLightingDuration(deviceQuery.getLightingDuration());
                deviceEntity.setMorningHours(deviceQuery.getMorningHours());
            }
        }
        StringBuffer sb = new StringBuffer();
        if(StringUtils.isNotEmpty(deviceQuery.getDeviceId())){
            if(oldDeviceEntity.getLightingDuration() == null || (deviceQuery.getLightingDuration() != null && !oldDeviceEntity.getLightingDuration().equals(deviceQuery.getLightingDuration()))){
                sb.append("亮灯时长,");
                if(Integer.valueOf(deviceQuery.getLightingDuration()) > sum){
                    deviceQuery.setLightingDuration(sum.toString());
                }
            }
            if(oldDeviceEntity.getTransport() == null || (deviceQuery.getTransport() != null && !oldDeviceEntity.getTransport().equals(deviceQuery.getTransport()))){
                sb.append("运输模式,");
            }
            if(oldDeviceEntity.getMorningHours() == null || (deviceQuery.getMorningHours() != null && !oldDeviceEntity.getMorningHours().equals(deviceQuery.getMorningHours()))){
                sb.append("晨亮时长,");
                if(advancedSettingEntity != null && advancedSettingEntity.getTimeDown() != null){
                    if(Integer.valueOf(deviceQuery.getMorningHours()) > advancedSettingEntity.getTimeDown()){
                        deviceQuery.setMorningHours(advancedSettingEntity.getTimeDown().toString());
                    }
                }
            }
            if(oldDeviceEntity.getLight() == null || (deviceQuery.getLight() != null && !oldDeviceEntity.getLight().equals(deviceQuery.getLight()))){
                sb.append("亮度,");
            }
            if(oldDeviceEntity.getOnOff() == null || (deviceQuery.getOnOff() != null && !oldDeviceEntity.getOnOff().equals(deviceQuery.getOnOff()))){
                sb.append("开关,");
            }
        }
        List<String> deviceList = new ArrayList<>();
        List<Map<String,Object>> temp = new ArrayList<>();
        String userName = deviceQuery.getUpdateUserName();
        if(CollectionUtils.isNotEmpty(deviceEntityList)){
            for (DeviceEntity device : deviceEntityList){
                deviceList.add(device.getDeviceId());
                if(!"".equals(sb.toString())){
                    String str = sb.substring(0, sb.length() - 1);
                    FaultlogEntity faultlogEntity = new FaultlogEntity();
                    faultlogEntity.setProjectId(device.getProjectId());
                    if(StringUtils.isNotEmpty(device.getGroupId())){
                        faultlogEntity.setGroupId(device.getGroupId());
                    }
                    faultlogEntity.setFaultLogId(UUID.randomUUID().toString());
                    faultlogEntity.setDeviceId(device.getDeviceId());
                    faultlogEntity.setCreateTime(new Date());
                    faultlogEntity.setCreateUserId(deviceQuery.getCreateUser());
                    faultlogEntity.setLogStatus(FaultlogEnum.OPERATIONALLOG.getCode());
                    faultlogEntity.setFaultLogDesc(userName+"操作"+device.getDeviceCode()+"设备"+str);
                    faultlogService.addFaultlog(faultlogEntity);
                    //修改ES start

                    DeviceEntity deviceEntity = new DeviceEntity();
                    deviceEntity.setDeviceId(device.getDeviceId());
                    deviceEntity.setUpdateTime(new Date());
                    deviceEntity.setUpdateUser(device.getCreateUser());
                    deviceEntity.setOnOff(device.getOnOff());
                    deviceEntity.setLight(device.getLight());
                    //deviceEntity.setTransport(device.getTransport());  es 没有这个字段 待添加
                    deviceEntity.setLightingDuration(device.getLightingDuration());
                    deviceEntity.setMorningHours(device.getMorningHours());
                    Map<String, Object> stringObjectMap = esUtil.convertUpdateES(deviceEntity);
                    temp.add(stringObjectMap);
                    //修改ES  end

                }
            }
            esUtil.updateListES(temp,"device_index","deviceId");
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
    public void initAdvSet(AdvancedSettingEntity advancedSettingEntity,int n){
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

        advancedSettingEntity.setLigntOnDuration(5);
        advancedSettingEntity.setPvSwitch(1);

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
        advancedSettingEntity.setTemControlSwitch(0);
        advancedSettingEntity.setCustomeSwitch(0);
        if(n == 1){
            advancedSettingEntity.setVpv(500);
            advancedSettingEntity.setBatType(4);
            advancedSettingEntity.setBatStringNum(3);
            advancedSettingEntity.setVolOverDisCharge(900);
            advancedSettingEntity.setVolCharge(1260);
            advancedSettingEntity.setICharge(2000);
        }else if(n == 2){
            advancedSettingEntity.setVpv(200);
            advancedSettingEntity.setBatType(3);
            advancedSettingEntity.setBatStringNum(1);
            advancedSettingEntity.setVolOverDisCharge(280);
            advancedSettingEntity.setVolCharge(360);
            advancedSettingEntity.setICharge(1500);
            advancedSettingEntity.setLowPowerConsumption(0);
        }
    }
    @Override
    public DeviceResult queryDeviceByDeviceId(String deviceId) {
        logger.info("查询详情入参deviceId:"+deviceId);
        DeviceResult resut = new DeviceResult();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            DeviceEntity deviceEntity = deviceMapper.queryDeviceByDeviceId(deviceId);
            if(StringUtils.isNotEmpty(deviceEntity.getLightingDuration())){
                deviceEntity.setLightingDuration(Integer.valueOf(deviceEntity.getLightingDuration()).toString());
            }
            if(StringUtils.isNotEmpty(deviceEntity.getMorningHours())){
                deviceEntity.setMorningHours(Integer.valueOf(deviceEntity.getMorningHours()).toString());
            }
            String startTime = sdf.format(deviceEntity.getCreateTime());
            String endTime = sdf.format(new Date());
            long day = (sdf.parse(endTime).getTime() - sdf.parse(startTime).getTime()) /(24*60*60*1000);
            BeanUtils.copyProperties(deviceEntity, resut);
            resut.setRunDay(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resut;
    }

    @Override
    public boolean deleteDeviceCj(List<String> list) {
        return deviceMapper.deleteDeviceCj(list) > 0 ? true : false;
    }

}
