package com.remote.modules.group.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/6/4 9:50
 * @Version 1.0
 **/
@Data
public class GroupQuery {

    private String groupName;

    private String projectId;

    private Integer pageSize;

    private Integer pageNum;

}
