package com.remote.device.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.service.DeviceService;
import com.remote.device.service.impl.DeviceServiceImpl;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static com.remote.device.util.MapKey.mapKey;


/**
 * @Author EDZ
 * @Date 2019/6/12 10:36
 * @Version 1.0
 **/
@Component
public class SocketServer {


    private final static Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);

    @Autowired
    private DeviceService deviceService;

    public DeviceInfo deviceInfo;

    public Integer flag = 0;

    BufferedReader reader;
    BufferedWriter writer;
    public void startAction(){
        ServerSocket serverSocket=null;
        try {
            serverSocket=new ServerSocket(2003);  //端口号
            LOGGER.info("服务端服务启动监听：");
            //通过死循环开启长连接，开启线程去处理消息
            while(true){
                Socket socket=serverSocket.accept();
                new Thread(new MyRuns(socket)).start();
  //              new Thread(new MyRunsOne(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
    class MyRuns implements Runnable{

        Socket socket;


        public MyRuns(Socket socket) {
            super();
            this.socket = socket;
        }

        public void run() {

            while (!isServerClose(socket)){
                List<Integer> list = new ArrayList<>();
                List<Integer> value = new ArrayList<>();

                try {
                    System.out.print("MyRuns");
                    char [] bytes = new char[1024];
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//读取客户端消息
                    reader.read(bytes);

                    //把字节数组转成字符串
                    String str = new String(bytes);
                    JSONObject jsonObject=JSONObject.parseObject(str);
                    System.out.print(jsonObject);
                    deviceInfo = JSONObject.toJavaObject(jsonObject, DeviceInfo.class);
                    flag = 1;
//                    //查询设备状态
//                    DeviceEntity deviceEntity = deviceService.queryDeviceByCode(deviceInfo.getDevSN());
//                    if(deviceEntity.getOnOff() != null){
//                        DeviceInfo result = new DeviceInfo(4,0,deviceInfo.getDevKey(),deviceInfo.getDevType(),deviceInfo.getDevSN());
//                        list.add(54);
//                        value.add(deviceEntity.getOnOff());
//                        result.setKey(list);
//                        result.setValue(value);
//                        result.setDataLen(list.size());
//                        String s = JSONObject.toJSONString(result);
//                        writer.write(s);
//                        writer.flush();
//                    }
//                    System.out.print(jsonObject);
//                    Integer cmdID = deviceInfo.getCmdID();
//                    if(cmdID.equals(new Integer(1))){
//                        //需要客户端的设备信息
//                        DeviceInfo result = new DeviceInfo(2,0,deviceInfo.getDevKey(),deviceInfo.getDevType(),deviceInfo.getDevSN());
//                        for (Map.Entry<Integer, String> entity : mapKey.entrySet()){
//                            list.add(entity.getKey());
//                        }
//                        result.setKey(list);
//                        result.setDataLen(list.size());
//                        String s = JSONObject.toJSONString(result);
//                        //String replace = replace(s);
//                        writer.write(s);
//                        writer.flush();
//                    }else if(cmdID.equals(new Integer(3))){
//                        //3终端发送需要上报的类型值
//
//                    }

                    //writer.write(str);
                    //writer.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
//                        if (reader!=null) {
//                            reader.close();
//                        }
//                        if (writer!=null) {
//                            writer.close();
//                        }
//                        if(socket != null){
//                            socket.close();
//                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }

    }

    class MyRunsOne implements Runnable{

        Socket socket;

        public MyRunsOne(Socket socket) {
            super();
            this.socket = socket;
        }

        public void run() {

            while (!isServerClose(socket)){
                List<Integer> list = new ArrayList<>();
                List<Integer> value = new ArrayList<>();

                try {
                    writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//向客户端写消息
//                    char [] bytes = new char[1024];
//                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//读取客户端消息
//                    writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//向客户端写消息
//                    reader.read(bytes);
//                    //把字节数组转成字符串
//                    String str = new String(bytes);
//                    JSONObject jsonObject=JSONObject.parseObject(str);
//                    DeviceInfo deviceInfo = JSONObject.toJavaObject(jsonObject, DeviceInfo.class);
//                    //查询设备状态
//                    DeviceEntity deviceEntity = deviceService.queryDeviceByCode(deviceInfo.getDevSN());
//                    if(deviceEntity.getOnOff() != null){
//                        DeviceInfo result = new DeviceInfo(4,0,deviceInfo.getDevKey(),deviceInfo.getDevType(),deviceInfo.getDevSN());
//                        list.add(54);
//                        value.add(deviceEntity.getOnOff());
//                        result.setKey(list);
//                        result.setValue(value);
//                        result.setDataLen(list.size());
//                        String s = JSONObject.toJSONString(result);
//                        writer.write(s);
//                        writer.flush();
//                    }
                    if(flag==1){
                        String s = JSONObject.toJSONString(deviceInfo);
                        flag = 0;
                        writer.write(s);
                        writer.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
//                        if (reader!=null) {
//                            reader.close();
//                        }
//                        if (writer!=null) {
//                            writer.close();
//                        }
//                        if(socket != null){
//                            socket.close();
//                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 判断是否断开连接，断开返回true,没有返回false
     * @param socket
     * @return
     */
    public Boolean isServerClose(Socket socket){
        try{
            socket.sendUrgentData(0xFF);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            return false;
        }catch(Exception se){
            return true;
        }
    }

    public String replace(String str){
        str = str.substring(1);
        str = str.substring(0,str.length() - 1);
        int start = str.indexOf("{");
        int end = str.indexOf("}");
        String s = str.replaceAll("\\{", "[");
        String newStr = s.replaceAll("}", "]");
        return "{"+newStr+"}";
    }
}
