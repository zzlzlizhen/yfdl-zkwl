package com.remote.project.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author EDZ
 * @Date 2019/6/29 13:40
 * @Version 1.0
 **/
@Data
public class ProjectQuery {

    private long userId;

    private String parentId;

    private Integer pageSize;

    private String projectName;

    private Integer pageNum;

    private List<Long> userIds = new ArrayList<>();

    public ProjectQuery(Integer pageSize, Integer pageNum, String projectName) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.projectName = projectName;
    }
}
