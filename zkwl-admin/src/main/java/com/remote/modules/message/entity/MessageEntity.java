package com.remote.modules.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * 
 * 
 * @author zsm
 * @date 2019-06-06 10:32:51
 */
@Data
@TableName("message")
public class MessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 消息id
	 */
	@TableId(type = IdType.UUID)
	private String msgId;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 消息内容
	 */
	private String content;
	/**
	 * 发布人
	 */
	private String publisher;
	/**
	 * 发布时间
	 */
	private String createDate;
	/**
	 * 消息类型
	 */
	private Integer type;
	/**
	 * 标签
	 */
	private String tip;

}
