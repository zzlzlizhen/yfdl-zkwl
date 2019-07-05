package com.remote.modules.group.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.common.enums.DeviceEnum;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.entity.DeviceQuery;
import com.remote.modules.device.service.DeviceService;
import com.remote.modules.group.dao.GroupMapper;
import com.remote.modules.group.entity.GroupEntity;
import com.remote.modules.group.entity.GroupQuery;
import com.remote.modules.group.service.GroupService;
import com.remote.modules.project.entity.ProjectEntity;
import com.remote.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
        Map<String,Integer> all = new HashMap<>();
        Map<String,Integer> alarm = new HashMap<>();
        Map<String,Integer> fault = new HashMap<>();
        PageHelper.startPage(groupQuery.getPageNum(),groupQuery.getPageSize());
        List<GroupEntity> list = groupMapper.queryGroupByName(groupQuery);
        if(CollectionUtils.isNotEmpty(list)){
            List<String> groupIds = list.parallelStream().map(groupEntity -> groupEntity.getGroupId()).collect(Collectors.toCollection(ArrayList::new));
            dataManager(groupIds,all,alarm,fault);
            for (GroupEntity groupEntity : list){
                if(all.get(groupEntity.getGroupId()) != null){
                    groupEntity.setDeviceCount(all.get(groupEntity.getGroupId()));
                }
                if(alarm.get(groupEntity.getGroupId()) != null){
                    groupEntity.setCallPoliceCount(alarm.get(groupEntity.getGroupId()));
                }
                if(fault.get(groupEntity.getGroupId()) != null){
                    groupEntity.setFaultCount(fault.get(groupEntity.getGroupId()));
                }
            }
        }
        PageInfo<GroupEntity> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    public void dataManager(List<String> groupIds,Map<String,Integer> all,Map<String,Integer> alarm,Map<String,Integer> fault){
        List<DeviceEntity> deviceEntities = deviceService.queryDeviceByGroupCount(groupIds,new ArrayList<String>(),DeviceEnum.NORMAL.getCode());//正常
        if(CollectionUtils.isNotEmpty(deviceEntities)){
            for(DeviceEntity deviceEntity : deviceEntities){
                all.put(deviceEntity.getGroupId(),deviceEntity.getCounts());
            }
        }
        List<DeviceEntity> deviceEntities1 = deviceService.queryDeviceByGroupCount(groupIds,new ArrayList<String>(),DeviceEnum.ALARM.getCode());//报警
        if(CollectionUtils.isNotEmpty(deviceEntities1)){
            for(DeviceEntity deviceEntity : deviceEntities1){
                alarm.put(deviceEntity.getGroupId(),deviceEntity.getCounts());
            }
        }
        List<DeviceEntity> deviceEntities2 = deviceService.queryDeviceByGroupCount(groupIds,new ArrayList<String>(),DeviceEnum.FAULT.getCode());//故障
        if(CollectionUtils.isNotEmpty(deviceEntities2)){
            for(DeviceEntity deviceEntity : deviceEntities2){
                fault.put(deviceEntity.getGroupId(),deviceEntity.getCounts());
            }
        }
    }




    @Override
    public String deleteGroup(List<String> groupList,String projectId) throws Exception {
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
            deviceQuery.setPageNum(1);
            deviceQuery.setPageSize(10);
            deviceQuery.setGroupId(groupId);
            PageInfo<DeviceEntity> pageInfo = deviceService.queryDevice(deviceQuery);
            if(CollectionUtils.isNotEmpty(pageInfo.getList())){
                //查询出list 如果有把不可删除的分组名称追加到sb变量中
                GroupEntity groupEntity = groupMapper.queryGroupById(groupId);
                sb.append(groupEntity.getGroupName()).append(",");
            }else{
                groupMapper.deleteGroupById(groupId);
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
    public List<GroupEntity> queryGroupIdNoPage(String projectId,String groupName) {
        Map<String,Integer> all = new HashMap<>();
        GroupQuery groupQuery = new GroupQuery();
        groupQuery.setProjectId(projectId);
        groupQuery.setGroupName(groupName);
        List<GroupEntity> list = groupMapper.queryGroupByName(groupQuery);
        if(CollectionUtils.isNotEmpty(list)){
            List<String> groupIds = list.parallelStream().map(groupEntity -> groupEntity.getGroupId()).collect(Collectors.toCollection(ArrayList::new));
            List<DeviceEntity> deviceEntities = deviceService.queryDeviceByGroupCount(groupIds,new ArrayList<String>(),DeviceEnum.ALL.getCode());//正常
            if(CollectionUtils.isNotEmpty(deviceEntities)){
                for(DeviceEntity deviceEntity : deviceEntities){
                    all.put(deviceEntity.getGroupId(),deviceEntity.getCounts());
                }
            }
            for (GroupEntity groupEntity : list){
                if(all.get(groupEntity.getGroupId()) != null){
                    groupEntity.setDeviceCount(all.get(groupEntity.getGroupId()));
                }
                DeviceQuery deviceQuery = new DeviceQuery();
                deviceQuery.setGroupId(groupEntity.getGroupId());
                //查询出项目下所有设备
                List<DeviceEntity> deviceList = deviceService.queryDeviceNoPage(deviceQuery);
                //定义经度总和
                BigDecimal longitudeSum = new BigDecimal(0);
                //定义纬度总和
                BigDecimal latitudeSum = new BigDecimal(0);
                if(CollectionUtils.isNotEmpty(deviceList)){
                    //如果设备太多，只取前100条
                    int size = deviceList.size() > 99 ? 99 : deviceList.size();
                    for(int i = 0; i < size;i++ ){
                        if(StringUtils.isNotEmpty(deviceList.get(i).getLongitude())){
                            BigDecimal decimal = new BigDecimal(deviceList.get(i).getLongitude()).setScale(4,BigDecimal.ROUND_HALF_DOWN);
                            longitudeSum = longitudeSum.add(decimal);
                        }
                        if(StringUtils.isNotEmpty(deviceList.get(i).getLatitude())){
                            BigDecimal decimal = new BigDecimal(deviceList.get(i).getLatitude()).setScale(4,BigDecimal.ROUND_HALF_DOWN);
                            latitudeSum = latitudeSum.add(decimal);
                        }
                    }
                    //保存项目经度和纬度
                    if(longitudeSum.compareTo(new BigDecimal(0)) == 1){ //判断是否大于0
                        groupEntity.setLongitude(longitudeSum.divide(BigDecimal.valueOf(size),4,BigDecimal.ROUND_HALF_UP).toString());
                    }else{
                        groupEntity.setLongitude(latitudeSum.toString());
                    }
                    if(latitudeSum.compareTo(new BigDecimal(0)) == 1){
                        groupEntity.setLatitude(latitudeSum.divide(BigDecimal.valueOf(size),4,BigDecimal.ROUND_HALF_UP).toString());
                    }else{
                        groupEntity.setLatitude(latitudeSum.toString());
                    }

                }

            }
        }
        return list;
    }



    @Override
    public boolean updateGroup(GroupEntity groupEntity) {
        return groupMapper.updateGroup(groupEntity) > 0 ? true : false;
    }


}
