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
@TableName("feedback")
public class FeedbackEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 反馈id
	 */
	@TableId(type = IdType.UUID)
	private String backId;
	/**
	 * 反馈用户id
	 */
	private Long uid;
	/**
	 * 反馈内容
	 */
	private String backContent;
	/**
	 * 时间
	 */
	private String backCreateTime;
	/**
	 * 反馈解答内容
	 */
	private String answerContent;
	/**
	 * 反馈解答人
	 */
	private String answerUser;
	/**
	 * 反馈解答时间
	 */
	private String answerCreateTime;

	/**
	 * 反馈邮箱
	 * */
	private String email;

	/**
	 * 反馈手机号
	 * */
	private String mobile;

}
