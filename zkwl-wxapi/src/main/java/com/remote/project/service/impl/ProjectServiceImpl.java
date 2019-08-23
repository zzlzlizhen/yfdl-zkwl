package com.remote.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.entity.DeviceQuery;
import com.remote.device.service.DeviceService;
import com.remote.enums.DeviceEnum;
import com.remote.project.dao.ProjectMapper;
import com.remote.project.entity.ProjectEntity;
import com.remote.project.entity.ProjectQuery;
import com.remote.project.service.ProjectService;
import com.remote.sys.entity.SysUserEntity;
import com.remote.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SysUserService sysUserService;


    @Override
    public boolean addProject(ProjectEntity project) {
        logger.info("添加项目信息："+JSONObject.toJSONString(project));
        return projectMapper.insert(project) > 0 ? true : false;
    }

    @Override
    public PageInfo<ProjectEntity> queryProjectByUserIds(ProjectQuery projectQuery) {
        logger.info("查询项目信息："+JSONObject.toJSONString(projectQuery));
        SysUserEntity sysUserEntitys = new SysUserEntity();
        sysUserEntitys.setUserId(projectQuery.getUserId());
        sysUserEntitys.setAllParentId(projectQuery.getParentId());
        List<SysUserEntity> userList = sysUserService.queryAllChild(sysUserEntitys);
        if(CollectionUtils.isNotEmpty(userList)){
            List<Long> transform = userList.parallelStream().map(sysUserEntity -> sysUserEntity.getUserId()).collect(Collectors.toCollection(ArrayList::new));
            projectQuery.setUserIds(transform);
            PageHelper.startPage(projectQuery.getPageNum(),projectQuery.getPageSize());
            List<ProjectEntity> list = projectMapper.queryProjectByUserIds(projectQuery);

            Map<Long,String> userMap = new HashMap<>();
            List<SysUserEntity> userLists = sysUserService.queryUserByUserIds(transform);
            if(CollectionUtils.isNotEmpty(userLists)){
                for (SysUserEntity sysUserEntity : userLists){
                    userMap.put(sysUserEntity.getUserId(),sysUserEntity.getRealName());
                }
            }
            for (ProjectEntity projectEntity : list){
                if(projectEntity.getExclusiveUser() != null){
                    projectEntity.setExclusiveUserName(userMap.get(projectEntity.getExclusiveUser()));
                }
            }
            //统计总装机/故障/报警数量
            statisticsData(list);
            //封装经纬度
            toDataLongitude(list);
            PageInfo<ProjectEntity> proPage = new PageInfo<>(list);
            return proPage;
        }
        return null;
    }

    private void toDataLongitude(List<ProjectEntity> list) {
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
                    //保存项目经度和纬度
                    if(longitudeSum.compareTo(new BigDecimal(0)) == 1){ //判断是否大于0
                        projectEntity.setLongitude(longitudeSum.divide(BigDecimal.valueOf(size),4,BigDecimal.ROUND_HALF_UP).toString());
                    }else{
                        projectEntity.setLongitude(latitudeSum.toString());
                    }
                    if(latitudeSum.compareTo(new BigDecimal(0)) == 1){
                        projectEntity.setLatitude(latitudeSum.divide(BigDecimal.valueOf(size),4,BigDecimal.ROUND_HALF_UP).toString());
                    }else{
                        projectEntity.setLatitude(latitudeSum.toString());
                    }

                }


            }
        }
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

        List<DeviceEntity> deviceEntities1 = deviceService.queryDeviceByProjectCount(projectIds,DeviceEnum.ALARM.getCode());//报警
        if(CollectionUtils.isNotEmpty(deviceEntities1)){
            for(DeviceEntity deviceEntity : deviceEntities1){
                alarm.put(deviceEntity.getProjectId(),deviceEntity.getCounts());
            }
        }
        List<DeviceEntity> deviceEntities2 = deviceService.queryDeviceByProjectCount(projectIds,DeviceEnum.FAULT.getCode());//故障
        if(CollectionUtils.isNotEmpty(deviceEntities2)){
            for(DeviceEntity deviceEntity : deviceEntities2){
                fault.put(deviceEntity.getProjectId(),deviceEntity.getCounts());
            }
        }
        List<DeviceEntity> deviceEntities3 = deviceService.queryDeviceByProjectCount(projectIds,DeviceEnum.NORMAL.getCode());//正常
        if(CollectionUtils.isNotEmpty(deviceEntities3)){
            for(DeviceEntity deviceEntity : deviceEntities3){
                normal.put(deviceEntity.getProjectId(),deviceEntity.getCounts());
            }
        }
        List<DeviceEntity> deviceEntities4 = deviceService.queryDeviceByProjectCount(projectIds,DeviceEnum.OFFLINE.getCode());//离线
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
        logger.info("修改项目信息："+JSONObject.toJSONString(projectEntity));
        return projectMapper.updateProjectById(projectEntity) > 0 ? true : false;
    }
    @Override
    public int queryProjectByUserCount(Long userId) {
        List<SysUserEntity> userList = sysUserService.queryAllLevel(userId);
        ProjectQuery projectQuery = new ProjectQuery();
        if(CollectionUtils.isNotEmpty(userList)){
            List<Long> transform = userList.parallelStream().map(sysUserEntity -> sysUserEntity.getUserId()).collect(Collectors.toCollection(ArrayList::new));
            projectQuery.setUserIds(transform);
            List<ProjectEntity> list = projectMapper.queryProjectByUserIds(projectQuery);
            return list.size();
        }
        return 0;
    }

    @Override
    public List<Long> queryExclusiveIds(List<String> projectIds) {
        return projectMapper.queryExclusiveIds(projectIds);
    }

    @Override
    public ProjectEntity queryProjectMap(String projectId) {
        return projectMapper.queryProjectMap(projectId);
    }
    @Override
    public Long queryExclusiveId(String projectId) {
        return this.projectMapper.queryExclusiveId(projectId);
    }
}
