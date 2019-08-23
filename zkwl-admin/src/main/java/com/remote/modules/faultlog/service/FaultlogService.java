package com.remote.modules.faultlog.service;

import com.remote.modules.faultlog.entity.FaultlogEntity;

import java.util.List;

public interface FaultlogService {
    /*
     * @Author zhangwenping
     * @Description 根据设备查询操作日志
     * @Date 17:55 2019/6/19
     * @Param deviceId status
     * @return List<FaultlogEntity>
     **/
    List<FaultlogEntity> queryFaultlogByDeviceId(String deviceId,String groupId);
    /*
     * @Author zhangwenping
     * @Description 添加操作日志
     * @Date 18:07 2019/6/19
     * @Param faultlogEntity
     * @return boolean
     **/
    boolean addFaultlog(FaultlogEntity faultlogEntity);
}
