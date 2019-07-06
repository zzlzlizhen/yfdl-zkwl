package com.remote.modules.device.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.common.enums.CommunicationEnum;
import com.remote.common.enums.FaultlogEnum;
import com.remote.common.utils.ValidateUtils;
import com.remote.modules.device.dao.DeviceMapper;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.entity.DeviceQuery;
import com.remote.modules.device.entity.DeviceResult;
import com.remote.modules.device.service.DeviceService;
import com.remote.modules.faultlog.entity.FaultlogEntity;
import com.remote.modules.faultlog.service.FaultlogService;
import com.remote.modules.group.dao.GroupMapper;
import com.remote.modules.group.entity.GroupEntity;
import com.remote.modules.group.entity.GroupQuery;
import com.remote.modules.project.dao.ProjectMapper;
import com.remote.modules.project.entity.ProjectEntity;
import com.remote.modules.project.entity.ProjectQuery;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.remote.common.utils.DeviceTypeMap.DEVICE_TYPE;

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
    public PageInfo<DeviceEntity> queryDevice(DeviceQuery deviceQuery) throws Exception {
        ValidateUtils.validate(deviceQuery,Arrays.asList("projectId"));
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
    public boolean addDevice(DeviceEntity deviceEntity) throws Exception {
        ValidateUtils.validate(deviceEntity,Arrays.asList("deviceCode","deviceName"));
        String deviceCode = deviceEntity.getDeviceCode();
        //目前只有一种产品，2G 日后在添加其他产品
        deviceEntity.setCommunicationType(CommunicationEnum.NORMAL.getCode());
        String deviceType = deviceCode.substring(0, 4);
        if(DEVICE_TYPE.get(deviceType) != null){
            deviceEntity.setDeviceType(DEVICE_TYPE.get(deviceType).toString());
            return deviceMapper.insert(deviceEntity) > 0 ? true : false;
        }else{
            return false;
        }

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
    public boolean updateById(DeviceEntity deviceEntity) throws Exception {
        ValidateUtils.validate(deviceEntity,Arrays.asList("deviceId"));
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
    public DeviceResult queryDeviceByDeviceId(String deviceId) {
        DeviceResult resut = new DeviceResult();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            DeviceEntity deviceEntity = deviceMapper.queryDeviceByDeviceId(deviceId);
            String startTime = sdf.format(deviceEntity.getCreateTime());
            String endTime = sdf.format(new Date());
            long day = (sdf.parse(startTime).getTime() - sdf.parse(endTime).getTime()) /(24*60*60*1000);
            BeanUtils.copyProperties(deviceEntity, resut);
            resut.setRunDay(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resut;
    }

    @Override
    public List<Map<String,Integer>> queryCountGroupByCity(Long userId) {
        List<SysUserEntity> userList = sysUserService.queryAllLevel(userId);
        if(CollectionUtils.isNotEmpty(userList)){
            List<Long> userIds = userList.parallelStream().map(sysUserEntity -> sysUserEntity.getUserId()).collect(Collectors.toCollection(ArrayList::new));
            return deviceMapper.queryDeviceGroupByCity(userIds);
        }
        return null;
    }


    @Override
    public int updateOnOffByIds(DeviceQuery deviceQuery) {
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
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
                faultlogEntity.setCreateUserId(deviceQuery.getCreateUser());
                faultlogEntity.setLogStatus(FaultlogEnum.OPERATIONALLOG.getCode());
                faultlogEntity.setFaultLogDesc(userName+"操作了路灯"+sb.toString());
                faultlogService.addFaultlog(faultlogEntity);
            }
        }
        deviceQuery.setDeviceList(deviceList);
        return deviceMapper.updateOnOffByIds(deviceQuery);
    }

    @Override
    public String queryByDevCode(String deviceCode) {
        return deviceMapper.queryByDevCode(deviceCode);
    }

    @Override
    public List<String> queryByGroupId(String groupId) {
        return this.deviceMapper.queryByGroupId(groupId);
    }

    @Override
    public int updateUserDevice(DeviceQuery deviceQuery) {
        return deviceMapper.updateUserDevice(deviceQuery);
    }

    /**
     * 通过当前用户id查询所有用户的设备数量
     * */
    @Override
    public Integer getDeviceCount(List<Long> curUids) {

        return this.deviceMapper.getDeviceCount(curUids);
    }

    /**
     * 通过用户id
     * */
    @Override
    public Integer getDevFailNum(List<Long> curUserIds) {
        return this.deviceMapper.getDevFailNum(curUserIds);
    }

    @Override
    public List<String> getDeviceCode(List<Long> userIds) {
        return this.deviceMapper.getDeviceCode(userIds);
    }
    /**
     * 通过所有用户id获取当前设备信息
     * */
    @Override
    public List<DeviceEntity> getDeviceInfoList(List<Long> userIds){
        return this.deviceMapper.getDeviceInfoList(userIds);
    }
}
