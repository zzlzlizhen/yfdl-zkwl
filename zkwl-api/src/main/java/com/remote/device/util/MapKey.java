package com.remote.device.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author EDZ
 * @Date 2019/6/11 19:09
 * @Version 1.0
 **/
public class MapKey {
    public static Map<Integer,String> mapKey = new HashMap<Integer,String>();

    static {
        //设备信息参数
        mapKey.put(1,"light");//亮度
        mapKey.put(2,"onOff");//开关
        mapKey.put(3,"lightingDuration");//亮灯时长
        mapKey.put(4,"morningHours");//晨亮时长
        mapKey.put(83,"batteryState");//蓄电池状态
        mapKey.put(84,"batteryMargin");//蓄电池余量
        mapKey.put(88,"photocellState");//c
        mapKey.put(89,"photovoltaicCellVoltage");//光电池电压
        mapKey.put(91,"loadState");//负载状态
        mapKey.put(92,"loadVoltage");//负载电压
        mapKey.put(93,"loadPower");//负载功率
        mapKey.put(94,"loadCurrent");//负载电流
        mapKey.put(97,"communicationType");//通讯模式
        mapKey.put(98,"longitude");//经度
        mapKey.put(99,"latitude");//纬度
        //历史数据参数
        mapKey.put(71,"chargingCapacity");//充电量
        mapKey.put(72,"dischargeCapacity");//放电量
        mapKey.put(74,"dischargeCurrent");//放电电流
        mapKey.put(76,"ambientTemperature");//环境温度
        mapKey.put(77,"internalTemperature");//内部温度
        mapKey.put(78,"visitorsFlowrate");//人流量
        mapKey.put(79,"inductionFrequency");//感应次数
        mapKey.put(82,"meteorological");//气象信息
        mapKey.put(81,"fillingsNumber");//充满次数
        mapKey.put(80,"dischargeNumber");//过放次数

        //公共数据参数
        mapKey.put(73,"chargingCurrent");//充电电流
        mapKey.put(75,"batteryVoltage");//电池电压

    }
}
