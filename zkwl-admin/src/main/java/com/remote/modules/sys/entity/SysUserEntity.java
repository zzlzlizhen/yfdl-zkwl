
package com.remote.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.remote.common.validator.group.AddGroup;
import com.remote.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 */
@Data
@TableName("sys_user")
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	@TableId
	private Long userId;

	/**
	 * 用户名
	 */
	/*@NotBlank(message="用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})*/
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message="密码不能为空", groups = AddGroup.class)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	/**
	 * 盐
	 */
	private String salt;

	/**
	 * 邮箱
	 */
	@Email(message="邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
	private String email;

	/**
	 * 手机号
	 */
/*	@NotBlank(message="手机号不能为空", groups = AddGroup.class)*/
	private String mobile;

	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer status;
	
	/**
	 * 角色ID列表
	 */
	@TableField(exist=false)
	private List<Long> roleIdList;

	/**
	 * 用户类型 0使用者  1  管理者  2 超级管理员
	 * */
	@TableField(exist=false)
	private Long roleId;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 部门ID
	 */
	/*@NotNull(message="部门不能为空", groups = {AddGroup.class, UpdateGroup.class})*/
	private Long deptId;

	/**
	 * 部门名称
	 */
	@TableField(exist=false)
	private String deptName;

	private Long parentId;//父ids

	private String allParentId;//父类ids

	private Integer deviceCount; //设备数量

	private Integer projectCount;//项目数量

	private String headUrl;//头像

	private Date updateTime;

	private Integer updateUser;

	private String realName;//真实姓名

	/**
	 * 有效期 0：半年  1： 一年 2：俩年  3：三年  4：永久
	 * */
	private Integer termOfValidity;

	/**
	 * 邮箱绑定状态 0未绑定 1已绑定
	 * */
	private Integer type;

	/**
	 * 是否删除
	 * */
	private Integer flag;

}
