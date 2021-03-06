package com.remote.device.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.device.entity.DeviceEntityApi;
import com.remote.device.entity.DeviceQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper extends BaseMapper<DeviceEntityApi> {

    /*
     * @Author zhangwenping
     * @Description 根据设备编号查询设备信息
     * @Date 13:29 2019/6/11
     * @Param deviceId
     * @return DeviceEntity
     **/
    DeviceEntityApi queryDeviceByCode(String deviceCode);
    /*
     * @Author zhangwenping
     * @Description 根据设备编号修改设备信息
     * @Date 13:29 2019/6/19
     * @Param deviceEntity
     * @return int
     **/
    int updateDeviceByCode(DeviceEntityApi deviceEntity);
    /*
     * @Author zhangwenping
     * @Description 超时修改状态 离线
     * @Date 17:18 2019/7/9
     * @Param deviceCode
     * @return int
     **/
    int updateDeviceTimeOutByCode(@Param("deviceCode") String deviceCode,@Param("runState") Integer runState);
    /*
     * @Author zhangwenping
     * @Description 查询设备信息
     * @Date 9:36 2019/7/15
     * @Param devicequery
     * @return List<DeviceEntity>
     **/
    List<DeviceEntityApi> queryDevice(DeviceQuery deviceQuery);
    /*
     * @Author zhangwenping
     * @Description 操作成功修改版本号
     * @Date 11:08 2019/7/30
     * @Param deviceCode
     * @return int
     **/
    int updateDeviceVersionByCode(String deviceCode);
    /*
     * @Author zhangwenping
     * @Description 修改升级GPRS标识
     * @Date 16:13 2019/8/22
     * @Param deviceCode
     * @return int
     **/
    int updateDeviceGprsFlag(@Param("deviceCode") String deviceCode,@Param("gprsFlag")Integer gprsFlag);
}
