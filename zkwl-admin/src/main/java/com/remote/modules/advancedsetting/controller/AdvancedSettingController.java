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
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = advancedSettingService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 组高级设置信息查看接口
     */
    @RequestMapping(value = "/settingInfo",method = RequestMethod.GET)
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
    @RequestMapping(value = "/updateGroup",method = RequestMethod.POST)
    public R update(AdvancedSettingEntity advancedSetting){
        boolean falg = false;
        String groupId = advancedSetting.getGroupId();
        String devCode = advancedSetting.getDeviceCode();
        if(StringUtils.isBlank(devCode)||"0".equals(devCode)&&StringUtils.isNotBlank(groupId)){
             advancedSetting.setDeviceCode("0");
            AdvancedSettingEntity advancedSettingEntity = advancedSettingService.queryByDevGroupId(advancedSetting.getDeviceCode(),groupId);
            advancedSetting.setUpdateUser(getUser().getRealName());
            advancedSetting.setUid(getUserId());
            if(advancedSettingEntity != null){
                advancedSetting.setUpdateTime(new Date());
              /*  advancedSettingService.updateById(advancedSetting);*/
                falg = advancedSettingService.updateAdvance(advancedSettingEntity.getId(),advancedSetting);
                if(!falg){
                    return R.error("更新数据失败");
                }
            }else{
                advancedSetting.setCreateTime(new Date());
                falg = advancedSettingService.save(advancedSetting);
                if(!falg){
                    return R.error("保存数据失败");
                }
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
    @RequestMapping(value = "/queryDevAdvInfo",method = RequestMethod.GET)
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
    @RequestMapping(value = "/updateDevice",method = RequestMethod.POST)
    public R updateDevice(AdvancedSettingEntity advancedSetting){
        boolean falg = false;
        String deviceCode = advancedSetting.getDeviceCode();
        if(StringUtils.isNotBlank(advancedSetting.getGroupId())&&StringUtils.isNotBlank(deviceCode)&&!"0".equals(deviceCode)){
            AdvancedSettingEntity advSE = advancedSettingService.queryByDeviceCode(deviceCode);
            advancedSetting.setUid(getUserId());
            advancedSetting.setUpdateUser(getUser().getRealName());
            if(advSE != null){
                advancedSetting.setUpdateTime(new Date());
                falg = advancedSettingService.updateAdvance(advSE.getId(),advancedSetting);
                if(!falg){
                    return R.error("更新数据失败");
                }
            }else{
                advancedSetting.setCreateTime(new Date());
                falg = advancedSettingService.save(advancedSetting);
                if(!falg){
                    return R.error("保存数据失败");
                }
            }
        }
        return R.ok();
    }

    /**
     * 对参数做数据验证
     * */
    public R initAdvSetEnt(AdvancedSettingEntity advSet){
        if(advSet.getSwitchDelayTime() < 1 || advSet.getSwitchDelayTime() > 120){
            R.error("开关灯延时时间应在1分钟到120分钟之间");
        }
        if(advSet.getInspectionTime() < 1 || advSet.getInspectionTime()>3600){
            R.error("巡检时间应在1到3600分钟");
        }
       if(advSet.getTimeTurnOn() < 0 || advSet.getTimeTurnOn() > 1440){
            R.error("开灯时刻应在0-24小时内");
       }
        if(advSet.getTimeTurnOff() < 0 || advSet.getTimeTurnOff() > 1440){
            R.error("关灯时刻应在0-24小时内");
        }
        if(advSet.getTime1()<0 || advSet.getTime1() < 770){
            R.error("一时段时常应该为0至12小时五十分钟");
        }
        if(advSet.getTime2()<0 || advSet.getTime2() < 770){
            R.error("一时段时常应该为0至12小时五十分钟");
        }
        if(advSet.getTime3()<0 || advSet.getTime3() < 770){
            R.error("一时段时常应该为0至12小时五十分钟");
        }

        if(advSet.getTime4()<0 || advSet.getTime4() < 770){
            R.error("一时段时常应该为0至12小时五十分钟");
        }
        if(advSet.getTime5()<0 || advSet.getTime5() < 770){
            R.error("一时段时常应该为0至12小时五十分钟");
        }
        if(advSet.getTimeDown()<0 || advSet.getTimeDown() < 770){
            R.error("一时段时常应该为0至12小时五十分钟");
        }
        if(advSet.getInductionLightOnDelay() < 1 || advSet.getInductionLightOnDelay()>600){
            R.error("人体感应后的亮灯延时应为1-600s");
        }
        return R.ok();
    }
}
