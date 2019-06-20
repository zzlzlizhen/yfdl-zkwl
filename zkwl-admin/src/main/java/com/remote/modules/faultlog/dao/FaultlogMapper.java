package com.remote.modules.faultlog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.modules.faultlog.entity.FaultlogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FaultlogMapper extends BaseMapper<FaultlogEntity> {

    /*
     * @Author zhangwenping
     * @Description 根据设备id 查询设备操作日志
     * @Date 17:56 2019/6/19
     * @Param deviceId
     * @return List<FaultlogEntity>
     **/
    List<FaultlogEntity> queryFaultlogByDeviceId(@Param("deviceId") String deviceId,@Param("logStatus") Integer logStatus);
}
