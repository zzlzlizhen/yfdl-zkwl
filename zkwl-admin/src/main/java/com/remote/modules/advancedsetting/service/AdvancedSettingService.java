package com.remote.modules.advancedsetting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.StringUtils;
import com.remote.modules.advancedsetting.entity.AdvancedSettingEntity;
import org.apache.ibatis.annotations.Param;


import java.util.List;
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
    List<AdvancedSettingEntity> queryByDeviceCode(List<String> deviceCode);
    boolean upadateAdvanceSteingById(AdvancedSettingEntity advancedSettingEntity,Long advSetId);
    boolean updateAdvance(Long advSetId,AdvancedSettingEntity advancedSettingEntity);
    AdvancedSettingEntity queryByDevGroupId(String deviceCode,String groupId);
    AdvancedSettingEntity queryByDevOrGroupId(String groupId,String deviceCode);
    /*
     * @Author zhangwenoping
     * @Description 通过设备codes修改高级设置信息
     * @Date 16:14 2019/7/24
     * @Param deviceIds
     * @return List<AdvancedSettingEntity>
     **/
    int updateAdvancedByDeviceCodes(List<String> deviceCodes,String groupId,String oldGroupId);
    /*
     * @Author zhangwenping
     * @Description 删除高级设置信息
     * @Date 17:35 2019/7/25
     * @Param deviceCode groupId
     * @return  int
     **/
    int deleteAdvancedByDeviceCode(String deviceCode,String groupId);
}

