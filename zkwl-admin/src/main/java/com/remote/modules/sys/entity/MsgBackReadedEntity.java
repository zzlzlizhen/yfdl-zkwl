package com.remote.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author zsm
 * @date 2019-06-06 10:32:51
 */
@Data
@TableName("msg_back_readed")
public class MsgBackReadedEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.UUID)
	private Long id;
	/**
	 * 
	 */
	private Long uid;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 类型
	 */
	private Integer type;
	/**
	 * 消息id或者反馈id
	 */
	private String msgBackId;
	/**
	 * 设备id
	 */
	private String deviceId;

}
