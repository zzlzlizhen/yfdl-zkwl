package com.remote.device.service;

import com.github.pagehelper.PageInfo;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.entity.DeviceQuery;

import java.util.List;

public interface DeviceService {

    /*
     * @Author zhangwenping
     * @Description 查询分组下设备数量
     * @Date 10:56 2019/6/18
     * @Param groupIds deviceStatus projectIds
     * @return  List<DeviceEntity>
     **/
    List<DeviceEntity> queryDeviceByGroupCount(List<String> groupIds, List<String> projectIds, Integer deviceStatus);

    /*
     * @Author zhangwenping
     * @Description  添加设备
     * @Date 14:03 2019/6/29
     * @Param deviceEntity
     * @return boolean
     **/
    boolean addDevice(DeviceEntity deviceEntity);
    /*
     * @Author zhangwenping
     * @Description 查询设备
     * @Date 14:05 2019/6/29
     * @Param  deviceQuery
     * @return PageInfo<DeviceEntity>
     **/
    PageInfo<DeviceEntity> queryDevice(DeviceQuery deviceQuery);
}
