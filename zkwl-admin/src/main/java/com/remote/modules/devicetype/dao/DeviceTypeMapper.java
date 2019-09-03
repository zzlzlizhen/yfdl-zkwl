package com.remote.modules.devicetype.dao;

import com.remote.modules.devicetype.entity.DeviceTypeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeviceTypeMapper {

    /*
     * @Author zhangwenping
     * @Description 根据设备类型编号查询
     * @Date 10:20 2019/8/21
     * @Param deviceTypeCode
     * @return DeviceTypeEntity
     **/
    DeviceTypeEntity getDeviceTypeByCode(@Param("deviceTypeCode") String deviceTypeCode, @Param("type") Integer type);
}
