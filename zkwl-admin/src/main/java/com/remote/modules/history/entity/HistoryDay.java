package com.remote.modules.history.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author zhangwenping
 * @Date 2019/6/14 14:14
 * @Version 1.0
 **/
@TableName("history_day")
@Data
public class HistoryDay {
    private String dayId;
    private String deviceCode;
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
    private Integer fillingsNumber; //充满次数
    private Integer dischargeNumber;//冲放次数
    private String time;
    private Date createTime;
}
