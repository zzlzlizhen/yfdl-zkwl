package com.remote.device.service.impl;

import com.remote.common.CommonEntity;
import com.remote.common.enums.BatteryStatusEnum;
import com.remote.common.enums.LoadStatusEnum;
import com.remote.common.enums.PhotovoltaicCellStatusEnum;
import com.remote.common.enums.RunStatusEnum;
import com.remote.device.dao.DeviceMapper;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.entity.DeviceQuery;
import com.remote.device.service.DeviceService;
import com.remote.device.util.XYmatch;
import com.remote.faultlog.entity.FaultlogEntity;
import com.remote.faultlog.service.FaultlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author EDZ
 * @Date 2019/6/11 9:25
 * @Version 1.0
 **/
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private FaultlogService faultlogService;

    @Override
    public DeviceEntity queryDeviceByCode(String deviceCode) {
        return deviceMapper.queryDeviceByCode(deviceCode);
    }

    @Override
    public int updateDeviceByCode(CommonEntity commonEntity, DeviceEntity deviceEntity) {

        DeviceEntity entity = deviceMapper.queryDeviceByCode(deviceEntity.getDeviceCode());


        //解析经纬度
        String latitudeInt = commonEntity.getLatitudeInt();//纬度整数
        String latitudeH = commonEntity.getLatitudeH();
        int i = Integer.valueOf(latitudeH) << 16;
        String latitudeL = commonEntity.getLatitudeL();
        double latitudeUp = Double.valueOf(latitudeInt+"."+i+latitudeL);


        String longitudeInt = commonEntity.getLongitudeInt();//经度整数
        String longitudeH = commonEntity.getLongitudeH();
        int h = Integer.valueOf(longitudeH) << 16;
        String longitudeL = commonEntity.getLongitudeL();
        double longitudeUp = Double.valueOf(longitudeInt+"."+h+longitudeL);

        //经纬度不为空的话，代表是手机扫码添加  否则代表页面添加，页面添加经纬度为空
        if(entity.getLongitude() != null && entity.getLatitude() != null){
            //原来经度
            String longitude = entity.getLongitude();
            //原来纬度
            String latitude = entity.getLatitude();
            //判断两个经纬度距离是否大于3公里
            double distance = XYmatch.getDistance(longitudeUp, latitudeUp, Double.valueOf(longitude), Double.valueOf(latitude));
            if(distance >= 3){
                //相距大于3公里已基站为准
                deviceEntity.setLatitude(latitudeInt+"."+i+latitudeL);
                deviceEntity.setLongitude(longitudeInt+"."+h+longitudeL);
            }
        }else{
            //经纬度为空，代表浏览器添加。浏览器添加没有基本定位，所以取基站定位
            deviceEntity.setLatitude(latitudeInt+"."+i+latitudeL);
            deviceEntity.setLongitude(longitudeInt+"."+h+longitudeL);
        }

        //负载状态 loadState  蓄电池状态 batteryState 光电池状态  photocellState
        Integer loadState = deviceEntity.getLoadState();
        Integer batteryState = deviceEntity.getBatteryState();
        Integer photocellState = deviceEntity.getPhotocellState();
        deviceEntity.setRunState(RunStatusEnum.NORAML.getCode());
        //运行状态为正常
        if((loadState == LoadStatusEnum.SHUT.getCode() || loadState == LoadStatusEnum.OPEN.getCode()) &&
                (batteryState == BatteryStatusEnum.UNDERVOLTAGE.getCode() || batteryState == BatteryStatusEnum.NORMAL.getCode() || batteryState == BatteryStatusEnum.LIMITING.getCode()) &&
                (photocellState == PhotovoltaicCellStatusEnum.LIGHTWEAK.getCode()  || photocellState == PhotovoltaicCellStatusEnum.LIGHTINTENSITY.getCode() || photocellState == PhotovoltaicCellStatusEnum.CHARGING.getCode())){
            deviceEntity.setRunState(RunStatusEnum.NORAML.getCode());
        }
        //运行状态为报警
        if((batteryState == BatteryStatusEnum.DISCHARGE.getCode() || batteryState == BatteryStatusEnum.ACTIVATION.getCode())
                && loadState == LoadStatusEnum.OVERLOADWARNING.getCode()){
            deviceEntity.setRunState(RunStatusEnum.WARNING.getCode());
        }
        //运行状态为故障
        if((batteryState == BatteryStatusEnum.OVERPRESSURE.getCode() || batteryState == BatteryStatusEnum.TEMPERATURE.getCode())
                && (loadState == LoadStatusEnum.OPENCIRCUITPROECTION.getCode() || loadState == LoadStatusEnum.THROUGHPROTECTION.getCode() || loadState == LoadStatusEnum.SHORTCIRCUITPROECTION.getCode() || loadState == LoadStatusEnum.OVERLOADPROTECTION.getCode())){
            deviceEntity.setRunState(RunStatusEnum.FAULT.getCode());
            //添加故障日志信息
            FaultlogEntity faultlogEntity = new FaultlogEntity();
            faultlogEntity.setFaultLogId(UUID.randomUUID().toString());
            faultlogEntity.setLogStatus(1);
            faultlogEntity.setProjectId(entity.getProjectId());
            faultlogEntity.setDeviceId(entity.getDeviceId());
            faultlogEntity.setGroupId(entity.getGroupId());
            faultlogEntity.setCreateTime(new Date());

            List<Integer> batteryList = new ArrayList<>();
            batteryList.add(BatteryStatusEnum.OVERPRESSURE.getCode());
            batteryList.add(BatteryStatusEnum.TEMPERATURE.getCode());
            if(batteryList.contains(batteryState)){
                if(batteryState == BatteryStatusEnum.OVERPRESSURE.getCode()){
                    faultlogEntity.setFaultLogName(BatteryStatusEnum.OVERPRESSURE.getName()+"异常");
                }
                if(batteryState == BatteryStatusEnum.TEMPERATURE.getCode()){
                    faultlogEntity.setFaultLogName(BatteryStatusEnum.TEMPERATURE.getName()+"异常");
                }
            }else{
                if(loadState == LoadStatusEnum.OPENCIRCUITPROECTION.getCode()){
                    faultlogEntity.setFaultLogName(LoadStatusEnum.OPENCIRCUITPROECTION.getName()+"异常");
                }
                if(loadState == LoadStatusEnum.THROUGHPROTECTION.getCode()){
                    faultlogEntity.setFaultLogName(LoadStatusEnum.THROUGHPROTECTION.getName()+"异常");
                }
                if(loadState == LoadStatusEnum.SHORTCIRCUITPROECTION.getCode()){
                    faultlogEntity.setFaultLogName(LoadStatusEnum.SHORTCIRCUITPROECTION.getName()+"异常");
                }
                if(loadState == LoadStatusEnum.OVERLOADPROTECTION.getCode()){
                    faultlogEntity.setFaultLogName(LoadStatusEnum.OVERLOADPROTECTION.getName()+"异常");
                }
            }
            faultlogService.addFaultlog(faultlogEntity);
        }
        return deviceMapper.updateDeviceByCode(deviceEntity);
    }

    @Override
    public int updateDeviceTimeOutByCode(String deviceCode) {
        return deviceMapper.updateDeviceTimeOutByCode(deviceCode);
    }

    @Override
    public List<DeviceEntity> queryDeviceNoPage(DeviceQuery deviceQuery) {
        return deviceMapper.queryDevice(deviceQuery);
    }


}
