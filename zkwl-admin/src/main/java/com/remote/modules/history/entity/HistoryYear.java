package com.remote.modules.history.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author zhangwenping
 * @Date 2019/6/14 14:14
 * @Version 1.0
 **/
@Data
@TableName("history_year")
public class HistoryYear {
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
    private Date createTime;
    private String time;
}
