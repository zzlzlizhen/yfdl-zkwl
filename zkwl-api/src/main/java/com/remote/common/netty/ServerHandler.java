package com.remote.common.netty;

import com.alibaba.fastjson.JSONObject;
import com.remote.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.advancedsetting.service.AdvancedSettingService;
import com.remote.common.CommonEntity;
import com.remote.common.SetUp;
import com.remote.common.enums.RunStatusEnum;
import com.remote.common.redis.CacheUtils;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.service.DeviceService;
import com.remote.device.util.*;
import com.remote.history.entity.HistoryMouth;
import com.remote.history.service.HistoryService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.remote.device.util.MapKey.mapKey;

/**
 * @Author zhangwenping
 * @Date 2019/7/1 15:36
 * @Version 1.0
 **/
@Component
public class ServerHandler extends ChannelInboundHandlerAdapter {
    UpdateVersion updateVersion  = null;
    private Integer count = 0;
    private Logger log = LoggerFactory.getLogger(ServerHandler.class);
    private List<List<Byte>> lists = new ArrayList<>();
    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    private static final ConcurrentHashMap<String, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();

    @RabbitListener(queues = "topic.upload")//upgrade
    @RabbitHandler
    public void upgrade(String str, com.rabbitmq.client.Channel channel, Message message) throws IOException {
        try{
            if(!"".equals(str)){
                log.info("MQ操作设备消费消息："+str);
                JSONObject jsonObject = JSONObject.parseObject(str);
                DeviceEntity deviceEntity = JSONObject.toJavaObject(jsonObject, DeviceEntity.class);
                //操作设备和设备升级
                onWhile(deviceEntity);
            }
        }
        catch(Exception e){
            log.info("MQ操作设备报文有误");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }

    }

    @RabbitListener(queues = "topic.upgrade")//upload
    @RabbitHandler
    public void upload(String str, com.rabbitmq.client.Channel channel, Message message) throws IOException {
        try{
            if(!"".equals(str)){
                log.info("MQ设备升级消费消息："+str);
                JSONObject jsonObject = JSONObject.parseObject(str);
                DeviceEntity deviceEntity = JSONObject.toJavaObject(jsonObject, DeviceEntity.class);
                //操作设备和设备升级
                onWhile(deviceEntity);
            }
        }
        catch(Exception e){
            log.info("MQ设备升级报文有误");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }



    //操作设备和设备升级
    private void onWhile(DeviceEntity deviceEntity){
        List<String> deviceCodes = deviceEntity.getDeviceCodes();
        if(deviceCodes != null && deviceCodes.size() > 0){
            for(String deviceSN : deviceCodes){
                List<Integer> list = new ArrayList<>();
                List<Integer> value = new ArrayList<>();
                //缓存中取出数据
                CacheUtils cacheUtils = (CacheUtils)SpringUtils.getBean("cacheUtils");
                String channelId = cacheUtils.get(deviceSN).toString();
                if(CHANNEL_MAP.get(channelId) != null){
                    ChannelHandlerContext ctx = CHANNEL_MAP.get(channelId);
                    String encrypt = Utils.encrypt(deviceSN);
                    DeviceInfo result = null;
                    if(deviceEntity.getStatus().equals(new Integer(2))){

                        //需要客户端的设备信息
                        result = new DeviceInfo(4,0,encrypt,deviceEntity.getDeviceType(),deviceSN);
                        list.addAll(deviceEntity.getKey());
                        value.addAll(deviceEntity.getValue());

                        result.setKey(list);
                        result.setValue(value);
                        result.setDataLen(list.size());
                        DeviceService deviceTemp = (DeviceService)SpringUtils.getBean("deviceServiceImpl");
                        int i = deviceTemp.updateDeviceVersionByCode(deviceSN);
                        DeviceEntity temp = deviceTemp.queryDeviceByCode(deviceSN);
                        list.add(6);
                        value.add(temp.getDeviceVersion());
                        if(i > 0){
                            log.info("操作设备"+deviceSN+"："+JSONObject.toJSONString(result));
                        }
                    }else if(deviceEntity.getStatus().equals(new Integer(1))){
                        result = new DeviceInfo(2,deviceEntity.getVersion(),encrypt,deviceEntity.getDeviceType(),deviceSN);
                        result.setKey(list);
                        result.setValue(value);
                        result.setDataLen(list.size());
                        log.info("设备升级"+deviceSN+"："+JSONObject.toJSONString(result));
                    }
                    byte[] bytes = HexConvert.hexStringToBytes(result);
                    ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
                    Channel channel = ctx.channel();
                    channel.writeAndFlush(byteBuf);


                }else{
                    log.info("设备未连接");
                }

            }
        }

    }
    /**
     * @param ctx
     * @DESCRIPTION: 有客户端连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();

        String clientIp = insocket.getAddress().getHostAddress();
        int clientPort = insocket.getPort();

        //获取连接通道唯一标识
        ChannelId channelId = ctx.channel().id();

        System.out.println();
        //如果map中不包含此连接，就保存连接

        if (CHANNEL_MAP.containsKey(channelId.asShortText())) {
            log.info("客户端【" + channelId + "】是连接状态，连接通道数量: " + CHANNEL_MAP.size());
        } else {
            //保存连接
            CHANNEL_MAP.put(channelId.asShortText(), ctx);

            log.info("客户端【" + channelId + "】连接netty服务器[IP:" + clientIp + "--->PORT:" + clientPort + "]");
            log.info("连接通道数量: " + CHANNEL_MAP.size());
        }
    }

    /**
     * @param ctx
     * @DESCRIPTION: 有客户端终止连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();

        String clientIp = insocket.getAddress().getHostAddress();

        ChannelId channelId = ctx.channel().id();
        //包含此客户端才去删除
        if (CHANNEL_MAP.get(channelId.asShortText()) != null) {
            //删除连接
            CHANNEL_MAP.remove(channelId.asShortText());

            System.out.println();
            log.info("客户端【" + channelId + "】退出netty服务器[IP:" + clientIp + "--->PORT:" + insocket.getPort() + "]");
            log.info("连接通道数量: " + CHANNEL_MAP.size());
        }
    }

    /**
     * @param ctx
     * @DESCRIPTION: 有客户端发消息会触发此函数
     * @return: void
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        int cmdId = 0;
        int length = 0;
        ByteBuf buf = (ByteBuf)msg;
        byte[] bytes = new byte[buf.readableBytes()];

        StringBuffer sb = new StringBuffer();
        buf.readBytes(bytes);

        for (int i = 0 ; i < bytes.length;i++){
            sb.append(bytes[i]).append(",");
        }
        log.info("未解析的数据:"+sb.toString());
        buf.resetReaderIndex();
        //判断数据长度
        int newBytes[] = new int[bytes.length];
        for (int l=0;l< bytes.length;l++){
            newBytes[l] = bytes[l] & 0xff;
        }
        if(newBytes.length <= 3){
            log.info("发送的数据长度小于3");
            ctx.close();
            CHANNEL_MAP.remove(ctx.channel().id().asShortText());
            return;
        }
        cmdId =newBytes[2];
        cmdId +=newBytes[3]<<8;
        //cmdId 5 或者 6 代表是正确数据

        if(cmdId == 5){
            length = 204;
        }else if(cmdId == 6 || cmdId == 8 || cmdId == 1 || cmdId == 10){
            length = 56;
        }

        if(bytes.length == length){
            //转换为对象
            DeviceInfo deviceInfo = HexConvert.BinaryToDeviceInfo(bytes);
            if(deviceInfo == null){
                log.info("deviceInfo解析为空");
                ctx.close();
                CHANNEL_MAP.remove(ctx.channel().id().asShortText());
                return;
            }
            if(cmdId == 1){
                log.info(deviceInfo.getDevSN()+"操作设备成功!");
                return;
            }
            if(cmdId == 10){
                log.info(deviceInfo.getDevSN()+"心跳");
                return;
            }
            if(StringUtils.isEmpty(deviceInfo.getDevSN()) || StringUtils.isEmpty(deviceInfo.getDevKey()) || StringUtils.isEmpty(deviceInfo.getDevType())){
                log.info("SN或DevKey或DevType为空");
                ctx.close();
                CHANNEL_MAP.remove(ctx.channel().id().asShortText());
                return;
            }
            //把连接放到redis中
            CacheUtils cacheUtils = (CacheUtils)SpringUtils.getBean("cacheUtils");
            cacheUtils.set(deviceInfo.getDevSN(),ctx.channel().id().asShortText());
            cacheUtils.set(ctx.channel().id().asShortText(),deviceInfo.getDevSN());
            //解密
            if(StringUtils.isEmpty(deviceInfo.getDevSN()) && StringUtils.isEmpty(deviceInfo.getDevKey())){
                log.info("密钥和SN号为空");
                ctx.close();
                CHANNEL_MAP.remove(ctx.channel().id().asShortText());
                return;
            }
            String encrypt = Utils.encrypt(deviceInfo.getDevSN());

            if(!encrypt.equals(deviceInfo.getDevKey())){
                log.info("解密失败");
                ctx.close();
                CHANNEL_MAP.remove(ctx.channel().id().asShortText());
                return;
            }
            /**
             *  下面可以解析数据，保存数据，生成返回报文，将需要返回报文写入write函数
             *
             */
            if(deviceInfo.getCmdID().equals(new Integer(5))){
                //5终端发送需要上报的类型值 拿到客户端向服务端返回的设备信息做处理
                dataAnalysis(deviceInfo,ctx.channel().id());
            }else if(deviceInfo.getCmdID().equals(new Integer(6))){
                //更新版本
                updateVersion(deviceInfo, ctx.channel().id());
            }else if(deviceInfo.getCmdID().equals(new Integer(8))){
                //更新时间
                updateTime(deviceInfo, ctx);
            }
        }else{
            log.info("数据长度有误"+sb.toString());
        }

    }
    //当前时刻
    private  void updateTime(DeviceInfo deviceInfo,ChannelHandlerContext ctx) {
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
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        Channel channel = ctx.channel();
        channel.writeAndFlush(byteBuf);
        DeviceService deviceService = (DeviceService)SpringUtils.getBean("deviceServiceImpl");
        deviceService.updateDeviceTimeOutByCode(deviceInfo.getDevSN(),RunStatusEnum.NORAML.getCode());
        log.info(deviceInfo.getDevSN()+"当前时刻信息获取完毕");
    }

    private void updateVersion(DeviceInfo deviceInfo,ChannelId channelId)throws Exception {
        ChannelHandlerContext ctx = CHANNEL_MAP.get(channelId.asShortText());

        Integer nextCmdID = deviceInfo.getNextCmdID();
        //判断是否为重复数据
        if(nextCmdID != count){
            count = nextCmdID;
            if(nextCmdID.equals(new Integer(1))){
                //获取更新文件信息
                updateVersion = Utils.version();
                List<Byte> list = Arrays.asList(updateVersion.getBytes());
                //切割成多少分，每份1024
                lists = Utils.averageAssign(list, 1024);
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
                log.info(deviceInfo.getDevSN()+"升级设备数据信息"+result.getNextCmdID());
                //转换成字节
                byte[] bytes = HexConvert.updateVersionToBytes(result);
                ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
                Channel channel = ctx.channel();
                channel.writeAndFlush(byteBuf);
            }else{
                //升级完毕
                log.info(deviceInfo.getDevSN()+"升级设备完毕");
                count = 0;
            }

        }
    }


    //数据格式解析
    public void dataAnalysis(DeviceInfo deviceInfo,ChannelId channelId) throws Exception {
        ChannelHandlerContext ctx = CHANNEL_MAP.get(channelId.asShortText());
        List<Integer> upKey = deviceInfo.getKey();
        List<Integer> upValue = deviceInfo.getValue();
        List<String> dataKey = new ArrayList<>();
        //将所有参数的key取出解析存放到dataKey下
        for(Integer integer : upKey){
            dataKey.add(mapKey.get(integer));
        }
        //根据devSN查询设备信息
        DeviceService deviceService = (DeviceService)SpringUtils.getBean("deviceServiceImpl");
        DeviceEntity deviceEntity = deviceService.queryDeviceByCode(deviceInfo.getDevSN());
        if(deviceEntity == null){
            log.info(deviceInfo.getDevSN()+"该设备未添加入系统");
            return;
        }
        //提供一个公共的类，类中存放设备全部信息  否则反射时会找不到属性。start
        CommonEntity common = new CommonEntity();
        BeanUtils.copyProperties(deviceEntity, common);
        //提供一个公共的类，类中存放设备全部信息  否则反射时会找不到属性。end
        Integer index = 0;
        if(dataKey != null && dataKey.size() > 0){
            for (String str : dataKey){
                Field field = common.getClass().getDeclaredField(str);
                Integer value = upValue.get(index);
                field.setAccessible(true);
                if (field.getGenericType().toString().equals("class java.lang.String")) {
                    field.set(common,value.toString());
                }else if (field.getGenericType().toString().equals("class java.lang.Integer")) {
                    field.set(common,value);
                }else if (field.getGenericType().toString().equals("class java.lang.Double")) {
                    field.set(common, Double.valueOf(value.toString()));
                }
                index ++;
            }
        }
        //代表有些设备没有操作成功
        if(common.getDeviceVersion() != deviceEntity.getDeviceVersion()){
            Thread.sleep(10000);
            SetUp setUp = new SetUp();
            log.info(deviceEntity.getDeviceCode() + "设备没有操作成功，重新下发数据");
            AdvancedSettingService advancedSettingService = (AdvancedSettingService)SpringUtils.getBean("advancedSettingServiceImpl");

            List<Integer> key = new ArrayList<>(48);
            List<Integer> value = new ArrayList<>(48);
            //添加所有key
            for(Map.Entry<Integer, String> entry : RealTimeMap.realTimeMap.entrySet()){
                key.add(entry.getKey());
            }
            AdvancedSettingEntity advancedSettingEntity = advancedSettingService.queryByDeviceCode(deviceEntity.getDeviceCode());
            BeanUtils.copyProperties(advancedSettingEntity, deviceEntity);
            deviceEntity.setUpdateUser(advancedSettingEntity.getUid().toString());
            BeanUtils.copyProperties(deviceEntity, setUp);
            if(key != null && key.size() > 0){
                for (Integer inde : key){
                    Field field = setUp.getClass().getDeclaredField(RealTimeMap.realTimeMap.get(inde));
                    field.setAccessible(true);
                    if (field.getGenericType().toString().equals("class java.lang.String")) {
                        String str = (String)field.get(setUp);
                        if(str == null){
                            value.add(0);
                        }else{
                            value.add(Integer.valueOf(str));
                        }

                    }else if (field.getGenericType().toString().equals("class java.lang.Integer")) {
                        Integer inte = (Integer)field.get(setUp);
                        if(inte == null){
                            value.add(0);
                        }else{
                            value.add(inte);
                        }
                    }
                }
            }

            String encrypt = Utils.encrypt(deviceEntity.getDeviceCode());
            DeviceInfo result = new DeviceInfo(4,0,encrypt,deviceEntity.getDeviceType(),deviceEntity.getDeviceCode());
            key.add(6);
            value.add(deviceEntity.getDeviceVersion());
            result.setKey(key);
            result.setValue(value);
            result.setDataLen(key.size());
            log.info(deviceEntity.getDeviceCode() + "设备重新操作："+JSONObject.toJSONString(result));
            //转换成字节
            byte[] bytes = HexConvert.hexStringToBytes(result);
            ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
            Channel channel = ctx.channel();
            channel.writeAndFlush(byteBuf);
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
        historyMouth.setDischargeCurrent(deviceEntity.getLoadCurrent());

        HistoryService historyService = (HistoryService)SpringUtils.getBean("historyServiceImpl");
        historyService.insertHistoryData(historyMouth);
        log.info(deviceInfo.getDevSN()+"设备上传的信息完毕："+JSONObject.toJSONString(deviceEntity));
    }

    //参数转换
    private void convert(CommonEntity common) {
        //放电量
        if(common.getDischargeCapacity() != null){
            BigDecimal bd1 = new BigDecimal(Double.toString(common.getDischargeCapacity()));
            double v = bd1.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
            common.setDischargeCapacity(v);
        }
        //充电量
        if(common.getChargingCapacity() != null){
            BigDecimal bd1 = new BigDecimal(Double.toString(common.getChargingCapacity()));
            double v = bd1.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
            common.setChargingCapacity(v);
        }
        //放电电流
        if(common.getDischargeCurrent() != null){
            common.setDischargeCurrent(Utils.div(common.getDischargeCurrent(),2));
        }
        //充电电流
        if(common.getChargingCurrent() != null){
            common.setChargingCurrent(Utils.div(common.getChargingCurrent(),2));
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
        //充电功率chargingPower
        common.setChargingPower(Utils.mul(common.getBatteryVoltage(),common.getChargingCurrent()));
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


    /**
     * @param msg        需要发送的消息内容
     * @param channelId 连接通道唯一id
     * @DESCRIPTION: 服务端给客户端发送消息
     * @return: void
     */
    public void channelWrite(ChannelId channelId, Object msg) throws Exception {

        ChannelHandlerContext ctx = CHANNEL_MAP.get(channelId);

        if (ctx == null) {
            log.info("通道【" + channelId + "】不存在");
            return;
        }

        if (msg == null && msg == "") {
            log.info("服务端响应空的消息");
            return;
        }

        //将客户端的信息直接返回写入ctx
        ctx.write(msg);
        //刷新缓存区
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //缓存中取出数据
        CacheUtils cacheUtils = (CacheUtils)SpringUtils.getBean("cacheUtils");
        String socketString = ctx.channel().remoteAddress().toString();
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String deviceCode = "";
            if (event.state() == IdleState.READER_IDLE) {
                try{
                    deviceCode = cacheUtils.get(ctx.channel().id().asShortText()).toString();
                    DeviceService deviceService = (DeviceService)SpringUtils.getBean("deviceServiceImpl");
                    deviceService.updateDeviceTimeOutByCode(deviceCode,RunStatusEnum.OFFLINE.getCode());
                }catch (Exception e){

                }
                log.info("Client: " +deviceCode +"："+ socketString + " READER_IDLE 读超时");
                ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("Client: " +deviceCode +"：" + socketString + " WRITER_IDLE 写超时");
                ctx.close();
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("Client: " +deviceCode +"："+ socketString + " ALL_IDLE 总超时");
                ctx.close();
            }
        }
    }

    /**
     * @param ctx
     * @DESCRIPTION: 发生异常会触发此函数
     * @return: void
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //缓存中取出数据
        CacheUtils cacheUtils = (CacheUtils)SpringUtils.getBean("cacheUtils");
        String deviceCode = cacheUtils.get(ctx.channel().id().asShortText()).toString();
        DeviceService deviceService = (DeviceService)SpringUtils.getBean("deviceServiceImpl");
        deviceService.updateDeviceTimeOutByCode(deviceCode,RunStatusEnum.OFFLINE.getCode());
        ctx.close();
        CHANNEL_MAP.remove(ctx.channel().id().asShortText());
        log.info(ctx.channel().id() + " 发生了错误,此连接被关闭" + "此时连通数量: " + CHANNEL_MAP.size());
        log.error("发生了错误:"+cause.getMessage(),cause);
        //cause.printStackTrace();
    }

}

