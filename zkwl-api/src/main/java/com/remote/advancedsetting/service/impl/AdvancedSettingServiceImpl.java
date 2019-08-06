package com.remote.advancedsetting.service.impl;


import com.remote.advancedsetting.dao.AdvancedSettingDao;
import com.remote.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.advancedsetting.service.AdvancedSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdvancedSettingServiceImpl implements AdvancedSettingService {

    @Autowired
    private AdvancedSettingDao advancedSettingDao;

    @Override
    public AdvancedSettingEntity queryByDeviceCode(String deviceCode) {
        return advancedSettingDao.queryByDeviceCode(deviceCode);
    }
}
