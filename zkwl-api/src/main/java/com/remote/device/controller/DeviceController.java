package com.remote.device.controller;

import com.alibaba.fastjson.JSONObject;
import com.remote.common.redis.CacheUtils;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.service.DeviceService;
import com.remote.device.util.DeviceInfo;
import com.remote.device.util.MapKey;
import com.remote.device.util.Utils;
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

    public static void main(String[] args) {
        double div = Utils.div(4386, 1);
        System.out.println(div);


    }

}
