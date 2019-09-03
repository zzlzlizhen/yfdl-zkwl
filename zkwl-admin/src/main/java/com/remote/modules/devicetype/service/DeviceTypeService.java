package com.remote.modules.devicetype.service;


import com.remote.modules.devicetype.entity.DeviceTypeEntity;

public interface DeviceTypeService {
    /*
     * @Author zhangwenping
     * @Description 根据设备类型编号查询
     * @Date 10:20 2019/8/21
     * @Param deviceTypeCode,type
     * @return DeviceTypeEntity
     **/
    DeviceTypeEntity getDeviceTypeByCode(String deviceTypeCode, Integer type);
}
