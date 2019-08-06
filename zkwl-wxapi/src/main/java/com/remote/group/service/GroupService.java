package com.remote.group.service;

import com.github.pagehelper.PageInfo;
import com.remote.group.entity.GroupEntity;
import com.remote.group.entity.GroupQuery;

import java.util.List;

public interface GroupService {
    /*
     * @Author zhangwenping
     * @Description 添加分组信息
     * @Date 9:38 2019/6/4
     * @Param group
     * @return boolean
     **/
    boolean addGroup(GroupEntity group);
    /*
     * @Author zhangwenping
     * @Description 查询当前用户和所有下级用户分组
     * @Date 9:53 2019/6/4
     * @Param groupQuery
     * @return PageInfo<GroupEntity>
     **/
    PageInfo<GroupEntity> queryGroupByName(GroupQuery groupQuery);
    /*
     * @Author zhangwenping
     * @Description 删除分组
     * @Date 10:50 2019/6/4
     * @Param groupList,projectId
     * @return String
     **/
    String deleteGroup(List<String> groupList, String projectId);

    /*
     * @Author zhangwenping
     * @Description 查询项目下分组名称是否重复
     * @Date 10:57 2019/6/4
     * @Param projectId,groupName
     * @return boolean
     **/
    boolean queryByName(String projectId, String groupName);
    /*
     * @Author zhangwenping
     * @Description 查询用户下所有分组  不分页
     * @Date 9:10 2019/6/5
     * @Param userId,userName,projectId
     * @return List<GroupEntity>
     **/
    List<GroupEntity> queryGroupNoPage(Long userId, String userName, String projectId);
    /*
     * @Author zhangwenping
     * @Description 设备管理查询分组  不分页
     * @Date 15:14 2019/6/6
     * @Param projectId groupName
     * @return List<GroupEntity>
     **/
    List<GroupEntity> queryGroupIdNoPage(String projectId, String groupName);
    /*
     * @Author zhangwenping
     * @Description 修改分组信息
     * @Date 11:02 2019/6/11
     * @Param groupEntity
     * @return boolean
     **/
    boolean updateGroup(GroupEntity groupEntity);
    /*
     * @Author zhangwenping
     * @Description 根据id查看分组详情
     * @Date 10:17 2019/8/5
     * @Param groupId
     * @return GroupEntity
     **/
    GroupEntity queryGroupById(String groupId);
}
