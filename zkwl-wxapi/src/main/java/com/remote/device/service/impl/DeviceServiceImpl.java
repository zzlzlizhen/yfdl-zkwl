package com.remote.device.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.device.dao.DeviceMapper;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.entity.DeviceQuery;
import com.remote.device.service.DeviceService;

import com.remote.enums.CommunicationEnum;
import com.remote.enums.FaultlogEnum;
import com.remote.faultlog.entity.FaultlogEntity;
import com.remote.faultlog.service.FaultlogService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.remote.utils.DeviceTypeMap.DEVICE_TYPE;

/**
 * @Author zhangwenping
 * @Date 2019/6/4 13:34
 * @Version 1.0
 **/
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private FaultlogService faultlogService;


    @Override
    public List<DeviceEntity> queryDeviceByGroupCount(List<String> groupIds, List<String> projectIds, Integer deviceStatus) {
        return deviceMapper.queryDeviceByGroupCount(groupIds,projectIds,deviceStatus);
    }

    @Override
    public boolean addDevice(DeviceEntity deviceEntity) {
        String deviceCode = deviceEntity.getDeviceCode();
        //目前只有一种产品，2G 日后在添加其他产品
        deviceEntity.setCommunicationType(CommunicationEnum.NORMAL.getCode());
        String deviceType = deviceCode.substring(0, 4);
        if(DEVICE_TYPE.get(deviceType) != null){
            deviceEntity.setDeviceType(DEVICE_TYPE.get(deviceType).toString());
            return deviceMapper.insert(deviceEntity) > 0 ? true : false;
        }else{
            return false;
        }
    }

    @Override
    public PageInfo<DeviceEntity> queryDevice(DeviceQuery deviceQuery) {
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
        return deviceMapper.deleteDevice(deviceQuery) > 0 ? true : false;
    }

    @Override
    public boolean updateById(DeviceEntity deviceEntity) {
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
}
