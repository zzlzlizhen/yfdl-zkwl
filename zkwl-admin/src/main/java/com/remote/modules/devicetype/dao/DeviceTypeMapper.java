package com.remote.modules.devicetype.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.modules.devicetype.entity.DeviceTypeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceTypeMapper extends BaseMapper<DeviceTypeEntity> {

    /*
     * @Author zhangwenping
     * @Description 根据设备类型编号查询
     * @Date 10:20 2019/8/21
     * @Param deviceTypeCode
     * @return DeviceTypeEntity
     **/
    DeviceTypeEntity getDeviceTypeByCode(@Param("deviceTypeCode") String deviceTypeCode, @Param("type") Integer type);
    /*
     * @Author zhangwenping
     * @Description 查询所有设备类型
     * @Date 14:56 2019/9/19
     * @Param type
     * @return  List<DeviceTypeEntity>
     **/
    List<DeviceTypeEntity> getDeviceType(@Param("type")Integer type);
}
