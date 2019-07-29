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
    List<DeviceEntity> queryDeviceByGroupCount(List<String> groupIds,String projectId,Integer deviceStatus);
    /*
     * @Author zhagnwenping
     * @Description 查询项目下设备各状态数量
     * @Date 11:23 2019/6/18
     * @Param groupIds deviceStatus projectIds
     * @return List<DeviceEntity>
     **/
    List<DeviceEntity> queryDeviceByProjectCount(List<String> projectIds,Integer deviceStatus);

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
     * @Description 删除设备
     * @Date 16:52 2019/7/3
     * @Param
     * @return
     **/
    boolean deleteDevice(DeviceQuery deviceQuery);

    /*
     * @Author zhangwenping
     * @Description 修改设备信息
     * @Date 15:32 2019/6/5
     * @Param deviceEntity
     * @return boolean
     **/
    boolean updateById(DeviceEntity deviceEntity);

    /*
     * @Author chengpunan
     * @Description //TODO
     * @Date 10:10 2019/6/24
     * @Param
     * @return
     **/
    int updateOnOffByIds(DeviceQuery deviceQuery);

    /*
     * @Author zhangwenping
     * @Description 根据编号查询
     * @Date 9:40 2019/7/12
     * @Param deviceCode
     * @return int
     **/
    int getDeviceByDeviceCode(String deviceCode);

    /*
     * @Author zhangwenping
     * @Description 根据分组查询设备的第一条
     * @Date 16:37 2019/7/17
     * @Param groupId
     * @return DeviceEntity
     **/
    DeviceEntity queryDeviceByGroupIdTopOne(String groupId);
}
