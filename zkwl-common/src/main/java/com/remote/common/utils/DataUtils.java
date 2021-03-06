package com.remote.common.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/6/17 18:10
 * @Version 1.0
 **/
@Data
public class DataUtils implements Serializable {
    private static final long serialVersionUID = -8819445301520699618L;
    //设备编号
    private List<String> deviceCodes;
    //用于将数据解析传给socket
    private List<Integer> key;
    //用于数据传给socket
    private List<Integer> value;
    //设备类型
    private String deviceType;
    //用于前端发送数据
    private List<String> qaKey;
    //用于分组操作
    private String groupId;
    //用于项目操作
    private String projectId;
    //用于区分是升级还是操作  1设备升级  2操作设备
    private Integer status;
    //更新版本号
    private Integer version;
}
