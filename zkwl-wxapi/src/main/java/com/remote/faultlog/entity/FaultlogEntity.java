package com.remote.faultlog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author zhangwenping
 * @Date 2019/6/19 17:49
 * @Version 1.0
 **/
@Data
@TableName("fun_fault_log")
public class FaultlogEntity {
    private String faultLogId;
    private Long createUserId;
    private String faultLogName;
    private String projectId;
    private String deviceId;
    private Integer logStatus;
    private String faultLogDesc;
    private Date createTime;
    private String groupId;
}
