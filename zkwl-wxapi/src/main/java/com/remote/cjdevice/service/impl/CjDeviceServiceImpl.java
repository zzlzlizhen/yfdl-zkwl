package com.remote.cjdevice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.cjdevice.dao.CjDeviceMapper;
import com.remote.cjdevice.entity.CjDevice;
import com.remote.cjdevice.service.CjDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/9/6 10:57
 * @Version 1.0
 **/
@Service
public class CjDeviceServiceImpl implements CjDeviceService {

    private Logger logger = LoggerFactory.getLogger(CjDeviceServiceImpl.class);

    @Autowired
    private CjDeviceMapper cjDeviceMapper;

    @Override
    public boolean saveCjDevice(CjDevice cjDevice) {
        CjDevice oldCjDevice = cjDeviceMapper.queryCjDeviceByDeviceCode(cjDevice.getDeviceCode());
        if(oldCjDevice != null){
            return cjDeviceMapper.updateByDeviceId(cjDevice) > 0 ? true : false;
        }
        return cjDeviceMapper.insert(cjDevice) > 0 ? true : false;
    }

    @Override
    public boolean deleteCjDeviceByCodes(List<String> deviceCodes) {
        logger.info("厂家删除设备入参："+JSONObject.toJSONString(deviceCodes));
        return cjDeviceMapper.deleteCjDeviceByCodes(deviceCodes) > 0 ? true : false;
    }

    @Override
    public boolean updateById(CjDevice cjDevice) {
        logger.info("厂家修改设备入参："+JSONObject.toJSONString(cjDevice));
        return cjDeviceMapper.updateByDeviceId(cjDevice)  > 0 ? true : false;
    }

    @Override
    public CjDevice queryCjDeviceByDeviceCode(String deviceCode) {
        return cjDeviceMapper.queryCjDeviceByDeviceCode(deviceCode);
    }

    @Override
    public PageInfo<CjDevice> queryDeviceByMysql(Integer pageNum, Integer pageSize, Long userId) {
        PageHelper.startPage(pageNum,pageSize);
        List<CjDevice> list = cjDeviceMapper.queryDeviceByMysql(userId);
        PageInfo<CjDevice> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}
