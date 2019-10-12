package com.remote.advancedsetting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.remote.advancedsetting.dao.AdvancedSettingDao;
import com.remote.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.advancedsetting.entity.AdvancedSettingResult;
import com.remote.advancedsetting.service.AdvancedSettingService;
import com.remote.common.es.utils.ESUtil;
import com.remote.common.utils.StringUtils;

import com.remote.device.dao.DeviceMapper;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.service.DeviceService;
import com.remote.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("advancedSettingService")
public class AdvancedSettingServiceImpl extends ServiceImpl<AdvancedSettingDao, AdvancedSettingEntity> implements AdvancedSettingService {

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    AdvancedSettingDao advancedSettingDao;
    @Autowired
    DeviceService deviceService;
    @Autowired
    private ESUtil esUtil;
    /**
     * 通过组id查询高级设置信息
     * */
    @Override
    public AdvancedSettingEntity queryByGroupId(String groupId) throws Exception {
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
     * 通过组id跟设备code更改高级设置信息
     * */
    @Override
    public boolean updateAdvanceByGAndDId(AdvancedSettingEntity advancedSettingEntity) {
        return this.update(advancedSettingEntity,new QueryWrapper<AdvancedSettingEntity>().eq("group_id",advancedSettingEntity.getGroupId()).eq("device_code","0"));
    }


    /**
     * 功能描述：通过组id或者是设备code查询高级设置信息
     * @param groupId
     * @param deviceCode
     * @return
     */
    @Override
    public AdvancedSettingEntity queryByDevOrGroupId(String groupId,String deviceCode) throws Exception{
        return this.baseMapper.selectOne(new QueryWrapper<AdvancedSettingEntity>().eq("group_id",groupId).eq("device_code",deviceCode));
    }

    /**
     * 功能描述：通过设备codes 组id 跟旧的组id更新高级设置中的组id
     * @param deviceCodes
     * @param groupId
     * @param oldGroupId
     * @return
     */
    @Override
    public int updateAdvancedByDeviceCodes(List<String> deviceCodes,String groupId,String oldGroupId) {
        return advancedSettingDao.updateAdvancedByDeviceCodes(deviceCodes,groupId,oldGroupId);
    }
    @Override
    public int updateAdvancedByDeviceCode(String deviceCode,String groupId,String oldGroupId) {
        return advancedSettingDao.updateAdvancedByDeviceCode(deviceCode,groupId,oldGroupId);
    }

    @Override
    public boolean deleteAdvSet(List<String> deviceCode) {
        return advancedSettingDao.deleteAdvSet(deviceCode);
    }

    /**
     * 功能描述：通过设备code跟组id删除高级设置对应的设备高级信息
     * @param deviceCode
     * @param groupId
     * @return
     */
    @Override
    public int deleteAdvancedByDeviceCode(String deviceCode, String groupId) {
        return advancedSettingDao.deleteAdvancedByDeviceCode(deviceCode,groupId);
    }

    /**
     * 功能描述:组的高级设置信息修改
     * 1、如果没有设置，则新增一条该组下的高级设置信息
     * 2、如果已经有设置，则更新其原来的设置信息
     * 3、将该组下所有的设备信息对应的高级设置信息，统一修改成和该次变更后的组高级设置一致
     * @date 2019/8/12 19:49
     * @return boolean
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addUpdateGroup(AdvancedSettingEntity advancedSetting, SysUserEntity curUser) throws Exception {
        String groupId = advancedSetting.getGroupId();
        String devCode = advancedSetting.getDeviceCode();
        if (StringUtils.isBlank(devCode) || "0".equals(devCode)) {
            AdvancedSettingEntity advancedSettingEntity = queryByDevOrGroupId(groupId, "0");
            advancedSetting.setUpdateUser(curUser.getUsername());
            advancedSetting.setUid(curUser.getUserId());
            if (advancedSettingEntity != null) {
                advancedSetting.setUpdateTime(new Date());
                updateAdvance(advancedSettingEntity.getId(), advancedSetting);
            } else {
                advancedSetting.setCreateTime(new Date());
                advancedSetting.setDeviceCode("0");
                save(advancedSetting);
            }
            //更新组下所有的设备的高级设置信息（排除组的高级设置本身）
            advancedSetting.setDeviceCode(null);
            this.update(advancedSetting, new QueryWrapper<AdvancedSettingEntity>().eq("group_id", groupId).notIn("device_code", "0"));
        }
    }

    /**
     * 功能描述:更新设备的高级设置。如有已经存在则更新，否则插入
     * @date 2019/8/12 19:57
     * @param
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addUpdateDevice(AdvancedSettingEntity advancedSetting, SysUserEntity curUser) throws Exception {
        boolean flag = false;
        advancedSetting.setUid(curUser.getUserId());
        advancedSetting.setUpdateUser(curUser.getUsername());
        AdvancedSettingEntity advancedSettingEntity = queryByDevOrGroupId(advancedSetting.getGroupId(),advancedSetting.getDeviceCode());
        if(advancedSettingEntity != null){
            advancedSetting.setUpdateTime(new Date());
            flag = updateAdvance(advancedSettingEntity.getId(),advancedSetting);
        }else{
            advancedSetting.setCreateTime(new Date());
            flag =  save(advancedSetting);
        }
        if(flag){
            List<String> deviceCodes = new ArrayList<String>();
            DeviceEntity deviceEntity = new DeviceEntity();
            if(StringUtils.isNotBlank(advancedSetting.getDeviceCode())){
                deviceCodes.add(advancedSetting.getDeviceCode());
                List<DeviceEntity> deviceEntities = deviceMapper.queryDeviceByCodes(deviceCodes);
                if(CollectionUtils.isNotEmpty(deviceEntities)||deviceEntities.size()>0){
                    for(DeviceEntity devEntity:deviceEntities){
                        deviceEntity.setDeviceId(devEntity.getDeviceId());
                    }
                }
            }

            deviceEntity.setUpdateUser(advancedSetting.getUid());
            deviceEntity.setLightingDuration(initAdv(advancedSetting)+"");
            deviceEntity.setMorningHours(advancedSetting.getTimeDown()+"");
            int i = deviceMapper.updateById(deviceEntity);
            System.out.println("成功"+ i);
        }
    }
    /**
     * 功能描述：通过设备code查询充电电压跟放电电压
     * @param deviceCode
     * @return
     */

    @Override
    public AdvancedSettingResult queryVol(String deviceCode) {

        return this.advancedSettingDao.queryVol(deviceCode);
    }

    /**
     * 功能描述：通过组id跟设备code保存设备高级设置数据
     * @param advancedSettingEntity
     * @return
     */
    @Override
    public boolean saveAdvSetDev(AdvancedSettingEntity advancedSettingEntity) {
        return  save(advancedSettingEntity);
    }

    /**
     * 功能描述：取高级设置亮灯时长，改变实时数据中亮灯时长
     * @param advancedSettingEntity
     * @return
     */
    public int initAdv(AdvancedSettingEntity advancedSettingEntity){
        int sum = 0;
        if(advancedSettingEntity.getTime1() != null){
            sum+=advancedSettingEntity.getTime1();
        }
        if(advancedSettingEntity.getTime2() != null){
            sum+=advancedSettingEntity.getTime2();
        }
        if(advancedSettingEntity.getTime3() != null){
            sum+=advancedSettingEntity.getTime3();
        }
        if(advancedSettingEntity.getTime4() != null){
            sum+=advancedSettingEntity.getTime4();
        }
        if(advancedSettingEntity.getTime5() != null){
            sum+=advancedSettingEntity.getTime5();
        }
        return sum;
    }
}
