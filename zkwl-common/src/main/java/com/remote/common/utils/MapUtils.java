package com.remote.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhangwenping
 * @Date 2019/7/3 16:19
 * @Version 1.0
 **/
public class MapUtils {
    public static Map<String,Integer> map = new HashMap<>();
    static {
        //设备信息参数
        map.put("light",1);//亮度
        map.put("onOff",2);//开关
        map.put("lightingDuration",3);//亮灯时长
        map.put("morningHours",4);//晨亮时长


        /**
         * 高级设置
         * */

        map.put("switchDelayTime",11);//开关灯延时时间
        map.put("inspectionTime",12);//巡检时间
        map.put("loadWorkMode",13); //负载工作模式
        map.put("powerLoad",14);//负载功率
        map.put("timeTurnOn",15);//开灯时刻
        map.put("timeTurnOff",16);//关灯时刻
        //   map.put("",17);//当前时刻
        map.put("time1",18);//1时段时长
        map.put("time2",19);//2时段时长
        map.put("time3",20);//3时段时长
        map.put("time4",21);//4时段时长
        map.put("time5",22);//5时段时长
        map.put("timeDown",23);//晨亮时段时长
        map.put("powerPeople1",24);//1时段有人功率
        map.put("powerPeople2",25);//2时段有人功率
        map.put("powerPeople3",26);//3时段有人功率
        map.put("powerPeople4",27);//4时段有人功率
        map.put("powerPeople5",28);//5时段有人功率
        map.put("powerDawnPeople",29);//晨亮时段有人功率
        map.put("inductionSwitch",30);//人体感应功能开关
        map.put("InductionLightOnDelay",31);//人体感应后的亮灯延时
        map.put("powerSensor1",32);//1时段无人功率
        map.put("powerSensor2",33);//2时段无人功率
        map.put("powerSensor3",34);//3时段无人功率
        map.put("powerSensor4",35);//4时段无人功率
        map.put("powerSensor5",36);//5时段无人功率
        map.put("powerSensorDown",37);//晨亮时段无人功率
        map.put("savingSwitch",38);//节能功能开关
        map.put("firDownPower",39);//一阶降功率电压
        map.put("twoDownPower",40);//二阶降功率电压
        map.put("threeDownPower",41);//三阶降功率电压
        map.put("firReducAmplitude",42);//一阶降功率幅度
        map.put("twoReducAmplitude",43);//二阶降功率幅度
        map.put("threeReducAmplitude",44);//三阶降功率幅度
        map.put("autoSleepTime",45);//自动休眠延时

        /**
         * 光电池
         * */
        map.put("vpv",51);//光控电压(即光电池电压)
        map.put("ligntOnDuration",52);//光控延时时间
        map.put("pvSwitch",53);//光控关灯开关
        /**
         * 蓄电池
         * */
        map.put("batType",59);//电池类型
        map.put("batStringNum",60);//电池串数
        map.put("volOverDisCharge",61);//过放电压
        map.put("volCharge",62);//充电电压
        map.put("iCharge",63);//充电电流
        map.put("tempCharge",64);//充电温度范围
        map.put("tempDisCharge",65);//放电温度范围
    }
}
