package com.remote.modules.advancedsetting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.remote.common.utils.PageUtils;
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

    AdvancedSettingEntity queryByProGroupId(String projectId,String groupId);
}

