package com.remote.modules.advancedsetting.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.remote.common.validator.group.AddGroup;
import io.swagger.models.auth.In;
import jdk.nashorn.internal.ir.IdentNode;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author zsm
 * @email 1648925727@qq.com
 * @date 2019-06-20 09:22:19
 */
@Data
@TableName("advanced_setting")
public class AdvancedSettingEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId

	private Long id;
	/**
	 * 负载工作模式 0：定时模式 1：光控模式 2：智能 3：常开 4：常关； 5：测试
	 */
	@NotNull(message="负载工作模式不能为空", groups = AddGroup.class)
	private Integer loadWorkMode;
	/**
	 * 负载功率
	 */
	@NotNull(message="负载功率不能为空", groups = AddGroup.class)
	private Integer powerLoad;
	/**
	 * 开灯时刻
	 */
	@NotNull(message="开灯时刻不能为空", groups = AddGroup.class)
	private Integer timeTurnOn;
	/**
	 * 关灯时刻
	 */
	@NotNull(message="关灯时刻不能为空", groups = AddGroup.class)
	private Integer timeTurnOff;
	/**
	 * 1时段时长
	 */
	@NotNull(message="1时段时长不能为空", groups = AddGroup.class)
	private Integer time1;
	/**
	 * 2时段时长
	 */
	@NotNull(message="2时段时长不能为空", groups = AddGroup.class)
	private Integer time2;
	/**
	 * 3时段时长
	 */
	@NotNull(message="3时段时长不能为空", groups = AddGroup.class)
	private Integer time3;
	/**
	 * 4时段时长
	 */
	@NotNull(message="4时段时长不能为空", groups = AddGroup.class)
	private Integer time4;
	/**
	 * 5时段时长
	 */
	@NotNull(message="5时段时长不能为空", groups = AddGroup.class)
	private Integer time5;
	/**
	 * 晨亮时长
	 */
	@NotNull(message="晨亮时长不能为空", groups = AddGroup.class)
	private Integer timeDown;
	/**
	 * 时段1 有人功率
	 */
	@NotNull(message="时段1有人功率不能为空", groups = AddGroup.class)
	private Integer powerPeople1;
	/**
	 * 时段2 有人功率
	 */
	@NotNull(message="时段2有人功率不能为空", groups = AddGroup.class)
	private Integer powerPeople2;
	/**
	 * 时段 3 有人功率
	 */
	@NotNull(message="时段3有人功率不能为空", groups = AddGroup.class)
	private Integer powerPeople3;
	/**
	 * 时段 4 有人功率
	 */
	@NotNull(message="时段4有人功率不能为空", groups = AddGroup.class)
	private Integer powerPeople4;
	/**
	 * 时段 5 有人功率
	 */
	@NotNull(message="时段5有人功率不能为空", groups = AddGroup.class)
	private Integer powerPeople5;
	/**
	 * 晨亮有人功率
	 */
	@NotNull(message="晨亮有人功率不能为空", groups = AddGroup.class)
	private Integer powerDawnPeople;
	/**
	 * 时段 1 无人功率
	 */
	@NotNull(message="时段1无人功率不能为空", groups = AddGroup.class)
	private Integer powerSensor1;
	/**
	 * 时段 2 无人功率
	 */
	@NotNull(message="时段2无人功率不能为空", groups = AddGroup.class)
	private Integer powerSensor2;
	/**
	 * 时段 3 无人功率
	 */
	@NotNull(message="时段3无人功率不能为空", groups = AddGroup.class)
	private Integer powerSensor3;
	/**
	 * 时段 4 无人功率
	 */
	@NotNull(message="时段4无人功率不能为空", groups = AddGroup.class)
	private Integer powerSensor4;
	/**
	 * 时段 5 无人功率
	 */
	@NotNull(message="时段5无人功率不能为空", groups = AddGroup.class)
	private Integer powerSensor5;
	/**
	 * 晨亮 无人功率
	 */
	@NotNull(message="晨亮无人功率不能为空", groups = AddGroup.class)
	private Integer powerSensorDown;
	/**
	 * 节能开关
	 */
	@NotNull(message="节能开关不能为空", groups = AddGroup.class)
	private Integer savingSwitch;
	/**
	 * 自动休眠时间长度
	 */
	@NotNull(message="负载功率", groups = AddGroup.class)
	private Integer autoSleepTime;
	/**
	 * 光控电压
	 */
	@NotNull(message="光控电压不能为空", groups = AddGroup.class)
	private Integer vpv;
	/**
	 * 光控延迟时间
	 */
	@NotNull(message="光控延迟时间不能为空", groups = AddGroup.class)
	private Integer ligntOnDuration;
	/**
	 * 光控关灯开关
	 */
	@NotNull(message="光控关灯开关不能为空", groups = AddGroup.class)
	private Integer pvSwitch;
	/**
	 *  电池类型 0:未设置；1：胶体电池；2：铅酸电池； 3磷酸铁锂电池；4：三元锂电池
	 */
	@NotNull(message="电池类型不能为空", groups = AddGroup.class)
	private Integer batType;
	/**
	 * 电池串数
	 */
	@NotNull(message="电池串数不能为空", groups = AddGroup.class)
	private Integer batStringNum;
	/**
	 * 过放电压
	 */
	@NotNull(message="过放电压不能为空", groups = AddGroup.class)
	private Integer volOverDisCharge;
	/**
	 * 充电电压
	 */
	@NotNull(message="充电电压不能为空", groups = AddGroup.class)
	private Integer volCharge;
	/**
	 * 充电电流
	 */
	@NotNull(message="充电电流不能为空", groups = AddGroup.class)
	private Integer iCharge;
	/**
	 * 充电温度范围
	 */
	@NotNull(message="充电温度范围不能为空", groups = AddGroup.class)
	private Integer tempCharge;

	/**
	 * 放电温度范围
	 */
	@NotNull(message="放电温度范围不能为空", groups = AddGroup.class)
	private Integer tempDisCharge;

	/**
	 * 巡检时间
	 */
	@NotNull(message="巡检时间不能为空", groups = AddGroup.class)
	private Integer inspectionTime;
	/**
	 * 感应开关
	 */@NotNull(message="感应开关不能为空", groups = AddGroup.class)
	private Integer inductionSwitch;
	/**
	 * 感应后的亮灯延时
	 */
	@NotNull(message="感应后的亮灯延时不能为空", groups = AddGroup.class)
	private Integer inductionLightOnDelay;

	/**
	 * 设备code
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
	@NotNull(message="一阶降功率电压不能为空", groups = AddGroup.class)
	private Integer firDownPower;
	/**
	 *二阶降功率电压
	 */
	@NotNull(message="二阶降功率电压不能为空", groups = AddGroup.class)
	private Integer twoDownPower;
	/**
	 *三阶降功率电压
	 */
	@NotNull(message="三阶降功率电压不能为空", groups = AddGroup.class)
	private Integer threeDownPower;
	/*
	* 一阶降功率幅度
	* */
	@NotNull(message="一阶降功率幅度不能为空", groups = AddGroup.class)
	private Integer firReducAmplitude;
	/*
	* 二阶降功率幅度
	* */
	@NotNull(message="二阶降功率幅度不能为空", groups = AddGroup.class)
	private Integer twoReducAmplitude;
		/*
	* 三阶降功率幅度
	* */
	@NotNull(message="三阶降功率幅度不能为空", groups = AddGroup.class)
	private Integer threeReducAmplitude;
	/**
	 * 开关灯延时时间
	 * */
	@NotNull(message="开关灯延时时间不能为空", groups = AddGroup.class)
	private Integer switchDelayTime;
}
