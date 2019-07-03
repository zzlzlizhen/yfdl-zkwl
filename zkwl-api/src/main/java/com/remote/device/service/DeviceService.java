package com.remote.device.service;

import com.remote.common.CommonEntity;
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
    /*
     * @Author zhangwenping
     * @Description 根据设备编号修改设备信息
     * @Date 13:28 2019/6/19
     * @Param deviceEntity
     * @return int
     **/
    int updateDeviceByCode(CommonEntity commonEntity,DeviceEntity deviceEntity);
}
