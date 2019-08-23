package com.remote.device.service;

import com.remote.common.CommonEntity;
import com.remote.device.entity.DeviceEntityApi;
import com.remote.device.entity.DeviceQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceService {
    /*
     * @Author zhangwenping
     * @Description 根据id查询设备信息
     * @Date 9:31 2019/6/11
     * @Param deviceId
     * @return DeviceEntity
     **/
    DeviceEntityApi queryDeviceByCode(String deviceCode);
    /*
     * @Author zhangwenping
     * @Description 根据设备编号修改设备信息
     * @Date 13:28 2019/6/19
     * @Param deviceEntity
     * @return int
     **/
    int updateDeviceByCode(CommonEntity commonEntity,DeviceEntityApi deviceEntity);

    /*
     * @Author zhangwenping
     * @Description 超时修改状态
     * @Date 17:17 2019/7/9
     * @Param deviceCode
     * @return  int
     **/
    int updateDeviceTimeOutByCode(String deviceCode,Integer runState);
    /*
     * @Author zhangwenping
     * @Description 查询设备
     * @Date 9:35 2019/7/15
     * @Param deviceQuery
     * @return List<DeviceEntity>
     **/
    List<DeviceEntityApi> queryDeviceNoPage(DeviceQuery deviceQuery);
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
    int updateDeviceGprsFlag(String deviceCode,Integer gprsFlag);
}
