package com.remote.modules.advancedsetting.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.remote.common.utils.CollectionUtils;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.R;
import com.remote.common.utils.StringUtils;
import com.remote.common.validator.ValidatorUtils;
import com.remote.modules.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.modules.advancedsetting.service.AdvancedSettingService;
import com.remote.modules.device.service.DeviceService;
import com.remote.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zsm
 * @email 1648925727@qq.com
 * @date 2019-06-20 09:22:19
 */
@RestController
@RequestMapping("/advancedsetting")
public class AdvancedSettingController extends AbstractController{
    @Autowired
    private AdvancedSettingService advancedSettingService;
    @Autowired
    DeviceService deviceService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = advancedSettingService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 组高级设置信息查看接口
     */
    @RequestMapping("/settingInfo")
    public R info(String groupId){
        if(StringUtils.isNotBlank(groupId)){
            String deviceCode = "0";
            AdvancedSettingEntity advancedSettingEntity = advancedSettingService.queryByGroupId(groupId);
            if(advancedSettingEntity != null){
                advancedSettingEntity.setDeviceCode(deviceCode);
                return R.ok().put("info",advancedSettingEntity);
            }
        }
        return R.ok().put("info","");
    }


    /**
     * 修改组的高级设置
     */
    @RequestMapping("/updateGroup")
    public R update(AdvancedSettingEntity advancedSetting){
        String groupId = advancedSetting.getGroupId();
        String devCode = advancedSetting.getDeviceCode();
        if(StringUtils.isBlank(devCode)||"0".equals(devCode)&&StringUtils.isNotBlank(groupId)){
            advancedSetting.setDeviceCode("0");
            AdvancedSettingEntity advancedSettingEntity = advancedSettingService.queryByDevGroupId(devCode,groupId);
            advancedSetting.setUpdateUser(getUser().getRealName());
            advancedSetting.setUid(getUserId());
            if(advancedSettingEntity != null){
                advancedSetting.setUpdateTime(new Date());
                advancedSettingService.updateById(advancedSetting);
            }else{
                advancedSetting.setCreateTime(new Date());
                advancedSettingService.save(advancedSetting);
            }
            List<String> deviceCodes = deviceService.queryByGroupId(groupId);
            if(CollectionUtils.isNotEmpty(deviceCodes)&&deviceCodes.size()>0){
                for(String deviceCode:deviceCodes){
                    AdvancedSettingEntity adv = advancedSettingService.queryByDeviceCode(deviceCode);
                    if(adv != null){
                        Long devSetId = adv.getId();
                        if(adv.getId() != null){
                            //通过设备code把组最新设置的高级设置信息同步到对应的code中
                            advancedSetting.setDeviceCode(adv.getDeviceCode());
                            advancedSetting.setUpdateTime(new Date());
                            advancedSettingService.updateAdvance(devSetId,advancedSetting);
                        }
                    }
                }
            }
        }
        return R.ok();
    }

    /**
     * 查询设备的信息时，必须传组id跟设备code
     */
    @RequestMapping("/queryDevAdvInfo")
    public R queryDevAdvInfo(String groupId,String deviceCode){
        //设备code不为空并且设备code不等于0
        if(StringUtils.isNotBlank(groupId)&&StringUtils.isNotBlank(deviceCode)){
            AdvancedSettingEntity advancedSettingEntity  = advancedSettingService.queryByDeviceCode(deviceCode);
            if(advancedSettingEntity != null){
                return R.ok().put("info",advancedSettingEntity);
            }
            AdvancedSettingEntity advSE = advancedSettingService.queryByGroupId(groupId);
            if(advSE == null){
                return R.ok().put("info","");
            }
            return R.ok().put("info",advSE);
        }
        return R.error("组id跟设备编号不能为空");
    }
    /**
     * 更改设备高级设置 组id跟设备code必传
     */
    @RequestMapping("/updateDevice")
    public R updateDevice(AdvancedSettingEntity advancedSetting){
        String deviceCode = advancedSetting.getDeviceCode();
        if(StringUtils.isNotBlank(advancedSetting.getGroupId())&&StringUtils.isNotBlank(deviceCode)&&!"0".equals(deviceCode)){
            AdvancedSettingEntity advSE = advancedSettingService.queryByDeviceCode(deviceCode);
            advancedSetting.setUid(getUserId());
            advancedSetting.setUpdateUser(getUser().getRealName());
            if(advSE != null){
                advancedSetting.setUpdateTime(new Date());
                advancedSettingService.updateById(advancedSetting);
            }else{
                advancedSetting.setCreateTime(new Date());
                advancedSettingService.save(advancedSetting);
            }
        }
        return R.ok();
    }
}
