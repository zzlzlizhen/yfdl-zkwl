package com.remote.device.util;


import com.alibaba.fastjson.JSONObject;
import com.remote.common.CommonEntity;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.service.DeviceService;
import com.remote.history.entity.HistoryMouth;
import com.remote.history.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

import static com.remote.device.util.MapKey.mapKey;

/**
 * @Author zhangwenping
 * @Date 2019/6/13 17:47
 * @Version 1.0
 **/
@Component
//@RabbitListener(queues = "CalonDirectQueue")//CalonDirectQueue为队列名称
public class EchoServerNoBlock implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(EchoServerNoBlock.class);
    private int port;
    private volatile boolean stop;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static Map<String,SocketChannel> socketMap = new LinkedHashMap<>();
    private Integer count = 0;
    public EchoServerNoBlock(int port){
        this.port = port;
    }
    public EchoServerNoBlock(){}
    private List<List<Byte>> lists = new ArrayList<>();
    UpdateVersion updateVersion  = null;


    @RabbitHandler
    public void process(String str) {
       push(str);
    }


    public void init(){
        try {
            //打开一个选择器
            selector = Selector.open();
            //打开一个Server-Socket监听通道
            serverSocketChannel = ServerSocketChannel.open();
            //设置该通道为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //绑定端口Channels
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            //将通道注册在选择器上面，并将准备连接状态作为通道订阅时间
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            stop = false;
            logger.info("服务器已经启动，端口号：" + port);
        }catch (IOException e){
            logger.error("IOException:"+e.getMessage(),e);
            e.printStackTrace();
        }
    }

    public void run() {
        init();
        while (!stop){
            try {
                //无论是否有读写事件发生，selector每隔5s被唤醒一次
                selector.select(1000);
                //判断服务端是否修改了数据
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    handleKey(selectionKey);
                    iterator.remove();
                }
                selectionKeys.clear();
            } catch (IOException e) {
                logger.error("run:"+e.getMessage(),e);
                e.printStackTrace();
            }
        }
        //selector关闭后会自动释放里面管理的资源
//        if(selector != null){
//            try {
//                selector.close();
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }

    }

    private void handleKey(SelectionKey selectionKey) {
        try{
            //判断是否准备好接收新进入的连接* 客户端连接事件 */
            if (selectionKey.isAcceptable()) {

                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                //通过ServerSocketChannel的accept()创建SocketChannel实例
                SocketChannel socketChannel = serverSocketChannel.accept();
                //设置为非阻塞
                socketChannel.configureBlocking(false);
                //在选择器注册，并订阅读事件
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
            //可读事件
            if (selectionKey.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                //创建byteBuffer，并开辟一个1M的缓冲区
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                //读取请求码流，返回读取到的字节数
                int readBytes = socketChannel.read(byteBuffer);
                //判断客户端是否断开
                if (readBytes < 0) {
                    count = 0;
                    selectionKey.cancel();
                    socketChannel.close();
                    return;
                }
                //读取到字节，对字节进行编解码
                if (readBytes > 0) {
                    //将缓冲区从写模式切换到读模式
                    byteBuffer.flip();
                    //根据缓冲区可读字节数创建字节数组
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    //向缓冲区读数据到字节数组
                    byteBuffer.get(bytes);
                    //String expression = new String(bytes, "UTF-8");
                    DeviceInfo deviceInfo = HexConvert.BinaryToDeviceInfo(bytes);
                    StringBuffer sb = new StringBuffer();
                    for(int i=0; i < bytes.length ; i++){
                        sb.append(bytes[i]);
                    }
                    logger.info("服务器收到消息16位：" + sb.toString());
                    logger.info("服务器收到消息：" + JSONObject.toJSONString(deviceInfo));
                    String encrypt = Utils.encrypt(deviceInfo.getDevSN());
                    if(!encrypt.equals(deviceInfo.getDevKey())){
                        socketChannel.close();
                        return;
                    }
                    //判断客户端消息
                    socketMap.put(deviceInfo.getDevSN(), socketChannel);
                    logicalProcess(deviceInfo,socketChannel);
                }
            }
        }catch (Exception e){
            logger.error("处理客户端消息异常:"+e.getMessage(),e);
            e.printStackTrace();
        }
    }

    //服务端向客户端推送数据
    private void push(String msg){

        try{
            if(!"".equals(msg)){
                JSONObject jsonObject = JSONObject.parseObject(msg);
                DeviceEntity deviceEntity = JSONObject.toJavaObject(jsonObject, DeviceEntity.class);
                //代表操作设备
                onWhile(deviceEntity);
            }
        }catch (Exception e){
            msg = "";
            logger.error("服务端向客户端推送数据:"+e.getMessage(),e);
            e.printStackTrace();
        }
    }


    //代表操作设备
    private void onWhile(DeviceEntity deviceEntity){
        List<Integer> list = new ArrayList<>();
        List<Integer> value = new ArrayList<>();
        try {
            List<String> deviceCodes = deviceEntity.getDeviceCodes();
            if(deviceCodes != null && deviceCodes.size() > 0){
                for(String deviceSN : deviceCodes){
                    SocketChannel socketChannel = socketMap.get(deviceSN);
                    String encrypt = Utils.encrypt(deviceSN);
                    //需要客户端的设备信息
                    DeviceInfo result = new DeviceInfo(4,0,encrypt,deviceEntity.getDeviceType(),deviceSN);

                    list.addAll(deviceEntity.getKey());
                    value.addAll(deviceEntity.getValue());
                    result.setKey(list);
                    result.setValue(value);
                    result.setDataLen(list.size());
                    logger.info("操作设备："+JSONObject.toJSONString(result));
                    byte[] bytes = HexConvert.hexStringToBytes(result);
                    socketChannel.write(ByteBuffer.wrap(bytes));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("操作设备异常:"+e.getMessage(),e);
        }

    }


    //处理服务器接收客户端数据
    public void logicalProcess(DeviceInfo deviceInfo,SocketChannel socketChannel){
        try{
            Integer cmdID = deviceInfo.getCmdID();
            if(cmdID.equals(new Integer(1))){
                //需要客户端的设备信息
                //第一次连接需要向客户端要一些设备信息
                //conFirst(deviceInfo,socketChannel);
            }else if(cmdID.equals(new Integer(5))){
                //5终端发送需要上报的类型值 拿到客户端向服务端返回的设备信息做处理
                dataAnalysis(deviceInfo);
            }else if(cmdID.equals(new Integer(6))){
                //更新版本
                updateVersion(deviceInfo,socketChannel);
            }
        }catch (Exception e){
           e.printStackTrace();
           logger.error("处理服务器接受客户端数据异常:"+e.getMessage(),e);
        }
    }

    private void updateVersion(DeviceInfo deviceInfo, SocketChannel socketChannel)throws Exception {
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
                logger.info("升级设备数据信息"+JSONObject.toJSONString(result));
                //转换成字节
                byte[] bytes = HexConvert.updateVersionToBytes(result);
                socketChannel.write(ByteBuffer.wrap(bytes));
            }else{
                //升级完毕
                logger.info("升级设备完毕");
                count = 0;
                socketChannel.close();
            }
        }

    }

    public void conFirst(DeviceInfo deviceInfo,SocketChannel socketChannel) throws Exception{
        List<Integer> list = new ArrayList<>();
        //需要客户端的设备信息
        DeviceInfo result = new DeviceInfo(2,0,deviceInfo.getDevKey(),deviceInfo.getDevType(),deviceInfo.getDevSN());
        for (Map.Entry<Integer, String> entity : mapKey.entrySet()){
            list.add(entity.getKey());
        }
        result.setKey(list);
        result.setDataLen(list.size()+56);
        logger.info("返回数据json格式："+JSONObject.toJSON(result));
        byte[] bytes = HexConvert.hexStringToBytes(result);
        socketChannel.write(ByteBuffer.wrap(bytes));

    }

    //数据格式解析
    public void dataAnalysis(DeviceInfo deviceInfo) throws Exception {
        logger.info("设备上传的信息："+JSONObject.toJSONString(deviceInfo));
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
        HistoryService historyService = (HistoryService)SpringUtils.getBean("historyServiceImpl");
        historyService.insertHistoryData(historyMouth);

    }

}
