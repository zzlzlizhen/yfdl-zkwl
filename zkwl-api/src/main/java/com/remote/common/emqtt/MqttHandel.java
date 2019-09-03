package com.remote.common.emqtt;

import com.alibaba.fastjson.JSONObject;
import com.remote.common.CommonEntity;
import com.remote.common.emqtt.service.MqttGateway;
import com.remote.common.netty.NettyServer;
import com.remote.device.entity.DeviceEntityApi;
import com.remote.device.service.DeviceService;
import com.remote.device.util.*;
import com.remote.devicetype.entity.DeviceTypeEntity;
import com.remote.devicetype.service.DeviceTypeService;
import com.remote.history.entity.HistoryMouth;
import com.remote.history.service.HistoryService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.remote.device.util.MapKey.mapKey;

/**
 * @Author zhangwenping
 * @Date 2019/7/13 15:34
 * @Version 1.0
 **/
@Component
public class MqttHandel {
    private static Logger log = LoggerFactory.getLogger(NettyServer.class);
    UpdateVersion updateVersion  = null;
    private Integer count = 0;
    private List<List<Byte>> lists = new ArrayList<>();

    @Autowired
    private MqttGateway mqttGateway;

    public void handelRead(byte[] bytes) throws Exception{
        int cmdId = 0;
        int length = 0;
        StringBuffer sb = new StringBuffer();

        for (int i = 0 ; i < bytes.length;i++){
            sb.append(bytes[i]).append(",");
        }
        log.info("收到客户端报文......16进制：");
        //判断数据长度
        int newBytes[] = new int[bytes.length];
        for (int l=0;l< bytes.length;l++){
            newBytes[l] = bytes[l] & 0xff;
        }
        if(newBytes.length <= 3){
            log.info("发送的数据长度小于3");
            return;
        }
        cmdId =newBytes[2];
        cmdId +=newBytes[3]<<8;
        //cmdId 5 或者 6 代表是正确数据
        if(cmdId == 5){
            length = 200;
        }else if(cmdId == 6 || cmdId == 8){
            length = 56;
        }

        if(bytes.length == length){
            //转换为对象
            DeviceInfo deviceInfo = HexConvert.BinaryToDeviceInfo(bytes);
            if(deviceInfo == null){
                log.info("deviceInfo解析为空");
                return;
            }
            if(StringUtils.isEmpty(deviceInfo.getDevSN()) || StringUtils.isEmpty(deviceInfo.getDevKey()) || StringUtils.isEmpty(deviceInfo.getDevType())){
                log.info("SN或DevKey或DevType为空");
                return;
            }
            log.info("收到客户端报文...... :" + JSONObject.toJSONString(deviceInfo));
            //解密
            if(StringUtils.isEmpty(deviceInfo.getDevSN()) && StringUtils.isEmpty(deviceInfo.getDevKey())){
                log.info("密钥和SN号为空");
                return;
            }
            String encrypt = Utils.encrypt(deviceInfo.getDevSN());

            if(!encrypt.equals(deviceInfo.getDevKey())){
                log.info("解密失败");
                return;
            }
            /**
             *  下面可以解析数据，保存数据，生成返回报文，将需要返回报文写入write函数
             *
             */
            if(deviceInfo.getCmdID().equals(new Integer(5))){
                //5终端发送需要上报的类型值 拿到客户端向服务端返回的设备信息做处理
                dataAnalysis(deviceInfo);
            }else if(deviceInfo.getCmdID().equals(new Integer(6))){
                //更新版本
                updateVersion(deviceInfo);
            }else if(deviceInfo.getCmdID().equals(new Integer(8))){
                //更新时间
                updateTime(deviceInfo);
            }
        }else{
            log.info("数据长度有误"+sb.toString());
        }
    }



    private void updateVersion(DeviceInfo deviceInfo)throws Exception {
        Integer nextCmdID = deviceInfo.getNextCmdID();
        //判断是否为重复数据
        if(nextCmdID != count){
            count = nextCmdID;
            if(nextCmdID.equals(new Integer(1))){
                DeviceTypeService deviceTypeService = (DeviceTypeService)SpringUtils.getBean("deviceTypeServiceImpl");
                DeviceTypeEntity deviceType = deviceTypeService.getDeviceTypeByCode(deviceInfo.getDevType(),2);
                if(deviceType != null){
                    DeviceService deviceService = (DeviceService)SpringUtils.getBean("deviceServiceImpl");
                    DeviceEntityApi deviceEntityApi = deviceService.queryDeviceByCode(deviceInfo.getDevSN());
                    String path = deviceType.getDeviceTypePath()+deviceEntityApi.getFutureVersion()+".bin";
                    //获取更新文件信息
                    updateVersion = Utils.version(path);
                    List<Byte> list = Arrays.asList(updateVersion.getBytes());
                    //切割成多少分，每份1024
                    lists = Utils.averageAssign(list, 1024);
                }
            }
            if(nextCmdID <= lists.size()){
                //拿到客户端需要的第几份
                List<Byte> bytes1 = lists.get(deviceInfo.getNextCmdID() - 1);
                //nextId+1 确保客户端下次拿的数据正确
                Integer nextId = deviceInfo.getNextCmdID() + 1;
                //封装返回对象
                DeviceVersionInfo result = new DeviceVersionInfo(7,nextId,deviceInfo.getDevKey(),deviceInfo.getDevType(),deviceInfo.getDevSN(),2,Integer.valueOf((int)updateVersion.getLength()),updateVersion.getSum());
                int binLastSize = (int)updateVersion.getLength() - (1024 * (deviceInfo.getNextCmdID()));
                result.setBinLastSize(binLastSize);
                //转换成数组
                Byte [] newBytes = bytes1.toArray(new Byte[1024]);
                result.setBin(newBytes);
                log.info("升级设备数据信息"+JSONObject.toJSONString(result));
                //转换成字节
                byte[] bytes = HexConvert.updateVersionToBytes(result);
                //发送消息
                mqttGateway.sendToMqtt(bytes,deviceInfo.getDevSN());
            }else{
                //升级完毕
                log.info("升级设备完毕");
                count = 0;
            }

        }
    }

    //数据格式解析
    public void dataAnalysis(DeviceInfo deviceInfo) throws Exception {
        log.info("设备上传的信息："+JSONObject.toJSONString(deviceInfo));
        List<Integer> upKey = deviceInfo.getKey();
        List<Integer> upValue = deviceInfo.getValue();
        List<String> dataKey = new ArrayList<>();
        //将所有参数的key取出解析存放到dataKey下
        for(Integer integer : upKey){
            dataKey.add(mapKey.get(integer));
        }
        //根据devSN查询设备信息
        DeviceService deviceService = (DeviceService)SpringUtils.getBean("deviceServiceImpl");
        DeviceEntityApi deviceEntity = deviceService.queryDeviceByCode(deviceInfo.getDevSN());
        //提供一个公共的类，类中存放设备全部信息  否则反射时会找不到属性。start
        CommonEntity common = new CommonEntity();
        BeanUtils.copyProperties(deviceEntity, common);
        //提供一个公共的类，类中存放设备全部信息  否则反射时会找不到属性。end
        Integer index = 0;
        if(dataKey != null && dataKey.size() > 0){
            for (String str : dataKey){
                Field field = common.getClass().getDeclaredField(str);
                Integer value = upValue.get(index);
                if (field.getGenericType().toString().equals("class java.lang.String")) {
                    field.setAccessible(true);
                    field.set(common,value.toString());
                }else if (field.getGenericType().toString().equals("class java.lang.Integer")) {
                    field.setAccessible(true);
                    field.set(common,value);
                }else if (field.getGenericType().toString().equals("class java.lang.Double")) {
                    field.setAccessible(true);
                    field.set(common, Double.valueOf(value.toString()));
                }
                index ++;
            }
        }

        //转换参数信息
        convert(common);
        //所有设备属性都映射到公共的类中，需要哪些信息，自行转换 start
        BeanUtils.copyProperties(common, deviceEntity);
        //首先修改设备信息
        deviceService.updateDeviceByCode(common,deviceEntity);
        //所有设备属性都映射到公共的类中，需要哪些信息，自行转换 end
        //判断公共类属于历史数据，还是设备信息
        //判断是否时历史数据
        HistoryMouth historyMouth = new HistoryMouth();
        BeanUtils.copyProperties(common, historyMouth);
        //放电电流
        historyMouth.setDischargeCapacity(Double.valueOf(deviceEntity.getLoadPower()));

        HistoryService historyService = (HistoryService)SpringUtils.getBean("historyServiceImpl");
        historyService.insertHistoryData(historyMouth);

    }


    //参数转换
    private void convert(CommonEntity common) {
        //放电量
        if(common.getDischargeCapacity() != null){
            common.setDischargeCapacity(Utils.div(common.getDischargeCapacity(),1));
        }
        //充电量
        if(common.getChargingCapacity() != null){
            common.setChargingCapacity(Utils.div(common.getChargingCapacity(),1));
        }
        //放电电流
        if(common.getDischargeCurrent() != null){
            common.setDischargeCurrent(Utils.div(common.getDischargeCurrent(),1));
        }
        //充电电流
        if(common.getChargingCurrent() != null){
            common.setChargingCurrent(Utils.div(common.getChargingCurrent(),1));
        }
        //电池电压
        if(common.getBatteryVoltage() != null){
            common.setBatteryVoltage(Utils.div(common.getBatteryVoltage(),1));
        }
        //总充电量
        if(common.getChargingCapacitySum() != null){
            common.setChargingCapacitySum(Utils.div(common.getChargingCapacitySum(),1));
        }
        //总放电量
        if(common.getDischargeCapacitySum() != null){
            common.setDischargeCapacitySum(Utils.div(common.getDischargeCapacitySum(),1));
        }
        //光电池电压
        if(common.getPhotovoltaicCellVoltage() != null){
            common.setPhotovoltaicCellVoltage(Utils.div(common.getPhotovoltaicCellVoltage(),1));
        }
        //充电电流
        if(common.getChargingCurrent() != null){
            common.setChargingCurrent(Utils.div(common.getChargingCurrent(),1));
        }
        //充电功率chargingPower
        common.setChargingPower(Utils.mul(common.getPhotovoltaicCellVoltage(),common.getChargingCurrent()));
        //负载电压
        if(common.getLoadVoltage() != null){
            common.setLoadVoltage(Utils.div(common.getLoadVoltage(),1));
        }
        //负载功率
        if(common.getLoadPower() != null){
            common.setLoadPower(Utils.div(common.getLoadPower(),1));
        }
        //负载电流
        if(common.getLoadCurrent() != null){
            common.setLoadCurrent(Utils.div(common.getLoadCurrent(),1));
        }
    }


    //当前时刻
    private  void updateTime(DeviceInfo deviceInfo) {
        Calendar now = Calendar.getInstance();
        String encrypt = Utils.encrypt(deviceInfo.getDevSN());
        DeviceInfo result = new DeviceInfo(9,0,encrypt,deviceInfo.getDevType(),deviceInfo.getDevSN());
        List<Integer> value = new ArrayList<>();
        List<Integer> key = new ArrayList<>();
        result.setValue(value);
        key.add(1);
        key.add(2);
        key.add(3);
        result.setKey(key);
        value.add(now.get(Calendar.HOUR_OF_DAY));
        value.add(now.get(Calendar.MINUTE));
        value.add(now.get(Calendar.SECOND));
        result.setDataLen(value.size());
        log.info("当前时刻："+JSONObject.toJSONString(result));
        byte[] bytes = HexConvert.hexStringToBytes(result);
        //发送mq
        mqttGateway.sendToMqtt(bytes,deviceInfo.getDevSN());
    }
}
