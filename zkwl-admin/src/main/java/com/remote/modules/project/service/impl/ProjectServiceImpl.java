package com.remote.modules.project.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.common.enums.DeviceEnum;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.entity.DeviceQuery;
import com.remote.modules.device.service.DeviceService;
import com.remote.modules.group.entity.GroupEntity;
import com.remote.modules.group.service.GroupService;
import com.remote.modules.project.dao.ProjectMapper;
import com.remote.modules.project.entity.ProjectEntity;
import com.remote.modules.project.entity.ProjectQuery;
import com.remote.modules.project.service.ProjectService;
import com.remote.modules.sys.entity.SysUserEntity;
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
 * @Date 2019/5/31 14:14
 * @Version 1.0
 **/

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private DeviceService deviceService;


    @Override
    public boolean addProject(ProjectEntity projectEntity) {
        return projectMapper.insert(projectEntity) > 0 ? true : false;
    }

    @Override
    public PageInfo<ProjectEntity> queryProjectByUserIds(ProjectQuery projectQuery) {
        List<SysUserEntity> userList = sysUserService.queryAllLevel(projectQuery.getUserId());
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

    @Override
    public boolean delProject(List<String> projectList,Long updateUser) {
        return projectMapper.delProject(projectList,updateUser,new Date()) > 0 ? true : false;
    }

    @Override
    public boolean updateProject(ProjectEntity projectEntity) {
        return projectMapper.updateProjectById(projectEntity) > 0 ? true : false;
    }

    @Override
    public List<ProjectEntity> queryProjectNoPage(Long userId,Integer deviceStatus) {
        List<SysUserEntity> userList = sysUserService.queryAllLevel(userId);
        if(CollectionUtils.isNotEmpty(userList)){
            List<Long> transform = userList.parallelStream().map(sysUserEntity -> sysUserEntity.getUserId()).collect(Collectors.toCollection(ArrayList::new));
            ProjectQuery projectQuery = new ProjectQuery();
            projectQuery.setUserIds(transform);
            List<ProjectEntity> list = projectMapper.queryProjectByUserIds(projectQuery);
            if(CollectionUtils.isNotEmpty(list)){
                for(ProjectEntity projectEntity : list){
                    DeviceQuery deviceQuery = new DeviceQuery();
                    deviceQuery.setProjectId(projectEntity.getProjectId());
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
                    }
                    //保存项目经度和纬度
                    projectEntity.setLongitude(longitudeSum.toString());
                    projectEntity.setLatitude(latitudeSum.toString());
                }
                //统计总装机/故障/报警数量
                statisticsData(list,deviceStatus);
            }
            return list;
        }
        return null;
    }


    private void statisticsData(List<ProjectEntity> list,Integer deviceStaus) {
        //总装机数量
        Map<String,Integer> all = new HashMap<>();
        List<String> projectIds = list.parallelStream().map(projectEntity -> projectEntity.getProjectId()).collect(Collectors.toCollection(ArrayList::new));

        List<DeviceEntity> deviceEntities1 = deviceService.queryDeviceByGroupCount(new ArrayList<String>(),projectIds,deviceStaus);
        if(CollectionUtils.isNotEmpty(deviceEntities1)){
            for(DeviceEntity deviceEntity : deviceEntities1){
                all.put(deviceEntity.getProjectId(),deviceEntity.getCounts());
            }
        }
        for (ProjectEntity projectEntity : list){
            if(all.get(projectEntity.getProjectId()) != null) {
                projectEntity.setSumCount(all.get(projectEntity.getProjectId()));
            }
            if(projectEntity.getSumCount() == null){
                projectEntity.setSumCount(0);
            }
        }
    }

    private void statisticsData(List<ProjectEntity> list) {
        //总装机数量
        Map<String,Integer> all = new HashMap<>();
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
    public ProjectEntity queryProjectMap(String projectId) {
        ProjectEntity projectEntity = projectMapper.queryProjectMap(projectId);
        if(projectEntity != null){
            //定义经度总和
            BigDecimal longitudeSum = new BigDecimal(0);
            //定义纬度总和
            BigDecimal latitudeSum = new BigDecimal(0);
            DeviceQuery deviceQuery = new DeviceQuery();
            deviceQuery.setProjectId(projectEntity.getProjectId());
            //查询出项目下所有设备
            List<DeviceEntity> deviceList = deviceService.queryDeviceNoPage(deviceQuery);
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
            }
            //保存项目经度和纬度
            projectEntity.setLongitude(longitudeSum.toString());
            projectEntity.setLatitude(latitudeSum.toString());
        }
        return projectEntity;
    }
}
