
package com.remote.project.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.project.entity.ProjectEntity;
import com.remote.project.entity.ProjectQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


@Mapper
public interface ProjectMapper extends BaseMapper<ProjectEntity> {

    /*
     * @Author zhangwenping
     * @Description 查询当前用户下所有项目 （包括下级单位的项目）
     * @Date 17:14 2019/5/31
     * @Param userIds
     * @return List<ProjectEntity>
     **/
    List<ProjectEntity> queryProjectByUserIds(@Param("projectQuery") ProjectQuery projectQuery);
    /*
     * @Author zhangwenping
     * @Description 删除项目
     * @Date 13:34 2019/6/3
     * @Param projectCodes
     * @return int
     **/
    int delProject(@Param("projectList")List<String> projectList, @Param("updateUser")Long updateUser, @Param("updateTime")Date updateTime);

    /*
     * @Author zhangwenping
     * @Description 根据项目id修改项目信息
     * @Date 15:13 2019/6/3
     * @Param projectentity
     * @return int
     **/
    int updateProjectById(ProjectEntity projectEntity);
    /*
     * @Author zhangwenping
     * @Description 根据项目id查询项目详情
     * @Date 13:47 2019/7/29
     * @Param projectId
     * @return ProjectEntity
     **/
    ProjectEntity queryProjectMap(@Param("projectId") String projectId);

    /**
     * 通过项目ids获取使用者用户ids
     * */
    Long queryExclusiveId(@Param("projectId")String projectId);
    /**
     * 通过项目ids获取使用者用户ids
     * */
    List<Long> queryExclusiveIds(@Param("projectIds")List<String> projectIds);

}
