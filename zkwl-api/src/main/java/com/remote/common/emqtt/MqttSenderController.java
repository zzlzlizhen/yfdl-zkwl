package com.remote.common.emqtt;

import com.alibaba.fastjson.JSONObject;
import com.remote.common.emqtt.service.MqttGateway;
import com.remote.common.netty.NettyServer;
import com.remote.common.utils.DataUtils;
import com.remote.common.utils.MapUtils;
import com.remote.device.entity.DeviceEntityApi;
import com.remote.device.entity.DeviceQuery;
import com.remote.device.service.DeviceService;
import com.remote.device.util.DeviceInfo;
import com.remote.device.util.HexConvert;
import com.remote.device.util.Utils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author EDZ
 * @Date 2019/7/15 9:13
 * @Version 1.0
 **/
@RestController
@RequestMapping("/mqtt")
public class MqttSenderController {

    private static Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Autowired
    private MqttGateway mqttGateway;

    @Autowired
    private DeviceService deviceService;

    @RequestMapping(value = "/sendMq", method= RequestMethod.POST)
    public void sendMq(@RequestBody DataUtils data){
        List<String> deviceCodes = data.getDeviceCodes();
        DeviceQuery deviceQuery = new DeviceQuery();
        if(data.getGroupId() != null && data.getGroupId() != ""){
            deviceQuery.setGroupId(data.getGroupId());
            List<DeviceEntityApi> deviceEntities = deviceService.queryDeviceNoPage(deviceQuery);
            deviceCodes = deviceEntities.parallelStream().map(deviceEntity -> deviceEntity.getDeviceCode()).collect(Collectors.toCollection(ArrayList::new));
            data.setDeviceCodes(deviceCodes);
        }
        if(data.getProjectId() != null && data.getProjectId() != ""){
            deviceQuery.setGroupId(data.getGroupId());
            List<DeviceEntityApi> deviceEntities = deviceService.queryDeviceNoPage(deviceQuery);
            deviceCodes = deviceEntities.parallelStream().map(deviceEntity -> deviceEntity.getDeviceCode()).collect(Collectors.toCollection(ArrayList::new));
            data.setDeviceCodes(deviceCodes);
        }
        List<Integer> key = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(data.getQaKey())){
            for(String str : data.getQaKey()){
                key.add(MapUtils.map.get(str));
            }
        }
        data.setKey(key);
        String s = JSONObject.toJSONString(data);
        logger.info("操作设备:"+s);
        if(deviceCodes != null && deviceCodes.size() > 0){
            for(String deviceSN : deviceCodes){
                DeviceEntityApi deviceEntity = deviceService.queryDeviceByCode(deviceSN);
                //缓存中取出数据
                String encrypt = Utils.encrypt(deviceSN);
                DeviceInfo result = null;
                if(data.getStatus().equals(new Integer(2))){
                    //需要客户端的设备信息
                    result = new DeviceInfo(4,0,encrypt,deviceEntity.getDeviceType(),deviceSN);
                    result.setKey(data.getKey());
                    result.setValue(data.getValue());
                    result.setDataLen(data.getKey().size());
                    logger.info("操作设备"+deviceSN+"："+JSONObject.toJSONString(result));
                }else if(data.getStatus().equals(new Integer(1))){
                    result = new DeviceInfo(2,deviceEntity.getVersion(),encrypt,deviceEntity.getDeviceType(),deviceSN);
                    result.setKey(data.getKey());
                    result.setValue(data.getValue());
                    result.setDataLen(data.getKey().size());
                    logger.info("设备升级"+deviceSN+"："+JSONObject.toJSONString(result));
                }
                byte[] bytes = HexConvert.hexStringToBytes(result);
                mqttGateway.sendToMqtt(bytes,deviceSN);
            }
        }
    }
}
