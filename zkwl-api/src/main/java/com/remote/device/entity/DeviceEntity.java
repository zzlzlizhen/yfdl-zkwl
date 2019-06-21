package com.remote.device.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/6/11 9:24
 * @Version 1.0
 **/
@Data
@TableName("fun_device")
public class DeviceEntity {
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
    private String photovoltaicCellVoltage;
    //充电电流
    private Double chargingCurrent;
    //充电功率
    private String chargingPower;
    //负载电压
    private String loadVoltage;
    //负载功率
    private String loadPower;
    //负载电流
    private String loadCurrent;

    private List<Integer> key;

    private List<Integer> value;
}
