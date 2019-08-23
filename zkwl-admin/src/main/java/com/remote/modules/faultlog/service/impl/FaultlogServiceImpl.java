package com.remote.modules.faultlog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.remote.modules.faultlog.dao.FaultlogMapper;
import com.remote.modules.faultlog.entity.FaultlogEntity;
import com.remote.modules.faultlog.service.FaultlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(FaultlogServiceImpl.class);

    @Autowired
    private FaultlogMapper faultlogMapper;

    @Override
    public List<FaultlogEntity> queryFaultlogByDeviceId(String deviceId,String groupId) {
        return faultlogMapper.queryFaultlogByDeviceId(deviceId,groupId);
    }

    @Override
    public boolean addFaultlog(FaultlogEntity faultlogEntity) {
        logger.info("添加操作日志："+JSONObject.toJSONString(faultlogEntity));
        return faultlogMapper.insert(faultlogEntity) > 0 ? true : false;
    }
}
