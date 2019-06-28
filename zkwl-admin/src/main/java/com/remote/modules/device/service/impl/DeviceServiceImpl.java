package com.remote.modules.device.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.common.enums.CommunicationEnum;
import com.remote.common.enums.FaultlogEnum;
import com.remote.modules.device.dao.DeviceMapper;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.entity.DeviceQuery;
import com.remote.modules.device.service.DeviceService;
import com.remote.modules.faultlog.entity.FaultlogEntity;
import com.remote.modules.faultlog.service.FaultlogService;
import com.remote.modules.group.dao.GroupMapper;
import com.remote.modules.group.entity.GroupEntity;
import com.remote.modules.group.entity.GroupQuery;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author zhangwenping
 * @Date 2019/6/4 13:34
 * @Version 1.0
 **/
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private FaultlogService faultlogService;

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public PageInfo<DeviceEntity> queryDevice(DeviceQuery deviceQuery) {
        PageHelper.startPage(deviceQuery.getPageNum(),deviceQuery.getPageSize());
        List<DeviceEntity> list = deviceMapper.queryDevice(deviceQuery);
        GroupQuery groupQuery = new GroupQuery();
        groupQuery.setProjectId(deviceQuery.getProjectId());
        List<GroupEntity> groupList = groupMapper.queryGroupByName(groupQuery);
        Map<String,String> map = new HashMap<>();
        if(CollectionUtils.isNotEmpty(groupList)){
            for(GroupEntity groupEntity : groupList){
                map.put(groupEntity.getGroupId(),groupEntity.getGroupName());
            }
        }
        if(CollectionUtils.isNotEmpty(list)){
            for(DeviceEntity deviceEntity : list){
                deviceEntity.setGroupName(map.get(deviceEntity.getGroupId()));
            }
        }
        PageInfo<DeviceEntity> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public boolean addDevice(DeviceEntity deviceEntity) {
        String deviceCode = deviceEntity.getDeviceCode();
        //目前只有一种产品，2G 日后在添加其他产品
        deviceEntity.setCommunicationType(CommunicationEnum.NORMAL.getCode());
        String deviceType = deviceCode.substring(0, 4);
        deviceEntity.setDeviceType(deviceType);
        return deviceMapper.insert(deviceEntity) > 0 ? true : false;
    }

    @Override
    public boolean deleteDevice(DeviceQuery deviceQuery) {
        return deviceMapper.deleteDevice(deviceQuery) > 0 ? true : false;
    }

    @Override
    public boolean moveGroup(DeviceQuery deviceQuery) {
        return deviceMapper.deleteDevice(deviceQuery) > 0 ? true : false;
    }

    @Override
    public boolean updateById(DeviceEntity deviceEntity) {
//        StringBuffer sb = new StringBuffer("");
//        String userName = deviceEntity.getUpdateUserName();
//        if(StringUtils.isNotEmpty(deviceEntity.getLightingDuration())){
//            sb.append("亮灯时长,");
//        }
//        if(StringUtils.isNotEmpty(deviceEntity.getMorningHours())){
//            sb.append("晨亮时长,");
//        }
//        if(StringUtils.isNotEmpty(deviceEntity.getLight())){
//            sb.append("亮度,");
//        }
//        if(deviceEntity.getOnOff() != null){
//            sb.append("开关,");
//        }
//        if(!"".equals(sb.toString())){
//            FaultlogEntity faultlogEntity = new FaultlogEntity();
//            faultlogEntity.setProjectId(deviceEntity.getProjectId());
//            faultlogEntity.setFaultLogId(UUID.randomUUID().toString());
//            faultlogEntity.setDeviceId(deviceEntity.getDeviceId());
//            faultlogEntity.setCreateTime(new Date());
//            faultlogEntity.setCreateUserId(deviceEntity.getCreateUser());
//            faultlogEntity.setLogStatus(FaultlogEnum.OPERATIONALLOG.getCode());
//            String logStr = sb.substring(0, sb.length() - 1);
//            faultlogEntity.setFaultLogDesc(logStr);
//            faultlogService.addFaultlog(faultlogEntity);
//        }
        return deviceMapper.updateById(deviceEntity) > 0 ? true : false;
    }

    @Override
    public List<DeviceEntity> queryDeviceNoPage(DeviceQuery deviceQuery) {
        return deviceMapper.queryDevice(deviceQuery);
    }

    @Override
    public List<DeviceEntity> queryDeviceByGroupCount(List<String> groupIds,List<String> projectIds, Integer deviceStatus) {
        return deviceMapper.queryDeviceByGroupCount(groupIds,projectIds,deviceStatus);
    }

    @Override
    public DeviceEntity queryDeviceByDeviceId(String deviceId) {
        DeviceEntity deviceEntity = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            deviceEntity = deviceMapper.queryDeviceByDeviceId(deviceId);
            String startTime = sdf.format(deviceEntity.getCreateTime());
            String endTime = sdf.format(new Date());
            long day = (sdf.parse(startTime).getTime() - sdf.parse(endTime).getTime()) /(24*60*60*1000);
            deviceEntity.setRunDay(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return deviceEntity;
    }

    @Override
    public List<DeviceEntity> queryCountGroupByCity(Long userId) {
        List<SysUserEntity> userList = sysUserService.queryAllLevel(userId);
        if(CollectionUtils.isNotEmpty(userList)){
            List<Long> userIds = userList.parallelStream().map(sysUserEntity -> sysUserEntity.getUserId()).collect(Collectors.toCollection(ArrayList::new));
            ProjectQuery projectQuery = new ProjectQuery();
            projectQuery.setUserIds(userIds);
            List<ProjectEntity> list = projectMapper.queryProjectByUserIds(projectQuery);
            if(CollectionUtils.isNotEmpty(list)){
                List<String> projectIds = list.parallelStream().map(projectEntity -> projectEntity.getProjectId()).collect(Collectors.toCollection(ArrayList::new));

            }
        }
        return null;
    }

    @Override
    public int updateOnOffByIds(DeviceQuery deviceQuery) {
        List<DeviceEntity> deviceEntityList = null;
        //代表操作单个设备开关
        if(StringUtils.isNotEmpty(deviceQuery.getDeviceId())){
            DeviceEntity deviceEntity = new DeviceEntity();
            deviceEntity.setDeviceId(deviceQuery.getDeviceId());
            deviceEntityList.add(deviceEntity);
        }else{
            deviceEntityList = deviceMapper.queryDevice(deviceQuery);
        }
        StringBuffer sb = new StringBuffer();
        if(StringUtils.isNotEmpty(deviceQuery.getLightingDuration())){
            sb.append("亮灯时长,");
        }
        if(StringUtils.isNotEmpty(deviceQuery.getMorningHours())){
            sb.append("晨亮时长,");
        }
        if(StringUtils.isNotEmpty(deviceQuery.getLight())){
            sb.append("亮度,");
        }
        if(deviceQuery.getOnOff() != null){
            sb.append("开关,");
        }
        List<String> deviceList = new ArrayList<>();
        String userName = deviceQuery.getUpdateUserName();
        if(CollectionUtils.isNotEmpty(deviceEntityList)){
            for (DeviceEntity device : deviceEntityList){
                deviceList.add(device.getDeviceId());
                FaultlogEntity faultlogEntity = new FaultlogEntity();
                faultlogEntity.setProjectId(device.getProjectId());
                faultlogEntity.setFaultLogId(UUID.randomUUID().toString());
                faultlogEntity.setDeviceId(device.getDeviceId());
                faultlogEntity.setCreateTime(new Date());
                faultlogEntity.setCreateUserId(deviceQuery.getUpdateUser());
                faultlogEntity.setLogStatus(FaultlogEnum.OPERATIONALLOG.getCode());
                faultlogEntity.setFaultLogDesc(userName+"操作了路灯"+sb.toString());
                faultlogService.addFaultlog(faultlogEntity);
            }
        }
        deviceQuery.setDeviceList(deviceList);
        return deviceMapper.updateOnOffByIds(deviceQuery);
    }

}
