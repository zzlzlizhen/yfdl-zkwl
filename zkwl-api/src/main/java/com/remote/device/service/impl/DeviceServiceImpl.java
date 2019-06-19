package com.remote.device.service.impl;

import com.remote.device.dao.DeviceMapper;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author EDZ
 * @Date 2019/6/11 9:25
 * @Version 1.0
 **/
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public DeviceEntity queryDeviceByCode(String deviceCode) {
        return deviceMapper.queryDeviceByCode(deviceCode);
    }

    @Override
    public int updateDeviceByCode(DeviceEntity deviceEntity) {
        return deviceMapper.updateDeviceByCode(deviceEntity);
    }
}
