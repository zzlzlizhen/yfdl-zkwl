package com.remote.modules.advancedsetting.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.remote.common.validator.group.AddGroup;
import lombok.Data;

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
public class AdvancedSettingResult extends AdvancedSettingEntity{
	private Integer sumTimer;
}
