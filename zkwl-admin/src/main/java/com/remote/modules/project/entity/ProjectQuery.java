package com.remote.modules.project.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author EDZ
 * @Date 2019/6/3 11:17
 * @Version 1.0
 **/
@Data
public class ProjectQuery {
    private Long userId;
    private String projectCode;
    private String projectName;
    private String userName;
    private Integer pageSize;
    private Integer pageNum;
    private List<Long> userIds = new ArrayList<>();

    public ProjectQuery(){};
    public ProjectQuery(Integer pageSize,Integer pageNum,String projectName,String projectCode,String userName){
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.projectName = projectName;
        this.projectCode = projectCode;
        this.userName = userName;
    }
}
