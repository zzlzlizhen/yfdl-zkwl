package com.remote.project.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.service.DeviceService;
import com.remote.enums.DeviceEnum;
import com.remote.project.dao.ProjectMapper;
import com.remote.project.entity.ProjectEntity;
import com.remote.project.entity.ProjectQuery;
import com.remote.project.service.ProjectService;
import com.remote.sys.entity.SysUserEntity;
import com.remote.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author zhangwenping
 * @Date 2019/5/31 14:14
 * @Version 1.0
 **/

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SysUserService sysUserService;


    @Override
    public boolean addProject(ProjectEntity project) {
        return projectMapper.insert(project) > 0 ? true : false;
    }

    @Override
    public PageInfo<ProjectEntity> queryProjectByUserIds(ProjectQuery projectQuery) {
        SysUserEntity sysUserEntitys = new SysUserEntity();
        sysUserEntitys.setUserId(projectQuery.getUserId());
        sysUserEntitys.setAllParentId(projectQuery.getParentId());
        List<SysUserEntity> userList = sysUserService.queryAllChild(sysUserEntitys);
        if(CollectionUtils.isNotEmpty(userList)){
            List<Long> transform = userList.parallelStream().map(sysUserEntity -> sysUserEntity.getUserId()).collect(Collectors.toCollection(ArrayList::new));
            projectQuery.setUserIds(transform);
            PageHelper.startPage(projectQuery.getPageNum(),projectQuery.getPageSize());
            List<ProjectEntity> list = projectMapper.queryProjectByUserIds(projectQuery);
            //统计总装机/故障/报警数量
            statisticsData(list);
            PageInfo<ProjectEntity> proPage = new PageInfo<>(list);
            return proPage;
        }
        return null;
    }

    private void statisticsData(List<ProjectEntity> list) {
        //故障数量
        Map<String,Integer> alarm = new HashMap<>();
        //报警数量
        Map<String,Integer> fault = new HashMap<>();
        //正常数量
        Map<String,Integer> normal = new HashMap<>();
        //离线数量
        Map<String,Integer> offline = new HashMap<>();
        List<String> projectIds = list.parallelStream().map(projectEntity -> projectEntity.getProjectId()).collect(Collectors.toCollection(ArrayList::new));

        List<DeviceEntity> deviceEntities1 = deviceService.queryDeviceByGroupCount(new ArrayList<String>(),projectIds,DeviceEnum.ALARM.getCode());//报警
        if(CollectionUtils.isNotEmpty(deviceEntities1)){
            for(DeviceEntity deviceEntity : deviceEntities1){
                alarm.put(deviceEntity.getProjectId(),deviceEntity.getCounts());
            }
        }
        List<DeviceEntity> deviceEntities2 = deviceService.queryDeviceByGroupCount(new ArrayList<String>(),projectIds,DeviceEnum.FAULT.getCode());//故障
        if(CollectionUtils.isNotEmpty(deviceEntities2)){
            for(DeviceEntity deviceEntity : deviceEntities2){
                fault.put(deviceEntity.getProjectId(),deviceEntity.getCounts());
            }
        }
        List<DeviceEntity> deviceEntities3 = deviceService.queryDeviceByGroupCount(new ArrayList<String>(),projectIds,DeviceEnum.NORMAL.getCode());//正常
        if(CollectionUtils.isNotEmpty(deviceEntities3)){
            for(DeviceEntity deviceEntity : deviceEntities3){
                normal.put(deviceEntity.getProjectId(),deviceEntity.getCounts());
            }
        }
        List<DeviceEntity> deviceEntities4 = deviceService.queryDeviceByGroupCount(new ArrayList<String>(),projectIds,DeviceEnum.OFFLINE.getCode());//离线
        if(CollectionUtils.isNotEmpty(deviceEntities4)){
            for(DeviceEntity deviceEntity : deviceEntities4){
                offline.put(deviceEntity.getProjectId(),deviceEntity.getCounts());
            }
        }
        for (ProjectEntity projectEntity : list){
            Integer sumCount = 0;
            if(alarm.get(projectEntity.getProjectId()) != null) {
                projectEntity.setCallPoliceCount(alarm.get(projectEntity.getProjectId()));
                sumCount += alarm.get(projectEntity.getProjectId());
            }
            if(fault.get(projectEntity.getProjectId()) != null) {
                projectEntity.setFaultCount(fault.get(projectEntity.getProjectId()));
                sumCount += fault.get(projectEntity.getProjectId());
            }
            if(normal.get(projectEntity.getProjectId()) != null) {
                projectEntity.setNormalCount(normal.get(projectEntity.getProjectId()));
                sumCount += normal.get(projectEntity.getProjectId());
            }
            if(offline.get(projectEntity.getProjectId()) != null) {
                projectEntity.setOfflineCount(offline.get(projectEntity.getProjectId()));
                sumCount += offline.get(projectEntity.getProjectId());
            }
            projectEntity.setGatewayCount(nullData(projectEntity.getGatewayCount()));
            projectEntity.setCallPoliceCount(nullData(projectEntity.getCallPoliceCount()));
            projectEntity.setFaultCount(nullData(projectEntity.getFaultCount()));
            projectEntity.setNormalCount(nullData(projectEntity.getNormalCount()));
            projectEntity.setOfflineCount(nullData(projectEntity.getOfflineCount()));
            projectEntity.setSumCount(sumCount);
        }
    }

    public Integer nullData(Integer data){
        if(data == null){
            return 0;
        }
        return data;
    }


    @Override
    public boolean delProject(List<String> projectList,Long updateUser) {
        return projectMapper.delProject(projectList,updateUser,new Date()) > 0 ? true : false;
    }


    @Override
    public boolean updateProject(ProjectEntity projectEntity) {
        return projectMapper.updateProjectById(projectEntity) > 0 ? true : false;
    }
}
