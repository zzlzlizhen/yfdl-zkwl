package com.remote.modules.device.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author zhangwenping
 * @Date 2019/6/4 13:41
 * @Version 1.0
 **/
@Data
public class DeviceQuery extends DeviceEntity{

    private Integer pageSize;

    private Integer pageNum;

    private Integer noRunState;

    private List<String> deviceList = new ArrayList<>();
}
