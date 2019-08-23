package com.remote.common;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/6/19 13:55
 * @Version 1.0
 **/
@Component
@Data
public class CommonEntity{
    private String mouthId;
    private String dayId;
    private String yearId;
    private Double dischargeCapacity;//放电量
    private Double chargingCapacity;//充电量
    private Double dischargeCurrent;//放电电流
    private Double ambientTemperature;//环境温度
    private Double internalTemperature;//内部温度
    private Double visitorsFlowrate;//人流量
    private Double inductionFrequency;//感应次数
    private Double meteorological;//气象信息
    private Integer fillingsNumber; //充满次数
    private Integer dischargeNumber;//冲放次数

    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 设备编号f
     */
    private String deviceCode;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 分组id
     */
    private String groupId;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 光电池状态
     */
    private Integer photocellState;
    /**
     * 蓄电池状态
     */
    private Integer batteryState;
    /**
     * 负载状态
     */
    private Integer loadState;
    /**
     * 信号状态
     */
    private Integer signalState;
    /**
     * 运行状态
     */
    private Integer runState;
    /**
     * 亮度
     */
    private String light;
    /**
     * 通讯类型
     */
    private Integer communicationType;
    /**
     * 开关
     */
    private Integer onOff;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 是否删除  1 已删除 0未删除
     */
    private Integer isDel;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 创建人姓名
     */
    private String createName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人id
     */
    private Long updateUser;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改状态
     */
    private boolean change;
    //设备编号
    private List<String> deviceCodes;
    //蓄电池余量
    private String batteryMargin;
    //蓄电池电压
    private Double batteryVoltage;
    //光电池电压
    private Double photovoltaicCellVoltage;
    //充电电流
    private Double chargingCurrent;
    //充电功率
    private Double chargingPower;
    //负载电压
    private Double loadVoltage;
    //负载功率
    private Double loadPower;
    //负载电流
    private Double loadCurrent;

    //亮灯时长
    private String lightingDuration;
    //晨亮时长
    private String  morningHours;

    private List<Integer> key;

    private List<Integer> value;

    //经度整数
    private String longitudeInt;
    //经度小数L
    private String longitudeL;
    //经度小数H
    private String longitudeH;
    //纬度整数
    private String latitudeInt;
    //纬度小数L
    private String latitudeL;
    //纬度小数H
    private String latitudeH;

    //总充电量
    private Double chargingCapacitySum;
    //总放电量
    private Double dischargeCapacitySum;
    //运行天数
    private Integer runDay;
    //故障日志
    private String faultLog;

    //软件版本
    private Integer version;

    //软件版本
    private Integer deviceVersion;

    private Integer gprsVersion;
}
