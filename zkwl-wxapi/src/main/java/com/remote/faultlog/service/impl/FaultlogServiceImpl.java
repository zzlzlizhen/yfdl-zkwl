package com.remote.faultlog.service.impl;

import com.remote.faultlog.dao.FaultlogMapper;
import com.remote.faultlog.entity.FaultlogEntity;
import com.remote.faultlog.service.FaultlogService;
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
    public List<FaultlogEntity> queryFaultlogByDeviceId(String deviceId, Integer status, String groupId) {
        return faultlogMapper.queryFaultlogByDeviceId(deviceId,status,groupId);
    }

    @Override
    public boolean addFaultlog(FaultlogEntity faultlogEntity) {
        return faultlogMapper.insert(faultlogEntity) > 0 ? true : false;
    }
}
