package com.remote.modules.sys.entity;

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
@TableName("security")
public class SecurityEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 电话
	 */
	private String phone;
	/**
	 * 验证码
	 */
	private String securityCode;
	/**
	 * 验证码类型，手机，邮箱
	 */
	private String type;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 内容
	 * */
	private String content;
	/**
	 * 发送人id
	 * */
	private Long userId;

}
