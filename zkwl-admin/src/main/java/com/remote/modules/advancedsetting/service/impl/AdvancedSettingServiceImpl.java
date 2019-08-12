package com.remote.modules.advancedsetting.service.impl;

import com.remote.common.utils.*;
import com.remote.modules.advancedsetting.dao.AdvancedSettingDao;
import com.remote.modules.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.modules.advancedsetting.service.AdvancedSettingService;
import com.remote.modules.device.service.DeviceService;
import com.remote.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("advancedSettingService")
public class AdvancedSettingServiceImpl extends ServiceImpl<AdvancedSettingDao, AdvancedSettingEntity> implements AdvancedSettingService {

    @Autowired
    AdvancedSettingDao advancedSettingDao;
    @Autowired
    DeviceService deviceService;
    /**
     * 通过组id查询高级设置信息
     * */
    @Override
    public AdvancedSettingEntity queryByGroupId(String groupId) {
        return this.queryByDevOrGroupId(groupId,"0");
    }

    /**
     * 通过设备code查询高级设置信息
     * */
    @Override
    public List<AdvancedSettingEntity> queryByDeviceCode(List<String> deviceCodes) {
        return this.advancedSettingDao.queryByDeviceCode(deviceCodes);
    }
    /**
     * 通过高级设置id更新对应设备code得高级设置信息
     * */
    @Override
    public boolean updateAdvance(Long advSetId,AdvancedSettingEntity advancedSettingEntity) {
        return this.update(advancedSettingEntity,new QueryWrapper<AdvancedSettingEntity>().eq("id",advSetId));
    }
    /**
     * 通过组id或者是设备code查询高级设置信息
     * */
    @Override
    public AdvancedSettingEntity queryByProGroupId(String deviceCode,String groupId) {
        return this.baseMapper.selectOne(new QueryWrapper<AdvancedSettingEntity>().eq("device_code",deviceCode).or().eq("group_id",groupId));
    }
    /**
     * 通过组id或者是设备code查询高级设置信息
     * */
    @Override
    public AdvancedSettingEntity queryByDevOrGroupId(String groupId,String deviceCode) {
        return this.baseMapper.selectOne(new QueryWrapper<AdvancedSettingEntity>().eq("group_id",groupId).eq("device_code",deviceCode));
    }

    @Override
    public int updateAdvancedByDeviceCodes(List<String> deviceCodes,String groupId,String oldGroupId) {
        return advancedSettingDao.updateAdvancedByDeviceCodes(deviceCodes,groupId,oldGroupId);
    }

    @Override
    public int deleteAdvancedByDeviceCode(String deviceCode, String groupId) {
        return advancedSettingDao.deleteAdvancedByDeviceCode(deviceCode,groupId);
    }

    @Override
    public boolean addUpdateGroup(AdvancedSettingEntity advancedSetting, SysUserEntity curUser) {
        String groupId = advancedSetting.getGroupId();
        String devCode = advancedSetting.getDeviceCode();
        boolean falg = false;
        if(StringUtils.isBlank(devCode)||"0".equals(devCode)&&StringUtils.isNotBlank(groupId)){
            advancedSetting.setDeviceCode("0");
            AdvancedSettingEntity advancedSettingEntity = queryByDevOrGroupId(groupId,"0");
            advancedSetting.setUpdateUser(curUser.getRealName());
            advancedSetting.setUid(curUser.getUserId());
            if(advancedSettingEntity != null){
                falg = updateAdvance(advancedSettingEntity.getId(),advancedSetting);
                if(!falg){
                    return false;
                }
            }else{
                advancedSetting.setCreateTime(new Date());
                falg = save(advancedSetting);
                if(!falg){
                    return false;
                }
            }
            List<String> deviceCodes = deviceService.queryByGroupId(groupId);
            if(CollectionUtils.isNotEmpty(deviceCodes)&&deviceCodes.size()>0){
                List<AdvancedSettingEntity> advList= queryByDeviceCode((ArrayList<String>)deviceCodes);
                if(CollectionUtils.isNotEmpty(advList)){
                    for(AdvancedSettingEntity adv: advList)
                        if(adv.getId() != null){
                            //通过设备code把组最新设置的高级设置信息同步到对应的code中
                            advancedSetting.setDeviceCode(adv.getDeviceCode());
                            advancedSetting.setUpdateTime(new Date());
                            updateAdvance(adv.getId(),advancedSetting);
                        }
                }
            }
        }
        return true;
    }


}
