package com.remote.modules.advancedsetting.service.impl;

import com.remote.common.utils.PageUtils;
import com.remote.common.utils.Query;
import com.remote.modules.advancedsetting.dao.AdvancedSettingDao;
import com.remote.modules.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.modules.advancedsetting.service.AdvancedSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("advancedSettingService")
public class AdvancedSettingServiceImpl extends ServiceImpl<AdvancedSettingDao, AdvancedSettingEntity> implements AdvancedSettingService {

    @Autowired
    AdvancedSettingDao advancedSettingDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AdvancedSettingEntity> page = this.page(
                new Query<AdvancedSettingEntity>().getPage(params),
                new QueryWrapper<AdvancedSettingEntity>()
        );
        return new PageUtils(page);
    }
    /**
     * 通过设备code更新设备信息
     * */
    @Override
    public boolean upadateAdvanceSteing(AdvancedSettingEntity advancedSettingEntity){
        return this.update(advancedSettingEntity,new QueryWrapper<AdvancedSettingEntity>().eq("device_code",advancedSettingEntity.getDeviceCode()));
    }
    /**
     * 通过高级设置id更新高级设置信息
     * */
    @Override
    public boolean upadateAdvanceSteingById(AdvancedSettingEntity advancedSettingEntity,Long advSetId){
        return this.update(advancedSettingEntity,new QueryWrapper<AdvancedSettingEntity>().eq("id",advSetId));
    }
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
    public AdvancedSettingEntity queryByDeviceCode(String deviceCode) {
        return this.queryByProGroupId(deviceCode,"0");
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
    public AdvancedSettingEntity queryByDevGroupId(String deviceCode,String groupId) {
        return this.baseMapper.selectOne(new QueryWrapper<AdvancedSettingEntity>().eq("device_code",deviceCode).eq("group_id",groupId));
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

}
