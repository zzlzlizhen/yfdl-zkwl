package com.remote.project.service;


import com.github.pagehelper.PageInfo;
import com.remote.project.entity.ProjectEntity;
import com.remote.project.entity.ProjectQuery;

/*
 * @Author zhangwenping
 * @Description 项目信息
 * @Date 14:20 2019/5/31
 **/
public interface ProjectService {
    /*
     * @Author zhangwenping
     * @Description 添加项目
     * @Date 13:32 2019/6/29
     * @Param project
     * @return boolean
     **/
    boolean addProject(ProjectEntity project);

    /*
     * @Author zhangwenping
     * @Description 查询项目
     * @Date 13:46 2019/6/29
     * @Param projectQuery
     * @return pageInfo<projectEntity>
     **/
    PageInfo<ProjectEntity> queryProjectByUserIds(ProjectQuery projectQuery);
}
