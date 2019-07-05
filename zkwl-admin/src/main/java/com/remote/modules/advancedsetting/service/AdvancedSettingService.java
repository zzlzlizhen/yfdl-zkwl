package com.remote.modules.advancedsetting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.StringUtils;
import com.remote.modules.advancedsetting.entity.AdvancedSettingEntity;


import java.util.Map;

/**
 * 
 *
 * @author zsm
 * @email 1648925727@qq.com
 * @date 2019-06-20 09:22:19
 */
public interface AdvancedSettingService extends IService<AdvancedSettingEntity> {

    PageUtils queryPage(Map<String, Object> params);

    AdvancedSettingEntity queryByProGroupId(String deviceCode,String groupId);
    boolean upadateAdvanceSteing(AdvancedSettingEntity advancedSettingEntity);
    AdvancedSettingEntity queryByGroupId(String groupId);
    AdvancedSettingEntity queryByDeviceCode(String deviceCode);
    boolean upadateAdvanceSteingById(AdvancedSettingEntity advancedSettingEntity,Long advSetId);
    boolean updateAdvance(Long advSetId,AdvancedSettingEntity advancedSettingEntity);
    AdvancedSettingEntity queryByDevGroupId(String deviceCode,String groupId);

}

