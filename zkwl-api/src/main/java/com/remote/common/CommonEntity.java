package com.remote.common;

import com.remote.device.entity.DeviceEntity;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author zhangwenping
 * @Date 2019/6/19 13:55
 * @Version 1.0
 **/
@Component
@Data
public class CommonEntity  {
    private String mouthId;
    private String dayId;
    private String yearId;
    private String deviceCode;//设备code
    private Double dischargeCapacity;//放电量
    private Double chargingCapacity;//充电量
    private Double chargingCurrent;//充电电流
    private Double dischargeCurrent;//放电电流
    private Double batteryVoltage;//电池电压
    private Double ambientTemperature;//环境温度
    private Double internalTemperature;//内部温度
    private Double visitorsFlowrate;//人流量
    private Double inductionFrequency;//感应次数
    private Double meteorological;//气象信息
    private Date createTime;
}
