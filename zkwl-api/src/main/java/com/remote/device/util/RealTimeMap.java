package com.remote.device.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author EDZ
 * @Date 2019/7/30 14:18
 * @Version 1.0
 **/
public class RealTimeMap {
    public static Map<Integer,String> realTimeMap = new LinkedHashMap<>();
    static {
        //设备信息参数
        realTimeMap.put(1,"light");//亮度
        realTimeMap.put(2,"onOff");//开关
        realTimeMap.put(3,"lightingDuration");//亮灯时长
        realTimeMap.put(4,"morningHours");//晨亮时长
        /**
         * 高级设置
         * */

        realTimeMap.put(11,"switchDelayTime");//开关灯延时时间
        realTimeMap.put(12,"inspectionTime");//巡检时间
        realTimeMap.put(13,"loadWorkMode"); //负载工作模式
        realTimeMap.put(14,"powerLoad");//负载功率
        realTimeMap.put(15,"timeTurnOn");//开灯时刻
        realTimeMap.put(16,"timeTurnOff");//关灯时刻
        //   map.put("",17);//当前时刻
        realTimeMap.put(18,"time1");//1时段时长
        realTimeMap.put(19,"time2");//2时段时长
        realTimeMap.put(20,"time3");//3时段时长
        realTimeMap.put(21,"time4");//4时段时长
        realTimeMap.put(22,"time5");//5时段时长
        realTimeMap.put(23,"timeDown");//晨亮时段时长
        realTimeMap.put(24,"powerPeople1");//1时段有人功率
        realTimeMap.put(25,"powerPeople2");//2时段有人功率
        realTimeMap.put(26,"powerPeople3");//3时段有人功率
        realTimeMap.put(27,"powerPeople4");//4时段有人功率
        realTimeMap.put(28,"powerPeople5");//5时段有人功率
        realTimeMap.put(29,"powerDawnPeople");//晨亮时段有人功率
        realTimeMap.put(30,"inductionSwitch");//人体感应功能开关
        realTimeMap.put(31,"inductionLightOnDelay");//人体感应后的亮灯延时
        realTimeMap.put(32,"powerSensor1");//1时段无人功率
        realTimeMap.put(33,"powerSensor2");//2时段无人功率
        realTimeMap.put(34,"powerSensor3");//3时段无人功率
        realTimeMap.put(35,"powerSensor4");//4时段无人功率
        realTimeMap.put(36,"powerSensor5");//5时段无人功率
        realTimeMap.put(37,"powerSensorDown");//晨亮时段无人功率
        realTimeMap.put(38,"savingSwitch");//节能功能开关
        realTimeMap.put(39,"firDownPower");//一阶降功率电压
        realTimeMap.put(40,"twoDownPower");//二阶降功率电压
        realTimeMap.put(41,"threeDownPower");//三阶降功率电压
        realTimeMap.put(42,"firReducAmplitude");//一阶降功率幅度
        realTimeMap.put(43,"twoReducAmplitude");//二阶降功率幅度
        realTimeMap.put(44,"threeReducAmplitude");//三阶降功率幅度
        realTimeMap.put(45,"autoSleepTime");//自动休眠延时

        /**
         * 光电池
         * */
        realTimeMap.put(51,"vpv");//光控电压(即光电池电压)
        realTimeMap.put(52,"ligntOnDuration");//光控延时时间
        realTimeMap.put(53,"pvSwitch");//光控关灯开关
        /**
         * 蓄电池
         * */
        realTimeMap.put(59,"batType");//电池类型
        realTimeMap.put(60,"batStringNum");//电池串数
        realTimeMap.put(61,"volOverDisCharge");//过放电压
        realTimeMap.put(62,"volCharge");//充电电压
        realTimeMap.put(63,"iCharge");//充电电流
        realTimeMap.put(64,"tempCharge");//充电温度范围
        realTimeMap.put(65,"tempDisCharge");//放电温度范围
    }
}
