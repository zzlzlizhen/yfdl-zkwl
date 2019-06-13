package com.remote.modules.project.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
    public List<ProjectEntity> queryProjectNoPage(Long userId) {
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
            }
            return list;
        }
        return null;
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
