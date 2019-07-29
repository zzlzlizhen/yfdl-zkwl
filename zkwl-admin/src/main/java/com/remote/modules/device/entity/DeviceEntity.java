package com.remote.modules.device.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author zhangwenping
 * @Date 2019/6/4 13:26
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
     * 设备编号
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
     * 分组名称
     */
    private String groupName;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 设备类型名称
     */
    private String deviceTypeName;
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
     * 设备状态  1 正常 2 报警 3 故障 4离线 5升级中
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

    private Integer counts;//统计数量
    //蓄电池余量
    private String batteryMargin;
    //蓄电池电压
    private String batteryVoltage;
    //光电池电压
    private String photovoltaicCellVoltage;
    //充电电流
    private String chargingCurrent;
    //充电功率
    private String chargingPower;
    //负载电压
    private String loadVoltage;
    //负载功率
    private String loadPower;
    //负载电流
    private String loadCurrent;
    //亮灯时长
    private String lightingDuration;
    //晨亮时长
    private String  morningHours;

    private String updateUserName;

    private String cityName;

    private Integer version;
}
