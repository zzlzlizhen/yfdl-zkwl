package com.remote.device.util;


import com.alibaba.fastjson.JSONObject;
import com.remote.common.CommonEntity;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.service.DeviceService;
import com.remote.history.entity.HistoryMouth;
import com.remote.history.service.HistoryService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RabbitListener(queues = "CalonDirectQueue")//CalonDirectQueue为队列名称
public class EchoServerNoBlock implements Runnable {
    private int port;
    private volatile boolean stop;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static Map<String,SocketChannel> socketMap = new LinkedHashMap<>();
    private static String msg = "";

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private HistoryService historyService;

    public EchoServerNoBlock(int port){
        this.port = port;
    }
    public EchoServerNoBlock(){}

    @RabbitHandler
    public void process(String str) {
        msg = str;
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
            System.out.println("服务器已经启动，端口号：" + port);
        }catch (IOException e){
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
                push();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    handleKey(selectionKey);
                    iterator.remove();
                }
                selectionKeys.clear();
            } catch (IOException e) {
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
                socketChannel.write(ByteBuffer.wrap("success".getBytes()));
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
                    selectionKey.cancel();
                    //socketChannel.close();
                    //return;
                }
                //读取到字节，对字节进行编解码
                if (readBytes > 0) {
                    //将缓冲区从写模式切换到读模式
                    byteBuffer.flip();
                    //根据缓冲区可读字节数创建字节数组
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    //向缓冲区读数据到字节数组
                    byteBuffer.get(bytes);
                    String expression = new String(bytes, "UTF-8");
                    System.out.println("服务器收到消息：" + expression);
                    //处理客户端的数据
                    JSONObject jsonObject = JSONObject.parseObject(expression);
                    DeviceInfo deviceInfo = JSONObject.toJavaObject(jsonObject, DeviceInfo.class);
                    //判断客户端消息
                    socketMap.put(deviceInfo.getDevSN(), socketChannel);
                    logicalProcess(deviceInfo,socketChannel);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //服务端向客户端推送数据
    private void push(){
        List<Integer> list = new ArrayList<>();
        List<Integer> value = new ArrayList<>();
        try{
            if(!"".equals(msg)){
                System.out.println(msg);
                JSONObject jsonObject = JSONObject.parseObject(msg);
                DeviceEntity deviceEntity = JSONObject.toJavaObject(jsonObject, DeviceEntity.class);
                //代表操作设备
                onWhile(list,value,deviceEntity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //代表操作设备
    private void onWhile(List<Integer> list,List<Integer> value,DeviceEntity deviceEntity){
        try {
            List<String> deviceCodes = deviceEntity.getDeviceCodes();
            if(deviceCodes != null && deviceCodes.size() > 0){
                for(String deviceSN : deviceCodes){
                    SocketChannel socketChannel = socketMap.get(deviceSN);
                    String encrypt = Utils.encrypt(deviceSN);
                    //需要客户端的设备信息
                    DeviceInfo result = new DeviceInfo(4,0,encrypt,deviceEntity.getDeviceType(),deviceSN);
                    List<Integer> key = deviceEntity.getKey();
                    //判断操作参数是否大于40，确保一次性发送40个数据
                    if(key.size() > 40){
                        List<List<Integer>> keys = Utils.averageAssign(key, 40);
                        List<List<Integer>> values = Utils.averageAssign(value, 40);
                        for(int i=0;i<keys.size();i++){
                            list.addAll(keys.get(i));
                            value.addAll(values.get(i));
                            result.setKey(list);
                            result.setValue(value);
                            result.setDataLen(list.size());
                            String s = JSONObject.toJSONString(result);
                            socketChannel.write(ByteBuffer.wrap(s.getBytes()));
                        }
                    }else{
                        list.addAll(deviceEntity.getKey());
                        value.addAll(deviceEntity.getValue());
                        result.setKey(list);
                        result.setValue(value);
                        result.setDataLen(list.size());
                        String s = JSONObject.toJSONString(result);
                        socketChannel.write(ByteBuffer.wrap(s.getBytes()));
                    }
                }
            }
            msg = "";
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //处理服务器接收客户端数据
    public void logicalProcess(DeviceInfo deviceInfo,SocketChannel socketChannel){
        try{
            Integer cmdID = deviceInfo.getCmdID();
            if(cmdID.equals(new Integer(1))){
                //需要客户端的设备信息
                //第一次连接需要向客户端要一些设备信息
                conFirst(deviceInfo,socketChannel);
            }else if(cmdID.equals(new Integer(3))){
                //3终端发送需要上报的类型值 拿到客户端向服务端返回的设备信息做处理
                dataAnalysis(deviceInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void conFirst(DeviceInfo deviceInfo,SocketChannel socketChannel) throws Exception{
        List<Integer> list = new ArrayList<>();
        //需要客户端的设备信息
        DeviceInfo result = new DeviceInfo(2,0,deviceInfo.getDevKey(),deviceInfo.getDevType(),deviceInfo.getDevSN());
        for (Map.Entry<Integer, String> entity : mapKey.entrySet()){
            list.add(entity.getKey());
        }
        //当需要40个以上参数时，将参数分段发送给客户端
        if(list.size() > 40){
            //调用拆分list方法
            List<List<Integer>> lists = Utils.averageAssign(list, 40);
            for (List<Integer> list1 : lists){
                result.setKey(list1);
                result.setDataLen(list1.size());
                String s = JSONObject.toJSONString(result);
                socketChannel.write(ByteBuffer.wrap(s.getBytes()));
            }
        }else{
            result.setKey(list);
            result.setDataLen(list.size());
            String s = JSONObject.toJSONString(result);
            socketChannel.write(ByteBuffer.wrap(s.getBytes()));
        }
    }

    //数据格式解析
    public void dataAnalysis(DeviceInfo deviceInfo) throws Exception {
        List<Integer> upKey = deviceInfo.getKey();
        List<Integer> upValue = deviceInfo.getValue();
        List<String> dataKey = new ArrayList<>();
        //将所有参数的key取出解析存放到dataKey下
        for(Integer integer : upKey){
            dataKey.add(mapKey.get(integer));
        }
        //根据devSN查询设备信息
        DeviceEntity deviceEntity = deviceService.queryDeviceByCode(deviceInfo.getDevSN());
        //提供一个公共的类，类中存放设备全部信息  否则反射时会找不到属性。start
        CommonEntity common = new CommonEntity();
        BeanUtils.copyProperties(deviceEntity, common);
        //提供一个公共的类，类中存放设备全部信息  否则反射时会找不到属性。end
        Integer index = 0;
        if(dataKey != null && dataKey.size() > 0){
            for (String str : dataKey){
                Field field = common.getClass().getDeclaredField(str);
                field.set(str,upValue.get(index));
                index ++;
            }
        }
        //所有设备属性都映射到公共的类中，需要哪些信息，自行转换 start
        BeanUtils.copyProperties(common, deviceEntity);
        //不论什么数据，首先修改设备信息
        deviceService.updateDeviceByCode(deviceEntity);
        //所有设备属性都映射到公共的类中，需要哪些信息，自行转换 end
        //判断公共类属于历史数据，还是设备信息
        //判断是否时历史数据
        if(common.getDischargeCapacity() != null && common.getChargingCapacity() != null){
            HistoryMouth historyMouth = new HistoryMouth();
            BeanUtils.copyProperties(common, historyMouth);
            historyService.insertHistoryData(historyMouth);
        }
    }

}
