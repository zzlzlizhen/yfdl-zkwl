package com.remote.advancedsetting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.remote.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.advancedsetting.entity.AdvancedSettingResult;
import com.remote.sys.entity.SysUserEntity;

import java.util.List;

/**
 * 
 *
 * @author zsm
 * @email 1648925727@qq.com
 * @date 2019-06-20 09:22:19
 */
public interface AdvancedSettingService extends IService<AdvancedSettingEntity> {

    AdvancedSettingEntity queryByGroupId(String groupId);
    List<AdvancedSettingEntity> queryByDeviceCode(List<String> deviceCode);
    boolean updateAdvance(Long advSetId, AdvancedSettingEntity advancedSettingEntity);
    AdvancedSettingEntity queryByDevOrGroupId(String groupId, String deviceCode);
    /*
     * @Author zhangwenoping
     * @Description 通过设备codes修改高级设置信息
     * @Date 16:14 2019/7/24
     * @Param deviceIds
     * @return List<AdvancedSettingEntity>
     **/
    int updateAdvancedByDeviceCodes(List<String> deviceCodes, String groupId, String oldGroupId);
    /*
     * @Author zhangwenping
     * @Description 删除高级设置信息
     * @Date 17:35 2019/7/25
     * @Param deviceCode groupId
     * @return  int
     **/
    int deleteAdvancedByDeviceCode(String deviceCode, String groupId);

    /**
     *
     * @param advancedSettingEntity
     * @param
     *
     *
     * @throws Exception
     */

    void addUpdateGroup(AdvancedSettingEntity advancedSettingEntity, SysUserEntity curUser) throws Exception;

    void addUpdateDevice(AdvancedSettingEntity advancedSettingEntity, SysUserEntity curUser) throws Exception;
    AdvancedSettingResult queryVol(String deviceCode);
    boolean saveAdvSetDev(AdvancedSettingEntity advancedSettingEntity);
    boolean updateAdvSetDev(AdvancedSettingEntity advancedSettingEntity);
    AdvancedSettingEntity queryAdvSetDev(String groupId, String deviceCode);
    AdvancedSettingEntity queryAdvSetDevCode(String deviceCode);

    /**
     *功能描述：通过设备code修改组id
     * @param deviceCode
     * @param groupId
     * @param oldGroupId
     * @return
     */
    int updateAdvancedByDeviceCode(String deviceCode, String groupId, String oldGroupId);
    boolean deleteAdvSet(List<String> deviceCode);

}

