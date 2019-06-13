package com.remote.modules.group.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.entity.DeviceQuery;
import com.remote.modules.device.service.DeviceService;
import com.remote.modules.group.dao.GroupMapper;
import com.remote.modules.group.entity.GroupEntity;
import com.remote.modules.group.entity.GroupQuery;
import com.remote.modules.group.service.GroupService;
import com.remote.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author zhangwenping
 * @Date 2019/6/4 9:25
 * @Version 1.0
 **/
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DeviceService deviceService;

    @Override
    public boolean addGroup(GroupEntity group) {
        return groupMapper.insert(group) > 0 ? true : false;
    }

    @Override
    public PageInfo<GroupEntity> queryGroupByName(GroupQuery groupQuery) {
        PageHelper.startPage(groupQuery.getPageNum(),groupQuery.getPageSize());
        List<GroupEntity> list = groupMapper.queryGroupByName(groupQuery);
        PageInfo<GroupEntity> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public String deleteGroup(List<String> groupList,String projectId) {
        StringBuffer sb = new StringBuffer();
        //查询所有分组信息
        GroupQuery groupQuery = new GroupQuery();
        groupQuery.setProjectId(projectId);
        List<GroupEntity> list = groupMapper.queryGroupByName(groupQuery);
        Map<String,String> map = new HashMap<>();
        if(CollectionUtils.isNotEmpty(list)){
            for (GroupEntity groupEntity : list){
                map.put(groupEntity.getGroupId(),groupEntity.getGroupName());
            }
        }
        for(String groupId : groupList){
            //根据跟组id 查询下面设备
            DeviceQuery deviceQuery = new DeviceQuery();
            deviceQuery.setGroupId(groupId);
            PageInfo<DeviceEntity> pageInfo = deviceService.queryDevice(deviceQuery);
            if(CollectionUtils.isNotEmpty(pageInfo.getList())){
                //查询出list 如果有把不可删除的分组名称追加到sb变量中
                GroupEntity groupEntity = groupMapper.queryGroupById(groupId);
                sb.append(groupEntity.getGroupName());
            }
        }
        return sb.toString();
    }

    @Override
    public boolean queryByName(String projectId,String groupName) {
        return groupMapper.queryByName(projectId,groupName) > 0 ? true : false;
    }

    @Override
    public List<GroupEntity> queryGroupNoPage(Long userId,String userName,String projectId) {
        GroupQuery groupQuery = new GroupQuery();
        groupQuery.setProjectId(projectId);
        List<GroupEntity> list = groupMapper.queryGroupByName(groupQuery);
        //如果有分组，则返回
        if(CollectionUtils.isNotEmpty(list)){
            return groupMapper.queryGroupByName(groupQuery);
        }
        //如果没有分组，新建一个默认分组 返回
        GroupEntity groupEntity = new GroupEntity(UUID.randomUUID().toString(),"默认分组");
        groupEntity.setProjectId(projectId);
        groupEntity.setCreateUser(userId);
        groupEntity.setCreateName(userName);
        groupEntity.setCreateTime(new Date());
        int insert = groupMapper.insert(groupEntity);
        if(insert > 0){
            List<GroupEntity> list1 = groupMapper.queryGroupByName(groupQuery);
            return groupMapper.queryGroupByName(groupQuery);
        }
        return new ArrayList<GroupEntity>();
    }

    @Override
    public List<GroupEntity> queryGroupIdNoPage(String projectId) {
        GroupQuery groupQuery = new GroupQuery();
        groupQuery.setProjectId(projectId);
        return groupMapper.queryGroupByName(groupQuery);
    }

    @Override
    public boolean updateGroup(GroupEntity groupEntity) {
        return groupMapper.updateGroup(groupEntity) > 0 ? true : false;
    }

}
