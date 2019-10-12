package com.remote.device.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.entity.DeviceQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper extends BaseMapper<DeviceEntity> {

    /*
     * @Author zhagnwenping
     * @Description 查询分组下设备各状态数量
     * @Date 11:23 2019/6/18
     * @Param groupIds deviceStatus projectIds
     * @return List<DeviceEntity>
     **/
    List<DeviceEntity> queryDeviceByGroupCount(@Param("groupIds") List<String> groupIds,@Param("projectId") String projectId,@Param("deviceStatus") Integer deviceStatus);
    /*
     * @Author zhagnwenping
     * @Description 查询项目下设备各状态数量
     * @Date 11:23 2019/6/18
     * @Param groupIds deviceStatus projectIds
     * @return List<DeviceEntity>
     **/
    List<DeviceEntity> queryDeviceByProjectCount(@Param("projectIds") List<String> projectIds,@Param("deviceStatus") Integer deviceStatus);

    /*
     * @Author zhangwenping
     * @Description 根据分组查询设备信息
     * @Date 13:59 2019/6/4
     * @Param deviceQuery
     * @return List<DeviceEntity>
     **/
    List<DeviceEntity> queryDevice(@Param("deviceQuery") DeviceQuery deviceQuery);

    /*
     * @Author zhangwenping
     * @Description 删除设备
     * @Date 11:41 2019/6/5
     * @Param deviceQuery
     * @return int
     **/
    int deleteDevice(@Param("deviceQuery")DeviceQuery deviceQuery);

    /*
     * @Author zhangwenping
     * @Description 修改设备
     * @Date 11:41 2019/6/5
     * @Param deviceEntity
     * @return int
     **/
    int updateById(DeviceEntity deviceEntity);

    /*
     * @Author zhagnwenping
     * @Description 批量开关灯
     * @Date 10:08 2019/6/24
     * @Param deviceQuery
     * @return int
     **/
    int updateOnOffByIds(@Param("deviceQuery")DeviceQuery deviceQuery);

    /*
     * @Author zhangwenping
     * @Description 根据编号查询
     * @Date 9:40 2019/7/12
     * @Param deviceCode
     * @return int
     **/
    int getDeviceByDeviceCode(@Param("deviceCode")String deviceCode);
    /*
     * @Author zhangwenping
     * @Description 根据分组查询设备的第一条
     * @Date 16:37 2019/7/17
     * @Param groupId
     * @return DeviceEntity
     **/
    DeviceEntity queryDeviceByGroupIdTopOne(@Param("groupId")String groupId);
    /**
     * 通过当前用户id查询所有子孙用户的设备数量
     * */
    Integer getDeviceCount(@Param("userIds")List<Long> userIds);
    /**
     * 通过设备ids获取所属用户ids
     * */
    List<Long> queryExclUserId(@Param("deviceIds")List<String> deviceIds);
    /**
     * 通过设备code去查询组id
     * */
    String queryByDevCode(@Param("deviceCode")String deviceCode);
    /**
     * 通过组id查询所有的设备code
     * */
    List<String> queryByGroupId(@Param("groupId")String groupId);
    /*
 * @Author zhangwenping
 * @Description 通过ids查询详情
 * @Date 16:26 2019/7/24
 * @Param deviceIds
 * @return  List<DeviceEsEntity>
 **/
    List<DeviceEntity> queryDeviceByDeviceIds(@Param("deviceIds") List<String> deviceIds);
    /*
 * @Author zhangwenping
 * @Description 查询分组详情
 * @Date 17:26 2019/6/19
 * @Param deviceId
 * @return DeviceEsEntity
 **/
    DeviceEntity queryDeviceByDeviceId(@Param("deviceId")String deviceId);
    /*
     * @Author zhangwenpiung
     * @Description 厂家删除设备
     * @Date 17:34 2019/8/31
     * @Param deviceList
     * @return int
     **/
    int deleteDeviceCj(@Param("deviceList")List<String> deviceList);
    /*
 * @Author zhangwenping
 * @Description 通过code获取设备
 * @Date 10:18 2019/8/27
 * @Param deviceCodes
 * @return  List<DeviceEntity>
 **/
    List<DeviceEntity> queryDeviceByCodes(@Param("deviceCodes") List<String> deviceCodes);
}

