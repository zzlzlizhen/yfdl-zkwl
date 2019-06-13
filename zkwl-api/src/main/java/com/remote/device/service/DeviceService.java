package com.remote.device.service;

import com.remote.device.entity.DeviceEntity;

public interface DeviceService {
    /*
     * @Author zhangwenping
     * @Description 根据id查询设备信息
     * @Date 9:31 2019/6/11
     * @Param deviceId
     * @return DeviceEntity
     **/
    DeviceEntity queryDeviceByCode(String deviceCode);
}
