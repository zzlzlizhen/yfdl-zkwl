package com.remote.common;

import lombok.Data;

import java.util.Date;

/**
 * @Author zhagnwenping
 * @Date 2019/7/30 15:32
 * @Version 1.0
 **/
@Data
public class SetUp {

    private String light;//亮度
    private Integer onOff;//开关
    private String lightingDuration;//亮灯时长
    private String morningHours;//晨亮时长


    /**
     * 负载工作模式 0：定时模式 1：光控模式 2：智能 3：常开 4：常关； 5：测试
     */
    private Integer loadWorkMode;
    /**
     * 负载功率
     */
    private Integer powerLoad;
    /**
     * 开灯时刻
     */
    private Integer timeTurnOn;
    /**
     * 关灯时刻
     */
    private Integer timeTurnOff;
    /**
     * 1时段时长
     */
    private Integer time1;
    /**
     * 2时段时长
     */
    private Integer time2;
    /**
     * 3时段时长
     */
    private Integer time3;
    /**
     * 4时段时长
     */
    private Integer time4;
    /**
     * 5时段时长
     */
    private Integer time5;
    /**
     * 晨亮时长
     */
    private Integer timeDown;
    /**
     * 时段1 有人功率
     */
    private Integer powerPeople1;
    /**
     * 时段2 有人功率
     */
    private Integer powerPeople2;
    /**
     * 时段 3 有人功率
     */
    private Integer powerPeople3;
    /**
     * 时段 4 有人功率
     */
    private Integer powerPeople4;
    /**
     * 时段 5 有人功率
     */
    private Integer powerPeople5;
    /**
     * 晨亮 有人功率
     */
    private Integer powerDawnPeople;
    /**
     * 时段 1 无人功率
     */
    private Integer powerSensor1;
    /**
     * 时段 2 无人功率
     */
    private Integer powerSensor2;
    /**
     * 时段 3 无人功率
     */
    private Integer powerSensor3;
    /**
     * 时段 4 无人功率
     */
    private Integer powerSensor4;
    /**
     * 时段 5 无人功率
     */
    private Integer powerSensor5;
    /**
     * 晨亮 无人功率
     */
    private Integer powerSensorDown;
    /**
     * 节能开关
     */
    private Integer savingSwitch;
    /**
     * 自动休眠时间长度
     */
    private Integer autoSleepTime;
    /**
     * 光控电压
     */
    private Integer vpv;
    /**
     * 光控延迟时间
     */
    private Integer ligntOnDuration;
    /**
     * 光控关灯开关
     */
    private Integer pvSwitch;
    /**
     *  电池类型 0:未设置；1：胶体电池；2：铅酸电池； 3磷酸铁锂电池；4：三元锂电池
     */
    private Integer batType;
    /**
     * 电池串数
     */
    private Integer batStringNum;
    /**
     * 过放电压
     */
    private Integer volOverDisCharge;
    /**
     * 充电电压
     */
    private Integer volCharge;
    /**
     * 充电电流
     */
    private Integer iCharge;

    /**
     * 充电温度范围
     */
    private Integer tempCharge;
    /**
     * 放电温度范围
     */
    private Integer tempDisCharge;

    /**
     * 巡检时间
     */
    private Integer inspectionTime;
    /**
     * 感应开关
     */
    private Integer inductionSwitch;
    /**
     * 感应后的亮灯延时
     */
    private Integer inductionLightOnDelay;

    /**
     * 项目id
     * */
    private String deviceCode;
    /**
     * 组id
     * */
    private String groupId;
    /**
     * 用户id
     * */
    private Long uid;
    /*
     * 创建时间
     * */
    private Date createTime;
    /**
     * 更新时间
     * */
    private Date updateTime;
    /**
     * 更新人
     * */
    private String updateUser;
    /**
     *一阶降功率电压
     */
    private Integer firDownPower;
    /**
     *二阶降功率电压
     */
    private Integer twoDownPower;
    /**
     *三阶降功率电压
     */
    private Integer threeDownPower;
    /*
     * 一阶降功率幅度
     * */
    private Integer firReducAmplitude;
    /*
     * 二阶降功率幅度
     * */
    private Integer twoReducAmplitude;
    /*
     * 三阶降功率幅度
     * */
    private Integer threeReducAmplitude;
    /**
     * 开关灯延时时间
     * */
    private Integer switchDelayTime;
}
