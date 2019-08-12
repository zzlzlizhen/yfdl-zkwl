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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


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

    /**
     * 功能描述:组的高级设置信息修改
     * 1、如果没有设置，则新增一条该组下的高级设置信息
     * 2、如果已经有设置，则更新其原来的设置信息
     * 3、将该组下所有的设备信息对应的高级设置信息，统一修改成和该次变更后的组高级设置一致
     * @author lizhen
     * @date 2019/8/12 19:49
     * @param
     * @return boolean
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addUpdateGroup(AdvancedSettingEntity advancedSetting, SysUserEntity curUser) {
        try {
            String groupId = advancedSetting.getGroupId();
            String devCode = advancedSetting.getDeviceCode();
            if(StringUtils.isBlank(devCode)||"0".equals(devCode)){
                AdvancedSettingEntity advancedSettingEntity = queryByDevOrGroupId(groupId,"0");
                if(advancedSettingEntity != null){
                    advancedSetting.setUpdateUser(curUser.getRealName());
                    advancedSetting.setUid(curUser.getUserId());
                    updateAdvance(advancedSettingEntity.getId(),advancedSetting);
                }else{
                    advancedSetting.setUpdateUser(curUser.getRealName());
                    advancedSetting.setUid(curUser.getUserId());
                    advancedSetting.setCreateTime(new Date());
                    advancedSetting.setDeviceCode("0");
                    save(advancedSetting);
                }
                //更新组下所有的设备的高级设置信息（排除组的高级设置本身）
                advancedSetting.setDeviceCode(null);
                this.update(advancedSetting,new QueryWrapper<AdvancedSettingEntity>().eq("groupId",groupId).notIn("deviceCode","0"));
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 功能描述:更新设备的高级设置。如有已经存在则更新，否则插入
     * @author lizhen
     * @date 2019/8/12 19:57
     * @param
     * @return boolean
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addUpdateDevice(AdvancedSettingEntity advancedSetting, SysUserEntity curUser) {
        try {
            advancedSetting.setUid(curUser.getUserId());
            advancedSetting.setUpdateUser(curUser.getRealName());

            AdvancedSettingEntity advancedSettingEntity = queryByDevOrGroupId(advancedSetting.getGroupId(),advancedSetting.getDeviceCode());
            if(advancedSettingEntity != null){
                updateAdvance(advancedSettingEntity.getId(),advancedSetting);
            }else{
                advancedSetting.setCreateTime(new Date());
                save(advancedSetting);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
