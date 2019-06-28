package com.remote.device.util;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/6/26 18:28
 * @Version 1.0
 **/
@Data
public class DeviceVersionInfo {
    public DeviceVersionInfo(){};

    public DeviceVersionInfo(Integer cmdID, Integer nextCmdID,String devKey,String devType,String devSN,Integer upgradeFlag,Integer binSize,Integer checkSum){
        this.cmdID = cmdID;
        this.nextCmdID = nextCmdID;
        this.devKey = devKey;
        this.devType = devType;
        this.devSN = devSN;
        this.upgradeFlag = upgradeFlag;
        this.binSize = binSize;
        this.checkSum = checkSum;
    }

    private Integer dataLen;
    private Integer cmdID; //读写数据标识  1终端请求事件 2服务器返回需要上报的类型长度 3终端发送需要上报的类型值 4服务器返回需要修改的配置参数
    private Integer nextCmdID; //没有请求 返回0
    private String devKey;//加密的sn
    private String devType;//类型  不做处理
    private String devSN;//设备sn
    private Integer upgradeFlag; //升级标志
    private Integer binSize;//length
    private Integer binLastSize;//还剩下多少
    private Integer checkSum;//sum
    private Byte [] bin = new Byte[1024];//数据
    private Integer number;//第几次

}
