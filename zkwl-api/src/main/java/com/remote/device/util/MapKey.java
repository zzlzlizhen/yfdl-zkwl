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
        mapKey.put(1,"batteryState");//蓄电池状态
        mapKey.put(19,"loadState");//负载状态
        mapKey.put(11,"photocellState");//光电池状态
        mapKey.put(54,"onOff");
    }
}
