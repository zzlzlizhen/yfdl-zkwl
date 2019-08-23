package com.remote.modules.advancedsetting.entity;

import com.remote.common.validator.group.AddGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 
 * 
 * @author zsm
 * @email 1648925727@qq.com
 * @date 2019-06-20 09:22:19
 */
@Data
public class AdvancedSettingResult {
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
}
