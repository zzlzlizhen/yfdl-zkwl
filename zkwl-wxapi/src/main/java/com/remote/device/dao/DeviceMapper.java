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
    List<DeviceEntity> queryDeviceByGroupCount(@Param("groupIds") List<String> groupIds, @Param("projectIds") List<String> projectIds, @Param("deviceStatus") Integer deviceStatus);

    /*
     * @Author zhangwenping
     * @Description 根据分组查询设备信息
     * @Date 13:59 2019/6/4
     * @Param deviceQuery
     * @return List<DeviceEntity>
     **/
    List<DeviceEntity> queryDevice(@Param("deviceQuery") DeviceQuery deviceQuery);
}
