package com.remote.modules.advancedsetting.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.modules.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.modules.advancedsetting.entity.AdvancedSettingResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author zsm
 * @email 1648925727@qq.com
 * @date 2019-06-20 09:22:19
 */
@Mapper
public interface AdvancedSettingDao extends BaseMapper<AdvancedSettingEntity> {
    AdvancedSettingEntity queryByGroupId(@Param("groupId") String groupId);
    List<AdvancedSettingEntity> queryByDeviceCode(@Param("deviceCodes")List<String> deviceCodes);
    /*
     * @Author zhangwenping
     * @Description 通过设备codes修改高级设置信息
     * @Date 16:22 2019/7/24
     * @Param deviceCodes
     * @return int
     **/
    int updateAdvancedByDeviceCodes(@Param("deviceCodes")List<String> deviceCodes,@Param("groupId")String groupId,@Param("oldGroupId")String oldGroupId);
    /*
     * @Author zhangwenping
     * @Description 删除高级设置信息
     * @Date 17:37 2019/7/25
     * @Param deviceCode groupId
     * @return int
     **/
    int deleteAdvancedByDeviceCode(@Param("deviceCode")String deviceCode,@Param("groupId") String groupId);

    AdvancedSettingResult queryVol(@Param("deviceCode")String deviceCode);
    int updateAdvancedByDeviceCode(@Param("deviceCode")String deviceCode,@Param("groupId")String groupId,@Param("oldGroupId")String oldGroupId);
    boolean deleteAdvSet(@Param("deviceCodes")List<String> deviceCodes);
    boolean saveAdvanceSetting(@Param("aseList")List<AdvancedSettingEntity> aseList);

}
