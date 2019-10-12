package com.remote.modules.group.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.modules.group.entity.GroupEntity;
import com.remote.modules.group.entity.GroupQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupMapper extends BaseMapper<GroupEntity> {
    /*
     * @Author zhangwenping
     * @Description 查询用户下所有分组
     * @Date 10:00 2019/6/4
     * @Param groupQuery
     * @return List<GroupEntity>
     **/
    List<GroupEntity> queryGroupByName(@Param("groupQuery") GroupQuery groupQuery);
    /*
     * @Author zhangwenping
     * @Description 查询项目下分组名称是否重复
     * @Date 10:58 2019/6/4
     * @Param projectId,groupName
     * @return int
     **/
    int queryByName(@Param("projectId") String projectId,@Param("groupName") String groupName);
    /*
     * @Author zhangwenping
     * @Description 根据名称和项目查询分组信息
     * @Date 16:26 2019/10/10
     * @Param  projectId groupName
     * @return GroupEntity
     **/
    GroupEntity selectGroupByName(@Param("projectId") String projectId,@Param("groupName") String groupName);

    /*
     * @Author zhangwenping
     * @Description 根据id查询
     * @Date 14:43 2019/6/5
     * @Param groupId
     * @return GroupEntity
     **/
    GroupEntity queryGroupById(String groupId);
    /*
     * @Author zhangwenping
     * @Description 修改分组信息
     * @Date 11:03 2019/6/11
     * @Param groupEntity
     * @return boolean
     **/
    int updateGroup(@Param("groupEntity")GroupEntity groupEntity);
    /*
     * @Author zhangwenping
     * @Description 删除分组信息
     * @Date 14:34 2019/6/18
     * @Param groupId
     * @return int
     **/
    int deleteGroupById(@Param("groupId")String groupId);
    /*
     * @Author zhangwenping
     * @Description 根据ids 查询分组信息
     * @Date 12:11 2019/8/7
     * @Param groupIds
     * @return  List<GroupEntity>
     **/
    List<GroupEntity> queryGroupByIds(@Param("groupIds")List<String> groupIds);
}
