package com.remote.device.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.device.entity.DeviceEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceMapper extends BaseMapper<DeviceEntity> {

    /*
     * @Author zhangwenping
     * @Description 根据设备编号查询设备信息
     * @Date 13:29 2019/6/11
     * @Param deviceId
     * @return DeviceEntity
     **/
    DeviceEntity queryDeviceByCode(String deviceCode);
    /*
     * @Author zhangwenping
     * @Description 根据设备编号修改设备信息
     * @Date 13:29 2019/6/19
     * @Param deviceEntity
     * @return int
     **/
    int updateDeviceByCode(DeviceEntity deviceEntity);
    /*
     * @Author zhangwenping
     * @Description 超时修改状态 离线
     * @Date 17:18 2019/7/9
     * @Param deviceCode
     * @return int
     **/
    int updateDeviceTimeOutByCode(String deviceCode);
}
