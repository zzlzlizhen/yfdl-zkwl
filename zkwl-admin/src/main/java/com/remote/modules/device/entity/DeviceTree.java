package com.remote.modules.device.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author zhagnwenping
 * @Date 2019/8/7 11:29
 * @Version 1.0
 **/
@Data
public class DeviceTree {
    private String id;

    private String name;

    private Integer runState;

    private Integer deviceNumber;

    private Integer projectStatus;

    private List<DeviceTree> children;

    private String parentId;
    //经度
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;

    private String deviceCode;

    private String deviceType;
}
