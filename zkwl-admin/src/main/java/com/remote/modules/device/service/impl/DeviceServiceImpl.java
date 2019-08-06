package com.remote.modules.device.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.common.enums.CommunicationEnum;
import com.remote.common.enums.FaultlogEnum;
import com.remote.common.utils.DeviceTypeMap;
import com.remote.common.utils.R;
import com.remote.common.utils.ValidateUtils;
import com.remote.modules.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.modules.advancedsetting.service.AdvancedSettingService;
import com.remote.modules.device.dao.DeviceMapper;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.entity.DeviceQuery;
import com.remote.modules.device.entity.DeviceResult;
import com.remote.modules.device.service.DeviceService;
import com.remote.modules.district.entity.DistrictEntity;
import com.remote.modules.district.service.DistrictService;
import com.remote.modules.faultlog.entity.FaultlogEntity;
import com.remote.modules.faultlog.service.FaultlogService;
import com.remote.modules.group.dao.GroupMapper;
import com.remote.modules.group.entity.GroupEntity;
import com.remote.modules.group.entity.GroupQuery;
import com.remote.modules.group.service.GroupService;
import com.remote.modules.group.service.impl.GroupServiceImpl;
import com.remote.modules.project.dao.ProjectMapper;
import com.remote.modules.project.entity.ProjectEntity;
import com.remote.modules.project.entity.ProjectQuery;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.remote.common.utils.DeviceTypeMap.DEVICE_TYPE;

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
    private SysUserService sysUserService;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private FaultlogService faultlogService;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private AdvancedSettingService advancedSettingService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private GroupService groupService;

    @Override
    public PageInfo<DeviceEntity> queryDevice(DeviceQuery deviceQuery) throws Exception {
        logger.info("查询设备信息："+JSONObject.toJSONString(deviceQuery));
        PageHelper.startPage(deviceQuery.getPageNum(),deviceQuery.getPageSize());
        List<DeviceEntity> list = deviceMapper.queryDevice(deviceQuery);
        GroupQuery groupQuery = new GroupQuery();
        groupQuery.setProjectId(deviceQuery.getProjectId());
        List<GroupEntity> groupList = groupMapper.queryGroupByName(groupQuery);
        Map<String,String> map = new HashMap<>();
        if(CollectionUtils.isNotEmpty(groupList)){
            for(GroupEntity groupEntity : groupList){
                map.put(groupEntity.getGroupId(),groupEntity.getGroupName());
            }
        }
        if(CollectionUtils.isNotEmpty(list)){
            for(DeviceEntity deviceEntity : list){
                deviceEntity.setGroupName(map.get(deviceEntity.getGroupId()));
                if(DeviceTypeMap.DEVICE_TYPE.get(deviceEntity.getDeviceType()) != null){
                    deviceEntity.setDeviceTypeName(DeviceTypeMap.DEVICE_TYPE.get(deviceEntity.getDeviceType()));
                }
            }
        }
        PageInfo<DeviceEntity> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public boolean addDevice(DeviceEntity deviceEntity) throws Exception {
        logger.info("添加设备信息："+JSONObject.toJSONString(deviceEntity));
        ValidateUtils.validate(deviceEntity,Arrays.asList("deviceCode","deviceName"));
        //目前只有一种产品，2G 日后在添加其他产品
        deviceEntity.setCommunicationType(CommunicationEnum.NORMAL.getCode());
        ProjectEntity projectEntity = projectMapper.queryProjectMap(deviceEntity.getProjectId());
        if(projectEntity.getCityId() == null){
            logger.info("所属项目没有城市");
        }else{
            DistrictEntity districtEntity = districtService.queryDistrictById(projectEntity.getCityId());
            deviceEntity.setCityName(districtEntity.getDistrictName());
        }
        //添加设备分类名称
        deviceEntity.setDeviceType("1");
        deviceEntity.setDeviceTypeName(DeviceTypeMap.DEVICE_TYPE.get("1"));
        //添加分组名称
        String groupId = deviceEntity.getGroupId();
        GroupEntity groupEntity = groupService.queryGroupById(groupId);
        deviceEntity.setGroupName(groupEntity.getGroupName());
        return deviceMapper.insert(deviceEntity) > 0 ? true : false;
    }

    @Override
    public boolean deleteDevice(DeviceQuery deviceQuery) {
        return deviceMapper.deleteDevice(deviceQuery) > 0 ? true : false;
    }

    @Override
    public boolean moveGroup(DeviceQuery deviceQuery) {
        logger.info("移动设备信息："+JSONObject.toJSONString(deviceQuery));
        List<DeviceEntity> deviceEntityList = deviceMapper.queryDeviceByDeviceIds(deviceQuery.getDeviceList());
        List<String> deviceCodes = deviceEntityList.parallelStream().map(deviceEntity -> deviceEntity.getDeviceCode()).collect(Collectors.toCollection(ArrayList::new));
        advancedSettingService.updateAdvancedByDeviceCodes(deviceCodes,deviceQuery.getGroupId(),deviceEntityList.get(0).getGroupId());
        return deviceMapper.deleteDevice(deviceQuery) > 0 ? true : false;
    }

    @Override
    public R updateById(DeviceEntity deviceEntity) throws Exception {
        logger.info("修改设备信息："+JSONObject.toJSONString(deviceEntity));
        String groupId = deviceEntity.getGroupId();
        DeviceEntity device = deviceMapper.queryDeviceByDeviceId(deviceEntity.getDeviceId());
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
        return R.ok(deviceMapper.updateById(deviceEntity) > 0 ? true : false);
    }

    @Override
    public List<DeviceEntity> queryDeviceNoPage(DeviceQuery deviceQuery) {
        return deviceMapper.queryDevice(deviceQuery);
    }


    @Override
    public List<DeviceEntity> queryDeviceByProjectCount(List<String> projectIds, Integer deviceStatus) {
        return deviceMapper.queryDeviceByProjectCount(projectIds,deviceStatus);
    }

    @Override
    public List<DeviceEntity> queryDeviceByGroupCount(List<String> groupIds,String projectId, Integer deviceStatus) {
        return deviceMapper.queryDeviceByGroupCount(groupIds,projectId,deviceStatus);
    }

    @Override
    public DeviceResult queryDeviceByDeviceId(String deviceId) {
        DeviceResult resut = new DeviceResult();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            DeviceEntity deviceEntity = deviceMapper.queryDeviceByDeviceId(deviceId);
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
    public List<Map<Object,Object>> queryCountGroupByCity(Long userId) {
        List<SysUserEntity> userList = sysUserService.queryAllLevel(userId);
        if(CollectionUtils.isNotEmpty(userList)){
            List<Long> userIds = userList.parallelStream().map(sysUserEntity -> sysUserEntity.getUserId()).collect(Collectors.toCollection(ArrayList::new));
            return deviceMapper.queryDeviceGroupByCity(userIds);
        }
        return null;
    }


    @Override
    public int updateOnOffByIds(DeviceQuery deviceQuery) {
        logger.info("修改设备添加日志："+JSONObject.toJSONString(deviceQuery));
        DeviceEntity oldDeviceEntity = deviceMapper.queryDeviceByDeviceId(deviceQuery.getDeviceId());
        Integer sum = 0;
        AdvancedSettingEntity advancedSettingEntity = null;
        if(StringUtils.isNotEmpty(deviceQuery.getGroupId())&&StringUtils.isNotEmpty(deviceQuery.getDeviceCode())|| !"0".equals(deviceQuery.getDeviceCode())){
            advancedSettingEntity = advancedSettingService.queryByDevOrGroupId(deviceQuery.getGroupId(), deviceQuery.getDeviceCode());
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

        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        //代表操作单个设备开关
        if(StringUtils.isNotEmpty(deviceQuery.getDeviceId())){
            DeviceEntity deviceEntity = deviceMapper.queryDeviceByDeviceId(deviceQuery.getDeviceId());
            deviceEntityList.add(deviceEntity);
        }else{
            deviceEntityList = deviceMapper.queryDevice(deviceQuery);
        }
        StringBuffer sb = new StringBuffer();
        if(deviceQuery.getLightingDuration() != null && !oldDeviceEntity.getLightingDuration().equals(deviceQuery.getLightingDuration())){
            sb.append("亮灯时长,");
            if(Integer.valueOf(deviceQuery.getLightingDuration()) > sum){
                deviceQuery.setLightingDuration(sum.toString());
            }
        }
        if(deviceQuery.getMorningHours() != null && !oldDeviceEntity.getMorningHours().equals(deviceQuery.getMorningHours())){
            sb.append("晨亮时长,");
            if(advancedSettingEntity.getTimeDown() != null){
                if(Integer.valueOf(deviceQuery.getMorningHours()) > advancedSettingEntity.getTimeDown()){
                    deviceQuery.setMorningHours(advancedSettingEntity.getTimeDown().toString());
                }
            }
        }
        if(deviceQuery.getLight() != null && !oldDeviceEntity.getLight().equals(deviceQuery.getLight())){
            sb.append("亮度,");
        }
        if(deviceQuery.getOnOff() != null && !oldDeviceEntity.getOnOff().equals(deviceQuery.getOnOff())){
            sb.append("开关,");
        }
        List<String> deviceList = new ArrayList<>();
        String userName = deviceQuery.getUpdateUserName();
        if(CollectionUtils.isNotEmpty(deviceEntityList)){
            for (DeviceEntity device : deviceEntityList){
                if(!"".equals(sb.toString())){
                    String str = sb.substring(0, sb.length() - 1);
                    deviceList.add(device.getDeviceId());
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
                }
            }
        }
        deviceQuery.setDeviceList(deviceList);
        return deviceMapper.updateOnOffByIds(deviceQuery);
    }

    @Override
    public String queryByDevCode(String deviceCode) {
        return deviceMapper.queryByDevCode(deviceCode);
    }

    @Override
    public List<String> queryByGroupId(String groupId) {
        return this.deviceMapper.queryByGroupId(groupId);
    }

    @Override
    public int updateUserDevice(DeviceQuery deviceQuery) {
        return deviceMapper.updateUserDevice(deviceQuery);
    }

    @Override
    public List<DeviceEntity> queryRunStateCount(String projectId, String groupId) {
        return deviceMapper.queryRunStateCount(projectId,groupId);
    }

    /**
     * 通过当前用户id查询所有用户的设备数量
     * */
    @Override
    public Integer getDeviceCount(List<Long> curUids) {

        return this.deviceMapper.getDeviceCount(curUids);
    }

    /**
     * 通过用户id
     * */
    @Override
    public Integer getDevFailNum(List<Long> curUserIds) {
        return this.deviceMapper.getDevFailNum(curUserIds);
    }

    @Override
    public List<String> getDeviceCode(List<Long> userIds) {
        return this.deviceMapper.getDeviceCode(userIds);
    }
    /**
     * 通过所有用户id获取当前设备信息
     * */
    @Override
    public List<Map<Object,Object>> getDeviceInfoList(List<Long> userIds){
        return this.deviceMapper.getDeviceInfoList(userIds);
    }

    @Override
    public int getDeviceByDeviceCode(String deviceCode) {
        return deviceMapper.getDeviceByDeviceCode(deviceCode);
    }

    @Override
    public DeviceEntity queryDeviceByGroupIdTopOne(String groupId) {
       return deviceMapper.queryDeviceByGroupIdTopOne(groupId);
    }

    @Override
    public boolean updateDeviceRunStatus(List<String> deviceCodes) {
        return deviceMapper.updateDeviceRunStatus(deviceCodes) > 0 ? true :false;
    }
}
