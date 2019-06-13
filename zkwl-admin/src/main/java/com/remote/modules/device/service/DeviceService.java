package com.remote.modules.device.service;

import com.github.pagehelper.PageInfo;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.entity.DeviceQuery;

import java.util.List;

public interface DeviceService {
    /*
     * @Author zhangwenping
     * @Description 根据分组查询设备（分页）
     * @Date 13:50 2019/6/4
     * @Param deviceQuery
     * @return PageInfo<DeviceEntity>
     **/
    PageInfo<DeviceEntity> queryDevice(DeviceQuery deviceQuery);
    /*
     * @Author zhangwenping
     * @Description 添加设备
     * @Date 17:04 2019/6/4
     * @Param deviceEntity
     * @return boolean
     **/
    boolean addDevice(DeviceEntity deviceEntity);
    /*
     * @Author zhangwenping
     * @Description 删除设备
     * @Date 10:53 2019/6/5
     * @Param deviceQuery
     * @return boolean
     **/
    boolean deleteDevice(DeviceQuery deviceQuery);
    /*
     * @Author zhangwenping
     * @Description 移动分组
     * @Date 15:15 2019/6/5
     * @Param deviceQuery
     * @return boolean
     **/
    boolean moveGroup(DeviceQuery deviceQuery);
    /*
     * @Author zhangwenping
     * @Description 修改设备信息
     * @Date 15:32 2019/6/5
     * @Param deviceEntity
     * @return boolean
     **/
    boolean updateById(DeviceEntity deviceEntity);

    /*
     * @Author zhangwenping
     * @Description 查询设备信息 不分页
     * @Date 13:29 2019/6/6
     * @Param deviceQuery
     * @return  List<DeviceEntity>
     **/
    List<DeviceEntity> queryDeviceNoPage(DeviceQuery deviceQuery);
}
