package com.remote.modules.advancedsetting.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.remote.common.utils.CollectionUtils;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.R;
import com.remote.common.utils.StringUtils;
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
                R r = initAdvSetEnt(advancedSetting);
                if(!r.isOK()){
                    return R.error(r);
                }
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
                R r = initAdvSetEnt(advancedSetting);
                if(!r.isOK()){
                    return R.error(r);
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
        int temChageMax =  advSet.getTempCharge().intValue() >> 8;

        int temChageMin = advSet.getTempCharge()&0xFF;
         byte teMin = (byte) temChageMin;
        Integer temDisChageMax = advSet.getTempDisCharge() >> 8;
        Integer temDisChageMin = advSet.getTempDisCharge()&0xFF;
        if(advSet.getSwitchDelayTime() < 1 || advSet.getSwitchDelayTime() > 120){
            return R.error("开关灯延时时间应在1分钟到120分钟之间");
        }else if(advSet.getInspectionTime() < 1 || advSet.getInspectionTime()>3600){
            return R.error("巡检时间应在1到3600分钟");
        }else if(advSet.getTimeTurnOn() < 0 || advSet.getTimeTurnOn() > 1440){
           return R.error("开灯时刻应在0-24小时内");
       }else if(advSet.getTimeTurnOff() < 0 || advSet.getTimeTurnOff() > 1440){
            return R.error("关灯时刻应在0-24小时内");
        }else if(advSet.getTime1()<0 || advSet.getTime1() > 770){
            return R.error("一时段时常应该为0至12小时五十分钟");
        }else if(advSet.getTime2()<0 || advSet.getTime2() > 770){
            return R.error("一时段时常应该为0至12小时五十分钟");
        }else if(advSet.getTime3()<0 || advSet.getTime3() > 770){
           return R.error("一时段时常应该为0至12小时五十分钟");
        }else if(advSet.getTime4()<0 || advSet.getTime4() > 770){
           return R.error("一时段时常应该为0至12小时五十分钟");
        } else if(advSet.getTime5()<0 || advSet.getTime5() > 770){
           return R.error("一时段时常应该为0至12小时五十分钟");
        }else if(advSet.getTimeDown()<0 || advSet.getTimeDown() > 770){
            return R.error("一时段时常应该为0至12小时五十分钟");
        }else if(advSet.getInductionLightOnDelay() < 1 || advSet.getInductionLightOnDelay()>600){
           return R.error("人体感应后的亮灯延时应为1-600s");
        }else if(advSet.getSavingSwitch()==1){
            if(advSet.getFirDownPower() > advSet.getVolCharge() || advSet.getFirDownPower() < advSet.getVolOverDisCharge()){
                return R.error("一阶降功率电压不能大于充电电压或者小于过放电压");
            }else if(advSet.getTwoDownPower() > advSet.getFirDownPower() || advSet.getTwoDownPower() < advSet.getVolOverDisCharge()){
                return R.error("二阶降功率电压不能大于一阶降功率电压或者小于过放电压");
            }else if(advSet.getThreeDownPower() > advSet.getTwoDownPower() || advSet.getThreeDownPower() < advSet.getVolOverDisCharge()){
                return R.error("三阶降功率电压不能大于二阶降功率电压或者小于过放电压");
            }else if(advSet.getTwoReducAmplitude() > advSet.getFirReducAmplitude()){
                return R.error("二阶降功率幅度不能大于一阶降功率幅度");
            }else if(advSet.getThreeReducAmplitude() > advSet.getTwoReducAmplitude()){
                return R.error("三阶降功率幅度不能大于二阶降功率幅度");
            }
        }else if(advSet.getAutoSleepTime() < 0 || advSet.getAutoSleepTime() > 120){
            return R.error("自动休眠延时应在0到120分钟之内");
        }else if(advSet.getVpv() < 0 || advSet.getVpv() > 2000){
            return R.error("光控电压(即光电池电压)应在0~20V");
        }else if(advSet.getLigntOnDuration() < 5 || advSet.getLigntOnDuration()>3600){
            return R.error("开关灯延时时间应在5s到1小时");
        } else if(advSet.getVolOverDisCharge()<250 || advSet.getVolOverDisCharge() > 4000){
            return R.error("过放电压的范围应该在2.5V到40V之间");
        }else if(advSet.getVolCharge()<250 || advSet.getVolCharge() > 4000){
            return R.error("充电电压的范围应该在2.5V到40V之间");
        }else if(advSet.getICharge() < 0 || advSet.getICharge() > 4000){
            return R.error("充电电流的范围应该在0到40A之间");
        } else if(temDisChageMin < -40 || temDisChageMax > 99){
            return R.error("放电温度范围应该在-40到99度之间");
        }else if(temChageMin < -40 || temChageMax > 99){
            return R.error("充电电温度范围应该在-40到99度之间");
        }
        return R.ok("");
    }
}
