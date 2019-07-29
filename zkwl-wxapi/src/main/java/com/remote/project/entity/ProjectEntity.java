package com.remote.project.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.remote.sys.entity.SysUserEntity;
import lombok.Data;

import java.util.Date;

/**
 * @Author zhangwenping
 * @Date 2019/5/31 13:57
 * @Version 1.0
 **/
@Data
@TableName("fun_project")
public class ProjectEntity {

    private String projectId;
    private String projectCode;
    private String projectName;
    private String projectDesc;//项目描述',
    private Integer cityId;// 所属城市,
    private Long exclusiveUser;// 管理者id',
    private Integer runStatus;// 运行状态',
    private Integer sumCount;// 总装机数量',
    private Integer gatewayCount;// 网管数量',
    private Integer offlineCount;//离线数量
    private Integer normalCount;//正常数量
    private Integer faultCount;// 故障数量',
    private Integer callPoliceCount;// 报警数量',
    private Integer isDel;//  删除状态  1 删除 0未删除
    private Long createUser;// 创建人id',
    private String createName;//创建人姓名',
    private Date createTime;// 创建时间',
    private Long updateUser;// 更新人id',
    private Date updateTime;// 更新时间',
    private Integer projectStatus;// '项目状态 1 启动 2停用',
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    //使用者姓名
    private String exclusiveUserName;


}
