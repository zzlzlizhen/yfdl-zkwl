package com.remote.project.service;


import com.github.pagehelper.PageInfo;
import com.remote.project.entity.ProjectEntity;
import com.remote.project.entity.ProjectQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * @Author zhangwenping
 * @Description 项目信息
 * @Date 14:20 2019/5/31
 **/
@Transactional
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
    /*
     * @Author zhangwenping
     * @Description 根据id查询项目详情
     * @Date 13:46 2019/7/29
     * @Param projectId
     * @return ProjectEntity
     **/
    ProjectEntity queryProjectMap(String projectId);
    /*
     * @Author zhangwenping
     * @Description 查询用户下面的项目数量
     * @Date 19:17 2019/7/8
     * @Param userId
     * @return int
     **/
    int queryProjectByUserCount(Long userId);
    /**
     * 通过项目ids查询所属人ids
     * */
    List<Long> queryExclusiveIds(List<String> projectIds);
    /**
     *通过项目id查询所属用户id
     * */
    Long queryExclusiveId(String projectId);
}
