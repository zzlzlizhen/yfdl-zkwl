package com.remote.modules.advancedsetting.service.impl;

import com.remote.common.utils.PageUtils;
import com.remote.common.utils.Query;
import com.remote.modules.advancedsetting.dao.AdvancedSettingDao;
import com.remote.modules.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.modules.advancedsetting.service.AdvancedSettingService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("advancedSettingService")
public class AdvancedSettingServiceImpl extends ServiceImpl<AdvancedSettingDao, AdvancedSettingEntity> implements AdvancedSettingService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AdvancedSettingEntity> page = this.page(
                new Query<AdvancedSettingEntity>().getPage(params),
                new QueryWrapper<AdvancedSettingEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public AdvancedSettingEntity queryByProGroupId(String deviceCode,String groupId) {
        return this.baseMapper.selectOne(new QueryWrapper<AdvancedSettingEntity>().eq("device_code",deviceCode).eq("group_id",groupId));
    }

}
