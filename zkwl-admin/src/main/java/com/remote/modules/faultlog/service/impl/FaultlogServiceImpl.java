package com.remote.modules.faultlog.service.impl;

import com.remote.modules.faultlog.dao.FaultlogMapper;
import com.remote.modules.faultlog.entity.FaultlogEntity;
import com.remote.modules.faultlog.service.FaultlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/6/19 17:48
 * @Version 1.0
 **/
@Service
public class FaultlogServiceImpl implements FaultlogService {

    @Autowired
    private FaultlogMapper faultlogMapper;

    @Override
    public List<FaultlogEntity> queryFaultlogByDeviceId(String deviceId,Integer status) {
        return faultlogMapper.queryFaultlogByDeviceId(deviceId,status);
    }

    @Override
    public boolean addFaultlog(FaultlogEntity faultlogEntity) {
        return faultlogMapper.insert(faultlogEntity) > 0 ? true : false;
    }
}
