package com.remote.devicetype.service;



import com.remote.devicetype.entity.DeviceTypeEntity;

import java.util.List;

public interface DeviceTypeService {
    /*
     * @Author zhangwenping
     * @Description 根据设备类型编号查询
     * @Date 10:20 2019/8/21
     * @Param deviceTypeCode,type
     * @return DeviceTypeEntity
     **/
    DeviceTypeEntity getDeviceTypeByCode(String deviceTypeCode, Integer type);

    /*
     * @Author zhangwenping
     * @Description 添加设备类型
     * @Date 14:54 2019/9/19
     * @Param deviceTypeEntity
     * @return boolean
     **/
    boolean addDeviceType(DeviceTypeEntity deviceTypeEntity);
    /*
     * @Author zhangwenping
     * @Description 查询所有设备类型
     * @Date 14:56 2019/9/19
     * @Param type
     * @return  List<DeviceTypeEntity>
     **/
    List<DeviceTypeEntity> getDeviceType(Integer type);
}
