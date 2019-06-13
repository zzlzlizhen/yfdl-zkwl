package com.remote.device.util;

import io.swagger.models.auth.In;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author EDZ
 * @Date 2019/6/11 19:20
 * @Version 1.0
 **/
@Data
public class DeviceInfo {

    public DeviceInfo(){};

    public DeviceInfo(Integer cmdID, Integer nextCmdID,String devKey,Integer devType,String devSN){
        this.cmdID = cmdID;
        this.nextCmdID = nextCmdID;
        this.devKey = devKey;
        this.devType = devType;
        this.devSN = devSN;
    }
    private Integer dataLen;
    private Integer cmdID; //读写数据标识  1终端请求事件 2服务器返回需要上报的类型长度 3终端发送需要上报的类型值 4服务器返回需要修改的配置参数
    private Integer nextCmdID; //没有请求 返回0
    private String devKey;//加密的sn
    private Integer devType;//类型  不做处理
    private String devSN;//设备sn
    private List<Integer> key = new ArrayList<>();
    private List<Integer> value = new ArrayList<>();
    public void wh(){
        if(cmdID.equals(new Integer(1))){
            //如果需要客户端再次请求,cmdId 返回2  nextCmdId 返回3
        }
        if(cmdID.equals(new Integer(3))){

        }
    }
}
