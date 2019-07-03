package com.remote.common.netty;

import com.alibaba.fastjson.JSONObject;
import com.remote.common.CommonEntity;
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
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.remote.device.util.MapKey.mapKey;

/**
 * @Author zhangwenping
 * @Date 2019/7/1 15:36
 * @Version 1.0
 **/
@Component
@RabbitListener(queues = "CalonDirectQueue")//CalonDirectQueue为队列名称
public class ServerHandler extends ChannelInboundHandlerAdapter {
    UpdateVersion updateVersion  = null;
    private Integer count = 0;
    private Logger log = LoggerFactory.getLogger(ServerHandler.class);
    private List<List<Byte>> lists = new ArrayList<>();
    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    private static final ConcurrentHashMap<String, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();

    @RabbitHandler
    public void process(String str) {
        push(str);
    }

    private void push(String str) {
        if(!"".equals(str)){
            JSONObject jsonObject = JSONObject.parseObject(str);
            DeviceEntity deviceEntity = JSONObject.toJavaObject(jsonObject, DeviceEntity.class);
            //代表操作设备
            onWhile(deviceEntity);
        }
    }
    //代表操作设备
    private void onWhile(DeviceEntity deviceEntity){
        List<Integer> list = new ArrayList<>();
        List<Integer> value = new ArrayList<>();
        List<String> deviceCodes = deviceEntity.getDeviceCodes();
        if(deviceCodes != null && deviceCodes.size() > 0){
            for(String deviceSN : deviceCodes){
                //缓存中取出数据
                CacheUtils cacheUtils = (CacheUtils)SpringUtils.getBean("cacheUtils");
                String channelId = cacheUtils.get(deviceSN).toString();
                if(CHANNEL_MAP.get(channelId) != null){
                    ChannelHandlerContext ctx = CHANNEL_MAP.get(channelId);
                    String encrypt = Utils.encrypt(deviceSN);
                    //需要客户端的设备信息
                    DeviceInfo result = new DeviceInfo(4,0,encrypt,deviceEntity.getDeviceType(),deviceSN);
                    list.addAll(deviceEntity.getKey());
                    value.addAll(deviceEntity.getValue());
                    result.setKey(list);
                    result.setValue(value);
                    result.setDataLen(list.size());
                    log.info("操作设备："+JSONObject.toJSONString(result));
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
     * @author xiongchuan on 2019/4/28 16:10
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

        if (CHANNEL_MAP.containsKey(channelId)) {
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
        if (CHANNEL_MAP.containsKey(channelId)) {
            //删除连接
            CHANNEL_MAP.remove(channelId);

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
        ByteBuf buf = (ByteBuf)msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        buf.resetReaderIndex();

        //转换为对象
        DeviceInfo deviceInfo = HexConvert.BinaryToDeviceInfo(bytes);
        //把连接放到redis中
        CacheUtils cacheUtils = (CacheUtils)SpringUtils.getBean("cacheUtils");
        cacheUtils.set(deviceInfo.getDevSN(),ctx.channel().id().asShortText());

        log.info("收到客户端报文......");
        log.info("【" + ctx.channel().id() + "】" + " :" + JSONObject.toJSONString(deviceInfo));
        //解密
        if(StringUtils.isEmpty(deviceInfo.getDevSN()) && StringUtils.isEmpty(deviceInfo.getDevKey())){
            log.info("密钥和SN号为空");
            ctx.close();
            return;
        }
        String encrypt = Utils.encrypt(deviceInfo.getDevSN());

        if(!encrypt.equals(deviceInfo.getDevKey())){
            log.info("解密失败");
            ctx.close();
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
            updateVersion(deviceInfo, ctx.channel().id());
        }

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
                log.info("升级设备数据信息"+JSONObject.toJSONString(result));
                //转换成字节
                byte[] bytes = HexConvert.updateVersionToBytes(result);
                ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
                Channel channel = ctx.channel();
                channel.writeAndFlush(byteBuf);
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
        DeviceEntity deviceEntity = deviceService.queryDeviceByCode(deviceInfo.getDevSN());
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

        String socketString = ctx.channel().remoteAddress().toString();

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("Client: " + socketString + " READER_IDLE 读超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("Client: " + socketString + " WRITER_IDLE 写超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("Client: " + socketString + " ALL_IDLE 总超时");
                ctx.disconnect();
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

        System.out.println();
        ctx.close();
        log.info(ctx.channel().id() + " 发生了错误,此连接被关闭" + "此时连通数量: " + CHANNEL_MAP.size());
        log.error("发生了错误:"+cause.getMessage(),cause);
        //cause.printStackTrace();
    }

}

