package com.remote.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author EDZ
 * @Date 2019/6/20 9:26
 * @Version 1.0
 **/
public class mapUtils {
    public static Map<String,Integer> map = new HashMap<>();
    static {
        map.put("light",1);//亮度
        map.put("onOff",2);//开关
        map.put("lightingDuration",3);//亮灯时长
        map.put("morningHours",4);//晨亮时长
        map.put("batteryState",77);//蓄电池状态
        map.put("batteryMargin",78);//蓄电池余量
        map.put("batteryVoltage",79);//蓄电池电压
        map.put("photocellState",83);//光电池状态
        map.put("photovoltaicCellVoltage",84);//光电池电压
        map.put("chargingCurrent",85);//光电池充电电流
        map.put("loadState",87);//负载状态
        map.put("loadVoltage",88);//负载电压
        map.put("loadPower",89);//负载功率
        map.put("loadCurrent",90);//负载电流
    }


}
