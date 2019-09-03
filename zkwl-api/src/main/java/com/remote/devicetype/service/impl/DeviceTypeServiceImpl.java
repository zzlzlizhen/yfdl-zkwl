package com.remote.devicetype.service.impl;

import com.remote.devicetype.dao.DeviceTypeMapper;
import com.remote.devicetype.entity.DeviceTypeEntity;
import com.remote.devicetype.service.DeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;

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
    public DeviceTypeEntity getDeviceTypeByCode(String deviceTypeCode,Integer type) {
        return deviceTypeMapper.getDeviceTypeByCode(deviceTypeCode,type);
    }
}
