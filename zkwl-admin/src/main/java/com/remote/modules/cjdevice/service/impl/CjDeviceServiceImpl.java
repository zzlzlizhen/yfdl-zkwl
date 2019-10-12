package com.remote.modules.cjdevice.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.modules.cjdevice.dao.CjDeviceMapper;
import com.remote.modules.cjdevice.entity.CjDevice;
import com.remote.modules.cjdevice.service.CjDeviceService;
import com.remote.modules.device.entity.DeviceEntity;
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

    @Autowired
    private CjDeviceMapper cjDeviceMapper;

    @Override
    public boolean saveCjDevice(CjDevice cjDevice) {
        return cjDeviceMapper.insert(cjDevice) > 0 ? true : false;
    }

    @Override
    public boolean deleteCjDeviceByCodes(List<String> deviceCodes) {
        return cjDeviceMapper.deleteCjDeviceByCodes(deviceCodes) > 0 ? true : false;
    }

    @Override
    public boolean updateById(CjDevice cjDevice) {
        return cjDeviceMapper.updateByDeviceId(cjDevice)  > 0 ? true : false;
    }

    @Override
    public CjDevice queryCjDeviceByDeviceCode(String deviceCode) {
        return cjDeviceMapper.queryCjDeviceByDeviceCode(deviceCode);
    }

    @Override
    public PageInfo<CjDevice> queryDeviceByMysql(Integer pageNum,Integer pageSize,Long userId) {
        PageHelper.startPage(pageNum,pageSize);
        List<CjDevice> list = cjDeviceMapper.queryDeviceByMysql(userId);
        PageInfo<CjDevice> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}
