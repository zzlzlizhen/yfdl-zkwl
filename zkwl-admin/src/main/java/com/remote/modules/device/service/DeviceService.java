package com.remote.modules.device.service;

import com.github.pagehelper.PageInfo;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.entity.DeviceQuery;
import com.remote.modules.device.entity.DeviceResult;
import com.remote.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;

public interface DeviceService {
    /*
     * @Author zhangwenping
     * @Description 根据分组查询设备（分页）
     * @Date 13:50 2019/6/4
     * @Param deviceQuery
     * @return PageInfo<DeviceEntity>
     **/
    PageInfo<DeviceEntity> queryDevice(DeviceQuery deviceQuery) throws Exception;
    /*
     * @Author zhangwenping
     * @Description 添加设备
     * @Date 17:04 2019/6/4
     * @Param deviceEntity
     * @return boolean
     **/
    boolean addDevice(DeviceEntity deviceEntity) throws Exception;
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
    boolean updateById(DeviceEntity deviceEntity) throws Exception;

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
    DeviceResult queryDeviceByDeviceId(String deviceId);
    /*
     * @Author zhangwenping
     * @Description 按照城市统计安装路灯数量
     * @Date 14:33 2019/6/20
     * @Param userId
     * @return  List<DeviceEntity>
     **/
    List<Map<String,Integer>> queryCountGroupByCity(Long userId);
    /*
     * @Author chengpunan
     * @Description //TODO
     * @Date 10:10 2019/6/24
     * @Param
     * @return
     **/
    int updateOnOffByIds(DeviceQuery deviceQuery);

    /**
     * 通过设备code查询组id
     * */
    String queryByDevCode(String deviceCode);
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
    int updateUserDevice(DeviceQuery deviceQuery);
    /**
     * 通过当前用户id查询所有子孙用户的设备数量
     * */
    Integer getDeviceCount(List<Long> userIds);
    /**
     * 通过当前用户所有用户的userIds查询故障数
     * */
    Integer getDevFailNum(List<Long> userIds);
    /**
     * 通过所有用户id查询出所有的设备codes
     * */
    List<String> getDeviceCode(List<Long> userIds);
    /**
     * 通过所有用户获取设备信息
     * */
    List<DeviceEntity> getDeviceInfoList(List<Long> userIds);
}
