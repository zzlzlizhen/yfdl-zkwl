package com.remote.faultlog.service;


import com.remote.faultlog.entity.FaultlogEntity;

import java.util.List;

public interface FaultlogService {
    /*
     * @Author zhangwenping
     * @Description 添加操作日志
     * @Date 18:07 2019/6/19
     * @Param faultlogEntity
     * @return boolean
     **/
    boolean addFaultlog(FaultlogEntity faultlogEntity);
}
