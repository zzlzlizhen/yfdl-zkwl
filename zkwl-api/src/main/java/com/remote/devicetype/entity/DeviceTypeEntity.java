package com.remote.devicetype.entity;

import lombok.Data;

/**
 * @Author zhangwepning
 * @Date 2019/8/21 10:18
 * @Version 1.0
 **/

@Data
public class DeviceTypeEntity {

    private Long deviceTypeId;

    private String deviceTypeCode;

    private String deviceTypeName;

    private String deviceTypePath;

    private Long createUser;

    private Long updateUser;

    private Long updateTime;
}
