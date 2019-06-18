package com.remote.modules.device.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.entity.DeviceQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper extends BaseMapper<DeviceEntity> {
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
     * @Description 查询分组下设备各状态数量
     * @Date 11:23 2019/6/18
     * @Param groupIds deviceStatus projectIds
     * @return List<DeviceEntity>
     **/
    List<DeviceEntity> queryDeviceByGroupCount(@Param("groupIds") List<String> groupIds,@Param("projectIds") List<String> projectIds,@Param("deviceStatus") Integer deviceStatus);
}
