package com.remote.modules.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.common.enums.DeviceEnum;
import com.remote.common.enums.RunStatusEnum;
import com.remote.common.utils.ValidateUtils;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.entity.DeviceQuery;
import com.remote.modules.device.service.DeviceService;
import com.remote.modules.group.entity.GroupEntity;
import com.remote.modules.group.service.GroupService;
import com.remote.modules.group.service.impl.GroupServiceImpl;
import com.remote.modules.project.dao.ProjectMapper;
import com.remote.modules.project.entity.ProjectEntity;
import com.remote.modules.project.entity.ProjectQuery;
import com.remote.modules.project.entity.ProjectResult;
import com.remote.modules.project.service.ProjectService;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
    private SysUserService sysUserService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private DeviceService deviceService;


    @Override
    public boolean addProject(ProjectEntity projectEntity) {
        logger.info("添加项目信息："+JSONObject.toJSONString(projectEntity));
        return projectMapper.insert(projectEntity) > 0 ? true : false;
    }

    @Override
    public PageInfo<ProjectEntity> queryProjectByUserIds(ProjectQuery projectQuery) {
        logger.info("查询项目信息："+JSONObject.toJSONString(projectQuery));
        List<SysUserEntity> userList = sysUserService.queryAllLevel(projectQuery.getUserId());
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
                    //经度list
                    List<Double> longitudeList = new ArrayList<>();
                    //纬度list
                    List<Double> latitudeList = new ArrayList<>();
                    //如果设备太多，只取前100条
                    int size = deviceList.size() > 99 ? 99 : deviceList.size();
                    //经度临时list
                    List<Double> longitudeTempList = new ArrayList<>();
                    //纬度临时list
                    List<Double> latitudeTempList = new ArrayList<>();
                    for(int i = 0; i < size;i++ ){
                        if(StringUtils.isNotEmpty(deviceList.get(i).getLongitude())){
                            double v = Double.parseDouble(deviceList.get(i).getLongitude());
                            if(v != 0.0){
                                longitudeTempList.add(v);
                            }
                        }
                        if(StringUtils.isNotEmpty(deviceList.get(i).getLatitude())){
                            double v = Double.parseDouble(deviceList.get(i).getLatitude());
                            if(v != 0.0){
                                latitudeTempList.add(v);
                            }
                        }
                    }
                    if(size > 5){
                       Collections.sort(longitudeTempList);
                       Collections.sort(latitudeTempList);

                        for (int i = 1;i<longitudeTempList.size() - 1;i++){
                            longitudeList.add(longitudeTempList.get(i));
                        }
                        for (int i = 1;i<latitudeTempList.size() - 1;i++){
                            latitudeList.add(latitudeTempList.get(i));
                        }
                    }else{
                        longitudeList.addAll(longitudeTempList);
                        latitudeList.addAll(latitudeTempList);
                    }


                    for (Double temp : longitudeList){
                        BigDecimal decimal = new BigDecimal(temp).setScale(4,BigDecimal.ROUND_HALF_DOWN);
                        longitudeSum = longitudeSum.add(decimal);
                    }

                    for (Double temp : latitudeList){
                        BigDecimal decimal = new BigDecimal(temp).setScale(4,BigDecimal.ROUND_HALF_DOWN);
                        latitudeSum = latitudeSum.add(decimal);
                    }

                    //保存项目经度和纬度
                    if(longitudeSum.compareTo(new BigDecimal(0)) == 1){ //判断是否大于0
                        projectEntity.setLongitude(longitudeSum.divide(BigDecimal.valueOf(longitudeList.size()),4,BigDecimal.ROUND_HALF_UP).toString());
                    }else{
                        projectEntity.setLongitude(latitudeSum.toString());
                    }
                    if(latitudeSum.compareTo(new BigDecimal(0)) == 1){
                        projectEntity.setLatitude(latitudeSum.divide(BigDecimal.valueOf(longitudeList.size()),4,BigDecimal.ROUND_HALF_UP).toString());
                    }else{
                        projectEntity.setLatitude(latitudeSum.toString());
                    }

                }else{
                    projectEntity.setLongitude(longitudeSum.toString());
                    projectEntity.setLatitude(latitudeSum.toString());
                }


            }
        }
    }

    @Override
    public boolean delProject(List<String> projectList,Long updateUser) {
        return projectMapper.delProject(projectList,updateUser,new Date()) > 0 ? true : false;
    }





    @Override
    public boolean updateProject(ProjectEntity projectEntity) throws Exception {
        logger.info("修改项目信息："+JSONObject.toJSONString(projectEntity));
        ValidateUtils.validate(projectEntity,Arrays.asList("projectId"));
        int i = projectMapper.updateProjectById(projectEntity);
        if(i > 0){
            DeviceQuery deviceQuery = new DeviceQuery();
            deviceQuery.setProjectId(projectEntity.getProjectId());
            List<DeviceEntity> deviceEntities = deviceService.queryDeviceNoPage(deviceQuery);
            if(CollectionUtils.isNotEmpty(deviceEntities)){
                List<String> deviceIds = deviceEntities.parallelStream().map(deviceEntity -> deviceEntity.getDeviceId()).collect(Collectors.toCollection(ArrayList::new));
                Long exclusiveUser = projectEntity.getExclusiveUser();
                deviceQuery.setUpdateTime(new Date());
                deviceQuery.setCreateUser(exclusiveUser);
                deviceQuery.setUpdateUser(projectEntity.getCreateUser());
                deviceQuery.setDeviceList(deviceIds);
                return deviceService.updateUserDevice(deviceQuery) > 0 ? true : false;
            }
            return i > 0 ? true : false;
        }
        return false;
    }

    @Override
    public List<ProjectEntity> queryProjectNoPage(Long userId,Integer deviceStatus) {
        List<SysUserEntity> userList = sysUserService.queryAllLevel(userId);
        if(CollectionUtils.isNotEmpty(userList)){
            List<Long> transform = userList.parallelStream().map(sysUserEntity -> sysUserEntity.getUserId()).collect(Collectors.toCollection(ArrayList::new));
            ProjectQuery projectQuery = new ProjectQuery();
            projectQuery.setUserIds(transform);
            List<ProjectEntity> list = projectMapper.queryProjectByUserIds(projectQuery);
            toDataLongitude(list);
            //统计总装机/故障/报警数量
            statisticsData(list,deviceStatus);
            return list;
        }
        return null;
    }


    private void statisticsData(List<ProjectEntity> list,Integer deviceStaus) {
        //总装机数量
        Map<String,Integer> all = new HashMap<>();
        List<String> projectIds = list.parallelStream().map(projectEntity -> projectEntity.getProjectId()).collect(Collectors.toCollection(ArrayList::new));

        List<DeviceEntity> deviceEntities1 = deviceService.queryDeviceByProjectCount(projectIds,deviceStaus);
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

        List<DeviceEntity> deviceEntities5 = deviceService.queryDeviceByProjectCount(projectIds,DeviceEnum.ALL.getCode());//全部
        if(CollectionUtils.isNotEmpty(deviceEntities5)){
            for(DeviceEntity deviceEntity : deviceEntities5){
                all.put(deviceEntity.getProjectId(),deviceEntity.getCounts());
            }
        }
        for (ProjectEntity projectEntity : list){
            if(alarm.get(projectEntity.getProjectId()) != null) {
                projectEntity.setCallPoliceCount(alarm.get(projectEntity.getProjectId()));
            }
            if(fault.get(projectEntity.getProjectId()) != null) {
                projectEntity.setFaultCount(fault.get(projectEntity.getProjectId()));
            }
            if(normal.get(projectEntity.getProjectId()) != null) {
                projectEntity.setNormalCount(normal.get(projectEntity.getProjectId()));
            }
            if(offline.get(projectEntity.getProjectId()) != null) {
                projectEntity.setOfflineCount(offline.get(projectEntity.getProjectId()));
            }
            if(all.get(projectEntity.getProjectId()) != null) {
                projectEntity.setSumCount(all.get(projectEntity.getProjectId()));
            }else{
                projectEntity.setSumCount(0);
            }
            projectEntity.setGatewayCount(nullData(projectEntity.getGatewayCount()));
            projectEntity.setCallPoliceCount(nullData(projectEntity.getCallPoliceCount()));
            projectEntity.setFaultCount(nullData(projectEntity.getFaultCount()));
            projectEntity.setNormalCount(nullData(projectEntity.getNormalCount()));
            projectEntity.setOfflineCount(nullData(projectEntity.getOfflineCount()));

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

    /**
     * 根据所属用户的父ids查询每个用户的项目数量
     * */
    @Override
    public List<Integer> queryProjectCountByAllParentIds(List<Long> userIds){
        List<Integer> projectCounts = new ArrayList<Integer>();
        if(CollectionUtils.isNotEmpty(userIds)||userIds.size()>0){
            ProjectQuery projectQuery = new ProjectQuery();
            for(Long userId : userIds){
                /**
                 * 查出每个用户下的所有 用户
                 * */
                List<SysUserEntity> userList = sysUserService.queryAllLevel(userId);
                if(CollectionUtils.isNotEmpty(userList)||userList.size()>0){
                    List<Long> transform = userList.parallelStream().map(sysUserEntity -> sysUserEntity.getUserId()).collect(Collectors.toCollection(ArrayList::new));
                    projectQuery.setUserIds(transform);
                    List<ProjectEntity> list = projectMapper.queryProjectByUserIds(projectQuery);
                    projectCounts.add(list.size());
                }
            }
        }

        return projectCounts;
    }

    @Override
    public List<Long> queryExclusiveIds(List<String> projectIds) {
        return projectMapper.queryExclusiveIds(projectIds);
    }

    @Override
    public ProjectResult queryProjectById(String projectId,String groupId) {
        List<Map<String,Integer>> deviceMapList = new ArrayList<>();
        List<Map<String,BigDecimal>> deviceScaleList = new ArrayList<>();
        ProjectResult projectResult = new ProjectResult();
        Map<String,Integer> deviceMap = new HashMap<>();
        Map<String,BigDecimal> deviceScale = new HashMap<>();
        Integer sumCount = 0;
        deviceMap.put(RunStatusEnum.NORAML.getName(),0);
        deviceMap.put(RunStatusEnum.WARNING.getName(),0);
        deviceMap.put(RunStatusEnum.FAULT.getName(),0);
        deviceMap.put(RunStatusEnum.OFFLINE.getName(),0);
        deviceMap.put(RunStatusEnum.UPGRADE.getName(),0);
        List<DeviceEntity> deviceList = deviceService.queryRunStateCount(projectId, groupId);
        if(CollectionUtils.isNotEmpty(deviceList)){
            for(DeviceEntity deviceEntity : deviceList){
                sumCount += deviceEntity.getCounts();
                if(deviceEntity.getRunState() == RunStatusEnum.NORAML.getCode()){
                    deviceMap.put(RunStatusEnum.NORAML.getName(),deviceEntity.getCounts());
                }
                if(deviceEntity.getRunState() == RunStatusEnum.WARNING.getCode()){
                    deviceMap.put(RunStatusEnum.WARNING.getName(),deviceEntity.getCounts());
                }
                if(deviceEntity.getRunState() == RunStatusEnum.FAULT.getCode()){
                    deviceMap.put(RunStatusEnum.FAULT.getName(),deviceEntity.getCounts());
                }
                if(deviceEntity.getRunState() == RunStatusEnum.OFFLINE.getCode()){
                    deviceMap.put(RunStatusEnum.OFFLINE.getName(),deviceEntity.getCounts());
                }
                if(deviceEntity.getRunState() == RunStatusEnum.UPGRADE.getCode()){
                    deviceMap.put(RunStatusEnum.UPGRADE.getName(),deviceEntity.getCounts());
                }
            }
        }
        projectResult.setSumCount(sumCount);
        projectResult.setDeviceCount(sumCount);
        if(sumCount == 0 ){
            BigDecimal bigDecimal = new BigDecimal(0);
            deviceScale.put(RunStatusEnum.NORAML.getName(),bigDecimal);
            deviceScale.put(RunStatusEnum.WARNING.getName(),bigDecimal);
            deviceScale.put(RunStatusEnum.FAULT.getName(),bigDecimal);
            deviceScale.put(RunStatusEnum.OFFLINE.getName(),bigDecimal);
            deviceScale.put(RunStatusEnum.UPGRADE.getName(),bigDecimal);
        }else{
            Integer integer = deviceMap.get(RunStatusEnum.NORAML.getName());
            Integer integer1 = deviceMap.get(RunStatusEnum.WARNING.getName());
            Integer integer2 = deviceMap.get(RunStatusEnum.FAULT.getName());
            Integer integer3 = deviceMap.get(RunStatusEnum.OFFLINE.getName());
            Integer integer4 = deviceMap.get(RunStatusEnum.UPGRADE.getName());
            deviceScale.put(RunStatusEnum.NORAML.getName(),new BigDecimal(integer).divide(new BigDecimal(sumCount),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            deviceScale.put(RunStatusEnum.WARNING.getName(),new BigDecimal(integer1).divide(new BigDecimal(sumCount),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            deviceScale.put(RunStatusEnum.FAULT.getName(),new BigDecimal(integer2).divide(new BigDecimal(sumCount),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            deviceScale.put(RunStatusEnum.OFFLINE.getName(),new BigDecimal(integer3).divide(new BigDecimal(sumCount),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            deviceScale.put(RunStatusEnum.UPGRADE.getName(),new BigDecimal(integer4).divide(new BigDecimal(sumCount),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
        }
        Set<Map.Entry<String, Integer>> entries = deviceMap.entrySet();
        for (Map.Entry<String, Integer> entry : entries){
            Map<String,Integer> temp = new HashMap<>();
            temp.put(entry.getKey(),entry.getValue());
            deviceMapList.add(temp);
        }

        Set<Map.Entry<String, BigDecimal>> entries1 = deviceScale.entrySet();
        for (Map.Entry<String, BigDecimal> entry : entries1){
            Map<String,BigDecimal> temp = new HashMap<>();
            temp.put(entry.getKey(),entry.getValue());
            deviceScaleList.add(temp);
        }

        projectResult.setDeviceMap(deviceMapList);
        projectResult.setDeviceScale(deviceScaleList);
        return projectResult;
    }

    @Override
    public Long queryExclusiveId(String projectId) {
        return this.projectMapper.queryExclusiveId(projectId);
    }
}
