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
import com.remote.common.validator.group.AddGroup;
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
        ValidatorUtils.validateEntity(advancedSetting, AddGroup.class);
        boolean falg = false;
        String groupId = advancedSetting.getGroupId();
        if(StringUtils.isBlank(groupId)){
            return R.error("组id不能为空");
        }
        String devCode = advancedSetting.getDeviceCode();
        if(StringUtils.isBlank(devCode)||"0".equals(devCode)&&StringUtils.isNotBlank(groupId)){
             advancedSetting.setDeviceCode("0");
            AdvancedSettingEntity advancedSettingEntity = advancedSettingService.queryByDevGroupId(advancedSetting.getDeviceCode(),groupId);
            advancedSetting.setUpdateUser(getUser().getRealName());
            advancedSetting.setUid(getUserId());
            if(advancedSettingEntity != null){
                advancedSetting.setUpdateTime(new Date());
                advancedSetting.setUpdateTime(new Date());
                int temChageMax =  advancedSetting.getTempCharge().intValue() >> 8;
                int temChageMin = advancedSetting.getTempCharge()&0xFF;
                Integer temDisChageMax = advancedSetting.getTempDisCharge() >> 8;
                Integer temDisChageMin = advancedSetting.getTempDisCharge()&0xFF;
                if(advancedSetting.getSwitchDelayTime() < 1 || advancedSetting.getSwitchDelayTime() > 120){
                    return R.error("开关灯延时时间应在1分钟到120分钟之间");
                }else if(advancedSetting.getInspectionTime() < 1 || advancedSetting.getInspectionTime()>3600){
                    return R.error("巡检时间应在1到3600分钟");
                }else if(advancedSetting.getTimeTurnOn() < 0 || advancedSetting.getTimeTurnOn() > 1440){
                    return R.error("开灯时刻应在0-24小时内");
                }else if(advancedSetting.getTimeTurnOff() < 0 || advancedSetting.getTimeTurnOff() > 1440){
                    return R.error("关灯时刻应在0-24小时内");
                }else if(advancedSetting.getTime1()<0 || advancedSetting.getTime1() > 770){
                    return R.error("一时段时常应该为0至12小时五十分钟");
                }else if(advancedSetting.getTime2()<0 || advancedSetting.getTime2() > 770){
                    return R.error("一时段时常应该为0至12小时五十分钟");
                }else if(advancedSetting.getTime3()<0 || advancedSetting.getTime3() > 770){
                    return R.error("一时段时常应该为0至12小时五十分钟");
                }else if(advancedSetting.getTime4()<0 || advancedSetting.getTime4() > 770){
                    return R.error("一时段时常应该为0至12小时五十分钟");
                } else if(advancedSetting.getTime5()<0 || advancedSetting.getTime5() > 770){
                    return R.error("一时段时常应该为0至12小时五十分钟");
                }else if(advancedSetting.getTimeDown()<0 || advancedSetting.getTimeDown() > 770){
                    return R.error("一时段时常应该为0至12小时五十分钟");
                }else if(advancedSetting.getInductionLightOnDelay() < 1 || advancedSetting.getInductionLightOnDelay()>600){
                    return R.error("人体感应后的亮灯延时应为1-600s");
                }else if(advancedSetting.getSavingSwitch()==1){
                    if(advancedSetting.getFirDownPower() > advancedSetting.getVolCharge() || advancedSetting.getFirDownPower() < advancedSetting.getVolOverDisCharge()){
                        return R.error("一阶降功率电压不能大于充电电压或者小于过放电压");
                    }else if(advancedSetting.getTwoDownPower() > advancedSetting.getFirDownPower() || advancedSetting.getTwoDownPower() < advancedSetting.getVolOverDisCharge()){
                        return R.error("二阶降功率电压不能大于一阶降功率电压或者小于过放电压");
                    }else if(advancedSetting.getThreeDownPower() > advancedSetting.getTwoDownPower() || advancedSetting.getThreeDownPower() < advancedSetting.getVolOverDisCharge()){
                        return R.error("三阶降功率电压不能大于二阶降功率电压或者小于过放电压");
                    }else if(advancedSetting.getTwoReducAmplitude() > advancedSetting.getFirReducAmplitude()){
                        return R.error("二阶降功率幅度不能大于一阶降功率幅度");
                    }else if(advancedSetting.getThreeReducAmplitude() > advancedSetting.getTwoReducAmplitude()){
                        return R.error("三阶降功率幅度不能大于二阶降功率幅度");
                    }
                }else if(advancedSetting.getAutoSleepTime() < 0 || advancedSetting.getAutoSleepTime() > 120){
                    return R.error("自动休眠延时应在0到120分钟之内");
                }else if(advancedSetting.getVpv() < 0 || advancedSetting.getVpv() > 2000){
                    return R.error("光控电压(即光电池电压)应在0~20V");
                }else if(advancedSetting.getLigntOnDuration() < 5 || advancedSetting.getLigntOnDuration()>3600){
                    return R.error("光控延时时间应在5s到1小时");
                } else if(advancedSetting.getVolOverDisCharge()<250 || advancedSetting.getVolOverDisCharge() > 4000){
                    return R.error("过放电压的范围应该在2.5V到40V之间");
                }else if(advancedSetting.getVolCharge()<250 || advancedSetting.getVolCharge() > 4000){
                    return R.error("充电电压的范围应该在2.5V到40V之间");
                }else if(advancedSetting.getICharge() < 0 || advancedSetting.getICharge() > 4000){
                    return R.error("充电电流的范围应该在0到40A之间");
                } else if(temDisChageMin < -40 || temDisChageMax > 99){
                    return R.error("放电温度范围应该在-40到99度之间");
                }else if(temChageMin < -40 || temChageMax > 99){
                    return R.error("充电电温度范围应该在-40到99度之间");
                }
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
        if(StringUtils.isBlank(groupId)||StringUtils.isBlank(deviceCode)){
            R.error("组id或设备编号不能为空");
        }
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
        ValidatorUtils.validateEntity(advancedSetting, AddGroup.class);
        boolean falg = false;
        String deviceCode = advancedSetting.getDeviceCode();
        if(StringUtils.isNotBlank(advancedSetting.getGroupId())&&StringUtils.isNotBlank(deviceCode)&&!"0".equals(deviceCode)){
            AdvancedSettingEntity advSE = advancedSettingService.queryByDeviceCode(deviceCode);
            advancedSetting.setUid(getUserId());
            advancedSetting.setUpdateUser(getUser().getRealName());
            if(advSE != null){
                advancedSetting.setUpdateTime(new Date());
                int temChageMax =  advancedSetting.getTempCharge().intValue() >> 8;
                int temChageMin = advancedSetting.getTempCharge()&0xFF;
                Integer temDisChageMax = advancedSetting.getTempDisCharge() >> 8;
                Integer temDisChageMin = advancedSetting.getTempDisCharge()&0xFF;
                if(advancedSetting.getSwitchDelayTime() < 1 || advancedSetting.getSwitchDelayTime() > 120){
                    return R.error("开关灯延时时间应在1分钟到120分钟之间");
                }else if(advancedSetting.getInspectionTime() < 1 || advancedSetting.getInspectionTime()>3600){
                    return R.error("巡检时间应在1到3600分钟");
                }else if(advancedSetting.getTimeTurnOn() < 0 || advancedSetting.getTimeTurnOn() > 1440){
                    return R.error("开灯时刻应在0-24小时内");
                }else if(advancedSetting.getTimeTurnOff() < 0 || advancedSetting.getTimeTurnOff() > 1440){
                    return R.error("关灯时刻应在0-24小时内");
                }else if(advancedSetting.getTime1()<0 || advancedSetting.getTime1() > 770){
                    return R.error("一时段时常应该为0至12小时五十分钟");
                }else if(advancedSetting.getTime2()<0 || advancedSetting.getTime2() > 770){
                    return R.error("一时段时常应该为0至12小时五十分钟");
                }else if(advancedSetting.getTime3()<0 || advancedSetting.getTime3() > 770){
                    return R.error("一时段时常应该为0至12小时五十分钟");
                }else if(advancedSetting.getTime4()<0 || advancedSetting.getTime4() > 770){
                    return R.error("一时段时常应该为0至12小时五十分钟");
                } else if(advancedSetting.getTime5()<0 || advancedSetting.getTime5() > 770){
                    return R.error("一时段时常应该为0至12小时五十分钟");
                }else if(advancedSetting.getTimeDown()<0 || advancedSetting.getTimeDown() > 770){
                    return R.error("一时段时常应该为0至12小时五十分钟");
                }else if(advancedSetting.getInductionLightOnDelay() < 1 || advancedSetting.getInductionLightOnDelay()>600){
                    return R.error("人体感应后的亮灯延时应为1-600s");
                }else if(advancedSetting.getSavingSwitch()==1){
                    if(advancedSetting.getFirDownPower() > advancedSetting.getVolCharge() || advancedSetting.getFirDownPower() < advancedSetting.getVolOverDisCharge()){
                        return R.error("一阶降功率电压不能大于充电电压或者小于过放电压");
                    }else if(advancedSetting.getTwoDownPower() > advancedSetting.getFirDownPower() || advancedSetting.getTwoDownPower() < advancedSetting.getVolOverDisCharge()){
                        return R.error("二阶降功率电压不能大于一阶降功率电压或者小于过放电压");
                    }else if(advancedSetting.getThreeDownPower() > advancedSetting.getTwoDownPower() || advancedSetting.getThreeDownPower() < advancedSetting.getVolOverDisCharge()){
                        return R.error("三阶降功率电压不能大于二阶降功率电压或者小于过放电压");
                    }else if(advancedSetting.getTwoReducAmplitude() > advancedSetting.getFirReducAmplitude()){
                        return R.error("二阶降功率幅度不能大于一阶降功率幅度");
                    }else if(advancedSetting.getThreeReducAmplitude() > advancedSetting.getTwoReducAmplitude()){
                        return R.error("三阶降功率幅度不能大于二阶降功率幅度");
                    }
                }else if(advancedSetting.getAutoSleepTime() < 0 || advancedSetting.getAutoSleepTime() > 120){
                    return R.error("自动休眠延时应在0到120分钟之内");
                }else if(advancedSetting.getVpv() < 0 || advancedSetting.getVpv() > 2000){
                    return R.error("光控电压(即光电池电压)应在0~20V");
                }else if(advancedSetting.getLigntOnDuration() < 5 || advancedSetting.getLigntOnDuration()>3600){
                    return R.error("光控延时时间应在5s到1小时");
                } else if(advancedSetting.getVolOverDisCharge()<250 || advancedSetting.getVolOverDisCharge() > 4000){
                    return R.error("过放电压的范围应该在2.5V到40V之间");
                }else if(advancedSetting.getVolCharge()<250 || advancedSetting.getVolCharge() > 4000){
                    return R.error("充电电压的范围应该在2.5V到40V之间");
                }else if(advancedSetting.getICharge() < 0 || advancedSetting.getICharge() > 4000){
                    return R.error("充电电流的范围应该在0到40A之间");
                } else if(temDisChageMin < -40 || temDisChageMax > 99){
                    return R.error("放电温度范围应该在-40到99度之间");
                }else if(temChageMin < -40 || temChageMax > 99){
                    return R.error("充电电温度范围应该在-40到99度之间");
                }
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

        return R.ok("");
    }
}
