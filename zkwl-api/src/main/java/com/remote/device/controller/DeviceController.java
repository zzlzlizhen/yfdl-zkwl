package com.remote.device.controller;

import com.alibaba.fastjson.JSONObject;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.service.DeviceService;
import com.remote.device.util.DeviceInfo;
import com.remote.device.util.MapKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.remote.device.util.MapKey.mapKey;


/**
 * @Author EDZ
 * @Date 2019/6/11 9:24
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fun/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @RequestMapping(value = "/updateBefore", method= RequestMethod.POST)
    public DeviceInfo updateDeviceBefore(@RequestBody DeviceInfo deviceInfo) {
        Integer cmdID = deviceInfo.getCmdID();
        if(cmdID.equals(new Integer(1))){
            //1终端请求事件
            DeviceInfo result = new DeviceInfo(2,3,deviceInfo.getDevKey(),deviceInfo.getDevType(),deviceInfo.getDevSN());
            List<Integer> list = new ArrayList<>();
            //Set<Map.Entry<Integer, String>> entries = mapKey.entrySet();
        }else if(cmdID.equals(new Integer(3))){
            //3终端发送需要上报的类型值
        }



        return deviceInfo;
    }




}
