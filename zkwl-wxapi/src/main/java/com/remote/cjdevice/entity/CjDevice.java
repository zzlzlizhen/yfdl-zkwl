package com.remote.cjdevice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author zhangwenping
 * @Date 2019/9/6 10:59
 * @Version 1.0
 **/
@Data
@TableName("fun_cj_device")
public class CjDevice {

    private Long cjDeviceId;

    private String deviceCode;

    private String deviceTypeCode;

    private String deviceTypeName;

    private Integer communicationType;

    private Long createUser;

    private Date createTime;

    private Long updateUser;

    private Date updateTime;
}
