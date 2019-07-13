package com.remote.common.utils;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author zhangwenping
 * @Date 2019/7/2 14:19
 * @Version 1.0
 **/
@Component
public class DeviceTypeMap {
    public static final ConcurrentHashMap<String,String> DEVICE_TYPE = new ConcurrentHashMap<>();
    static {
        DEVICE_TYPE.put("1","GPRS-40W-路灯");
    }
}
