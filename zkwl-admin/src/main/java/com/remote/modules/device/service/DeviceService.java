package com.remote.modules.device.service;

import com.github.pagehelper.PageInfo;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.entity.DeviceQuery;

import java.util.List;
import java.util.Map;

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
    /*
     * @Author zhangwenping
     * @Description 查询分组下设备数量
     * @Date 10:56 2019/6/18
     * @Param groupIds deviceStatus projectIds
     * @return  List<DeviceEntity>
     **/
    List<DeviceEntity> queryDeviceByGroupCount(List<String> groupIds,List<String> projectIds,Integer deviceStatus);

    /*
     * @Author zhangwenping
     * @Description 查询分组详情
     * @Date 17:26 2019/6/19
     * @Param deviceId
     * @return DeviceEntity
     **/
    DeviceEntity queryDeviceByDeviceId(String deviceId);
    /*
     * @Author zhangwenping
     * @Description 按照城市统计安装路灯数量
     * @Date 14:33 2019/6/20
     * @Param userId
     * @return  List<DeviceEntity>
     **/
    List<DeviceEntity> queryCountGroupByCity(Long userId);
}
