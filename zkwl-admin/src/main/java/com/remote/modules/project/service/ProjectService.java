package com.remote.modules.project.service;

import com.github.pagehelper.PageInfo;
import com.remote.modules.project.entity.ProjectEntity;
import com.remote.modules.project.entity.ProjectQuery;
import com.remote.modules.project.entity.ProjectResult;
import com.remote.modules.sys.entity.SysUserEntity;

import java.util.List;

/*
 * @Author zhangwenping
 * @Description 项目信息
 * @Date 14:20 2019/5/31
 **/
public interface ProjectService {

    /*
     * @Author zhangwenping
     * @Description 添加项目信息
     * @Date 14:21 2019/5/31
     * @Param projectEntity
     * @return boolean
     **/
    boolean addProject(ProjectEntity projectEntity);

    /*
     * @Author zhangwenping
     * @Description 查询当前用户下所有项目 （包括下级单位的项目）
     * @Date 17:14 2019/5/31
     * @Param projectQuery
     * @return PageInfo<ProjectEntity>
     **/
    PageInfo<ProjectEntity> queryProjectByUserIds(ProjectQuery projectQuery);

    /*
     * @Author zhangwenping
     * @Description 删除项目
     * @Date 13:33 2019/6/3
     * @Param projectList,updateUser
     * @return boolean
     **/
    boolean delProject(List<String> projectList,Long updateUser);
    /*
     * @Author zhangwenping
     * @Description 修改项目
     * @Date 15:08 2019/6/3
     * @Param projectEntity
     * @return boolean
     **/
    boolean updateProject(ProjectEntity projectEntity) throws Exception;
    /*
     * @Author zhangwenping
     * @Description 查询项目不分页
     * @Date 10:27 2019/6/6
     * @Param userId deviceStatus
     * @return List<ProjectEntity>
     **/
    List<ProjectEntity> queryProjectNoPage(Long userId,Integer deviceStatus);
    /*
     * @Author zhangwenping
     * @Description 在地图中查看信息
     * @Date 14:34 2019/6/11
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
    /*
     * @Author zhangwenping
     * @Description 查询地图右边信息
     * @Date 16:58 2019/7/9
     * @Param projectId groupId
     * @return ProjectResult
     **/
    ProjectResult queryProjectById(String projectId,String groupId);
}
