package com.remote.modules.group.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author zhangwenping
 * @Date 2019/6/3 15:57
 * @Version 1.0
 **/

@Data
@TableName("fun_group")
public class GroupEntity {

    public GroupEntity(){

    }
    public GroupEntity(String groupId,String groupName){
        this.groupId = groupId;
        this.groupName = groupName;
    }
    /**
     * 分组id
     */
    private String groupId;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 分组名称
     */
    private String groupName;
    /**
     * 设备ids
     */
    private String deviceIds;
    /**
     * 创建人
     */
    private String createName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人id
     */
    private Long createUser;
    /**
     * 更新人id
     */
    private Long updateUser;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 报警数量
     */
    private Integer callPoliceCount;
    /**
     * 故障数量
     */
    private Integer faultCount;
    /**
     * 设备数量
     */
    private Integer deviceCount;

    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
}
