package com.remote.device.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.remote.advancedsetting.entity.AdvancedSettingEntity;
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
public class DeviceEntityApi extends AdvancedSettingEntity{
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
     * 修改人
     */
    private String updateUser;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改状态
     */
    private boolean change;
    /**
     * 运输模式   0退出休眠  1进入休眠
     */
    private Integer transport;
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
    //总充电量
    private double chargingCapacitySum;
    //总放电量
    private double dischargeCapacitySum;
    //运行天数
    private Integer runDay;
    //1代表设备升级    2 代表操作设备
    private Integer status;
    //操作版本号
    private Integer deviceVersion;

    //当前版本号
    private Integer version;
    //将要升级的版本号
    private Integer futureVersion;
    //升级标识  0不升级 1升级
    private Integer futureFlag;
    //gprs 将要升级的版本
    private Integer gprsFutureVersion;
    /**
     * gprs版本号
     */
    private Integer gprsVersion;
    /**
     * gprs 升级标志
     */
    private Integer gprsFlag;
    //连接  0长连接  1短链接
    private Integer link;
}
