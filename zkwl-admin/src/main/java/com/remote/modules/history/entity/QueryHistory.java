package com.remote.modules.history.entity;

import lombok.Data;
import org.aspectj.lang.annotation.DeclareAnnotation;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author EDZ
 * @Date 2019/6/14 14:38
 * @Version 1.0
 **/
@Data
public class QueryHistory {

    private List<Double> dischargeCapacityList = new ArrayList<>();//放电量
    private List<Double> chargingCapacityList = new ArrayList<>();//充电量
    private List<Double> chargingCurrentList = new ArrayList<>();//充电电流
    private List<Double> dischargeCurrentList = new ArrayList<>();//放电电流
    private List<Double> batteryVoltageList = new ArrayList<>();//电池电压
    private List<Double> ambientTemperatureList = new ArrayList<>();//环境温度
    private List<Double> internalTemperatureList = new ArrayList<>();//内部温度
    private List<Double> visitorsFlowrateList = new ArrayList<>();//人流量
    private List<Double> inductionFrequencyList = new ArrayList<>();//感应次数
    private List<Double> meteorologicalList = new ArrayList<>();//气象信息
}
