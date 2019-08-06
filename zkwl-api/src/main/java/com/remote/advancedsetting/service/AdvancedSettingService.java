package com.remote.advancedsetting.service;

import com.remote.advancedsetting.entity.AdvancedSettingEntity;


/**
 * 
 *
 * @author zhangwenping
 */
public interface AdvancedSettingService  {

    /*
     * @Author zhangwenping
     * @Description 根据设备编号查询高级设置信息
     * @Date 13:30 2019/7/30
     * @Param deviceCode
     * @return AdvancedSettingEntity
     **/
    AdvancedSettingEntity queryByDeviceCode(String deviceCode);

}

