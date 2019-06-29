package com.remote.device.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.device.dao.DeviceMapper;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.entity.DeviceQuery;
import com.remote.device.service.DeviceService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author zhangwenping
 * @Date 2019/6/4 13:34
 * @Version 1.0
 **/
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;


    @Override
    public List<DeviceEntity> queryDeviceByGroupCount(List<String> groupIds, List<String> projectIds, Integer deviceStatus) {
        return deviceMapper.queryDeviceByGroupCount(groupIds,projectIds,deviceStatus);
    }

    @Override
    public boolean addDevice(DeviceEntity deviceEntity) {
        return deviceMapper.insert(deviceEntity) > 0 ? true : false;
    }

    @Override
    public PageInfo<DeviceEntity> queryDevice(DeviceQuery deviceQuery) {
        PageHelper.startPage(deviceQuery.getPageNum(),deviceQuery.getPageSize());
        List<DeviceEntity> list = deviceMapper.queryDevice(deviceQuery);
        PageInfo<DeviceEntity> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }


}
