
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
}
