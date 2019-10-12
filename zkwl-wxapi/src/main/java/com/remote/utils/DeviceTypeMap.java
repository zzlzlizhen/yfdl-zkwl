package com.remote.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author zhangwenping
 * @Date 2019/7/2 14:19
 * @Version 1.0
 **/
@Component
public class DeviceTypeMap {
    public static final ConcurrentHashMap<String, Integer> DEVICE_TYPE = new ConcurrentHashMap<>();
    static {
        DEVICE_TYPE.put("LADG",1); //代表路灯设备
    }
}
