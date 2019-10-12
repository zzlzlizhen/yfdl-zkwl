package com.remote.modules.devicetype.service.impl;

import com.remote.modules.devicetype.dao.DeviceTypeMapper;
import com.remote.modules.devicetype.entity.DeviceTypeEntity;
import com.remote.modules.devicetype.service.DeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/8/21 10:17
 * @Version 1.0
 **/
@Service
public class DeviceTypeServiceImpl implements DeviceTypeService {

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Override
    public DeviceTypeEntity getDeviceTypeByCode(String deviceTypeCode, Integer type) {
        return deviceTypeMapper.getDeviceTypeByCode(deviceTypeCode,type);
    }

    @Override
    public boolean addDeviceType(DeviceTypeEntity deviceTypeEntity) {
        return deviceTypeMapper.insert(deviceTypeEntity) > 0 ? true : false;
    }

    @Override
    public List<DeviceTypeEntity> getDeviceType(Integer type) {
        return deviceTypeMapper.getDeviceType(type);
    }
}
