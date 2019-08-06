package com.remote.advancedsetting.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.advancedsetting.entity.AdvancedSettingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author zhangwenping
 */
@Mapper
public interface AdvancedSettingDao extends BaseMapper<AdvancedSettingEntity> {
    /*
     * @Author zhangwenping
     * @Description 根据设备编号查询高级设置信息
     * @Date 13:30 2019/7/30
     * @Param deviceCode
     * @return AdvancedSettingEntity
     **/
    AdvancedSettingEntity queryByDeviceCode(@Param("deviceCode") String deviceCode);
}
