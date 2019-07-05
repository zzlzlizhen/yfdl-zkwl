package com.remote.project.service;


import com.github.pagehelper.PageInfo;
import com.remote.project.entity.ProjectEntity;
import com.remote.project.entity.ProjectQuery;

import java.util.List;

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
    /*
     * @Author chengpunan
     * @Description 删除项目
     * @Date 16:47 2019/7/3
     * @Param
     * @return
     **/
    boolean delProject(List<String> projectList, Long userId);

    /*
     * @Author zhangwenping
     * @Description 修改项目
     * @Date 15:08 2019/6/3
     * @Param projectEntity
     * @return boolean
     **/
    boolean updateProject(ProjectEntity projectEntity);
}
