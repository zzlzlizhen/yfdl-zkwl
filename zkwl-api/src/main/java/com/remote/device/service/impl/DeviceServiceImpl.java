package com.remote.device.service.impl;

import com.remote.common.CommonEntity;
import com.remote.device.dao.DeviceMapper;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author EDZ
 * @Date 2019/6/11 9:25
 * @Version 1.0
 **/
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public DeviceEntity queryDeviceByCode(String deviceCode) {
        return deviceMapper.queryDeviceByCode(deviceCode);
    }

    @Override
    public int updateDeviceByCode(CommonEntity commonEntity, DeviceEntity deviceEntity) {
        //解析经纬度
        String latitudeInt = commonEntity.getLatitudeInt();//纬度整数
        String latitudeH = commonEntity.getLatitudeH();
        String latitudeL = commonEntity.getLatitudeL();
        deviceEntity.setLatitude(latitudeInt+"."+latitudeH+latitudeL);

        String longitudeInt = commonEntity.getLongitudeInt();//经度整数
        String longitudeH = commonEntity.getLongitudeH();
        String longitudeL = commonEntity.getLongitudeL();
        deviceEntity.setLongitude(longitudeInt+"."+longitudeH+longitudeL);


        //负载状态 loadState  蓄电池状态 batteryState 光电池状态  photocellState
        Integer loadState = deviceEntity.getLoadState();
        Integer batteryState = deviceEntity.getBatteryState();
        Integer photocellState = deviceEntity.getPhotocellState();
        //运行状态为正常
        if((loadState == 0 || loadState == 1) && (batteryState == 1 || batteryState == 2 || batteryState == 3) && (photocellState == 0 || photocellState == 1 || photocellState == 2)){
            deviceEntity.setRunState(1);
        }
        //运行状态为报警
        if((batteryState == 0 || batteryState == 6) && loadState == 6){
            deviceEntity.setRunState(2);
        }
        //运行状态为故障
        if((batteryState == 4 || batteryState == 5) && (loadState == 2 || loadState == 3 || loadState == 4 || loadState == 5)){
            deviceEntity.setRunState(3);
        }
        return deviceMapper.updateDeviceByCode(deviceEntity);
    }
}
