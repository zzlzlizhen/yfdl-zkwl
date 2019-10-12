package com.remote.modules.advancedsetting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.StringUtils;
import com.remote.modules.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.modules.advancedsetting.entity.AdvancedSettingResult;
import com.remote.modules.sys.entity.SysUserEntity;
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

    AdvancedSettingEntity queryByGroupId(String groupId) throws Exception;
    List<AdvancedSettingEntity> queryByDeviceCode(List<String> deviceCode);
    boolean updateAdvance(Long advSetId,AdvancedSettingEntity advancedSettingEntity);
    AdvancedSettingEntity queryByDevOrGroupId(String groupId,String deviceCode) throws Exception;
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

    /**
     *
     * @param advancedSettingEntity
     * @param
     * @throws Exception
     */

    void addUpdateGroup(AdvancedSettingEntity advancedSettingEntity, SysUserEntity curUser) throws Exception;

    void addUpdateDevice(AdvancedSettingEntity advancedSettingEntity,SysUserEntity curUser) throws Exception;
    AdvancedSettingResult queryVol(String deviceCode);
    boolean saveAdvSetDev(AdvancedSettingEntity advancedSettingEntity);

    /**
     *功能描述：通过设备code修改组id
     * @param deviceCode
     * @param groupId
     * @param oldGroupId
     * @return
     */
    int updateAdvancedByDeviceCode(String deviceCode,String groupId,String oldGroupId);
    boolean deleteAdvSet(List<String> deviceCode);
    boolean updateAdvanceByGAndDId(AdvancedSettingEntity advancedSettingEntity);
    boolean saveAdvanceSetting(List<AdvancedSettingEntity> advancedSettingEntityList);
}

