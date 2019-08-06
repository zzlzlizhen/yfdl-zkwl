package com.remote.modules.device.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.entity.DeviceQuery;
import com.remote.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeviceMapper extends BaseMapper<DeviceEntity> {
    /*
     * @Author zhangwenping
     * @Description 根据分组查询设备信息
     * @Date 13:59 2019/6/4
     * @Param deviceQuery
     * @return List<DeviceEsEntity>
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
     * @Description 查询分组下设备各状态数量
     * @Date 11:23 2019/6/18
     * @Param groupIds deviceStatus projectIds
     * @return List<DeviceEsEntity>
     **/
    List<DeviceEntity> queryDeviceByGroupCount(@Param("groupIds") List<String> groupIds,@Param("projectId") String projectId,@Param("deviceStatus") Integer deviceStatus);
    /*
     * @Author zhangwenping
     * @Description 查询分组详情
     * @Date 17:26 2019/6/19
     * @Param deviceId
     * @return DeviceEsEntity
     **/
    DeviceEntity queryDeviceByDeviceId(@Param("deviceId")String deviceId);

    /*
     * @Author zhangwenping
     * @Description 根据城市统计项目下设备的数量
     * @Date 14:40 2019/6/20
     * @Param projectIds
     * @return List<DeviceEsEntity>
     **/
    List<Map<Object,Object>> queryDeviceGroupByCity(@Param("userIds") List<Long> userIds);
    /*
     * @Author zhagnwenping
     * @Description 批量开关灯
     * @Date 10:08 2019/6/24
     * @Param deviceQuery
     * @return int
     **/
    int updateOnOffByIds(@Param("deviceQuery")DeviceQuery deviceQuery);
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
     * @Description 修改创建人
     * @Date 18:32 2019/7/5
     * @Param deviceQuery
     * @return int
     **/
    int updateUserDevice(@Param("deviceQuery")DeviceQuery deviceQuery);



    /*
     * @Author zhagnwenping
     * @Description 根据项目或者分组统计其下所有设备
     * @Date 11:23 2019/6/18
     * @Param projectId groupId
     * @return List<DeviceEsEntity>
     **/
    List<DeviceEntity> queryRunStateCount(@Param("projectId")String projectId,@Param("groupId")String groupId);


    /**
     * 通过当前用户id查询所有子孙用户的设备数量
     * */
    Integer getDeviceCount(@Param("userIds")List<Long> userIds);
    /**
     * 通过当前用户查询所有子孙用户的设备故障数量
     */
    Integer getDevFailNum(@Param("userIds")List<Long> userIds);
    /**
     * 通过用户id获取用户下所有的设备code
     * */
    List<String> getDeviceCode(@Param("userIds")List<Long> userIds);
    /**
     * 通过当前用户的所拥有的userIds查询设备信息
     * */
    List<Map<Object,Object>> getDeviceInfoList(@Param("userIds")List<Long> userIds);

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
     * @return DeviceEsEntity
     **/
    DeviceEntity queryDeviceByGroupIdTopOne(@Param("groupId")String groupId);
    /*
     * @Author zhagnwenping
     * @Description 查询项目下设备各状态数量
     * @Date 11:23 2019/6/18
     * @Param groupIds deviceStatus projectIds
     * @return List<DeviceEsEntity>
     **/
    List<DeviceEntity> queryDeviceByProjectCount(@Param("projectIds") List<String> projectIds,@Param("deviceStatus") Integer deviceStatus);
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
     * @Description 批量修改设备状态
     * @Date 11:15 2019/7/25
     * @Param deviceCodes
     * @return int
     **/
    int updateDeviceRunStatus(@Param("deviceCodes") List<String> deviceCodes);
}

