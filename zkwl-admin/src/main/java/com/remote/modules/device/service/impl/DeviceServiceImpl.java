package com.remote.modules.device.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import com.remote.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        //此处省略根据设备编号自动选择设备类型代码  后期补充start

        //此处省略根据设备编号自动选择设备类型代码  后期补充end
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
        StringBuffer sb = new StringBuffer("");
        String userName = deviceEntity.getUpdateUserName();
        if(StringUtils.isNotEmpty(deviceEntity.getLightingDuration())){
            sb.append("亮灯时长,");
        }
        if(StringUtils.isNotEmpty(deviceEntity.getMorningHours())){
            sb.append("晨亮时长,");
        }
        if(StringUtils.isNotEmpty(deviceEntity.getLight())){
            sb.append("亮度,");
        }
        if(deviceEntity.getOnOff() != null){
            sb.append("开关,");
        }
        if(!"".equals(sb)){
            FaultlogEntity faultlogEntity = new FaultlogEntity();
            faultlogEntity.setProjectId(deviceEntity.getProjectId());
            faultlogEntity.setFaultLogId(UUID.randomUUID().toString());
            faultlogEntity.setDeviceId(deviceEntity.getDeviceId());
            faultlogEntity.setCreateTime(new Date());
            faultlogEntity.setCreateUserId(deviceEntity.getUpdateUser());
            faultlogEntity.setLogStatus(FaultlogEnum.OPERATIONALLOG.getCode());
            String logStr = sb.substring(0, sb.length() - 1);
            faultlogEntity.setFaultLogDesc(logStr);
            faultlogService.addFaultlog(faultlogEntity);
        }
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
        return deviceMapper.queryDeviceByDeviceId(deviceId);
    }

}
