package com.remote.modules.advancedsetting.controller;

import com.remote.common.errorcode.ErrorCode;
import com.remote.common.errorcode.ErrorMsg;
import com.remote.common.utils.R;
import com.remote.common.utils.StringUtils;
import com.remote.common.validator.ValidatorUtils;
import com.remote.common.validator.group.AddGroup;
import com.remote.modules.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.modules.advancedsetting.entity.AdvancedSettingResult;
import com.remote.modules.advancedsetting.service.AdvancedSettingService;
import com.remote.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 功能描述：
 * @author zsm
 * @email 1648925727@qq.com
 * @date 2019-06-20 09:22:19
 */
@RestController
@RequestMapping("/advancedsetting")
public class AdvancedSettingController extends AbstractController{
    @Autowired
    private AdvancedSettingService advancedSettingService;

    private Class asc = AdvancedSettingController.class;
    /**
     * 查询设备的信息时，必须传组id跟设备code
     */
    @RequestMapping(value = "/queryDevAdvInfo",method = RequestMethod.GET)
    public R queryDevAdvInfo(String groupId,String deviceCode){

        if(StringUtils.isBlank(groupId) || StringUtils.isBlank(deviceCode) || "undefined".equals(groupId)||"undefined".equals(deviceCode)){
            return ErrorMsg.errorMsg(asc,ErrorCode.NOT_EMPTY,"组id跟设备编号不能为空");
        }
        AdvancedSettingEntity advancedSettingEntity = null;
        try {
            advancedSettingEntity = advancedSettingService.queryByDevOrGroupId(groupId.trim(),deviceCode.trim());
            if(advancedSettingEntity == null){
                ErrorMsg.errorMsg(asc,ErrorCode.DATA_QUERY_CHECKING,"查询设备高级设置信息为空");
            }
        }catch (Exception e){
            return ErrorMsg.errorMsg(asc,ErrorCode.ABNORMAL,"查询设备高级设置信息异常");
        }
        return R.ok().put("info",advancedSettingEntity);

    }

    /**
     * 组高级设置信息查看接口
     */
    @RequestMapping(value = "/settingInfo",method = RequestMethod.GET)
    public R info(String groupId){

        if (StringUtils.isBlank(groupId)||"undefined".equals(groupId)){
            return ErrorMsg.errorMsg(asc,ErrorCode.NOT_EMPTY,"组id不能为空");
        }
        AdvancedSettingEntity advancedSettingEntity = null;
        try {
            advancedSettingEntity = advancedSettingService.queryByDevOrGroupId(groupId,"0");
        }catch (Exception e){
            e.printStackTrace();
            return ErrorMsg.errorMsg(asc,ErrorCode.ABNORMAL,"查询组的高级设置信息异常");
        }
        return R.ok().put("info",advancedSettingEntity);
    }



    /**
     * 修改组的高级设置
     */
    @RequestMapping(value = "/updateGroup",method = RequestMethod.POST)
    public R update(AdvancedSettingEntity advancedSetting){
        ValidatorUtils.validateEntity(advancedSetting, AddGroup.class);
        String groupId = advancedSetting.getGroupId();

        if(StringUtils.isBlank(groupId)||"undefined".equals(groupId)){
            return ErrorMsg.errorMsg(asc,ErrorCode.NOT_EMPTY,"组id不能为空2");
        }
        String msg = volatileData(advancedSetting);
        if(!"".equals(msg)){
            return R.error(msg);
        }
        try {
            advancedSettingService.addUpdateGroup(advancedSetting,getUser());
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            return  ErrorMsg.errorMsg(asc,ErrorCode.ABNORMAL,"数据保存失败");

        }
    }

    /**
     * 更改设备高级设置 组id跟设备code必传
     */
    @RequestMapping(value = "/updateDevice",method = RequestMethod.POST)
    public R updateDevice(AdvancedSettingEntity advancedSetting){
        ValidatorUtils.validateEntity(advancedSetting, AddGroup.class);
        String deviceCode = advancedSetting.getDeviceCode();

        if((StringUtils.isBlank(deviceCode) && "0".equals(deviceCode))||"undefined".equals(advancedSetting.getDeviceCode())||"undefined".equals(advancedSetting.getGroupId()) || StringUtils.isBlank(advancedSetting.getGroupId())){
            return  ErrorMsg.errorMsg(asc,ErrorCode.NOT_EMPTY,"设备编号或组不能为空");
        }
        String msg = volatileData(advancedSetting);
        if(!"".equals(msg)){
            return  ErrorMsg.errorMsg(asc,ErrorCode.NOT_EMPTY,msg);
        }
        try {
            advancedSettingService.addUpdateDevice(advancedSetting,getUser());
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            return  ErrorMsg.errorMsg(asc,ErrorCode.ABNORMAL,"数据保存失败");
        }
    }


    public String volatileData(AdvancedSettingEntity advancedSetting){
        advancedSetting.setUpdateTime(new Date());
        int temChageMin=  (advancedSetting.getTempCharge().intValue() >> 8) & 0xFF;
        if(temChageMin > 127){
            temChageMin |= 0xFFFFFF00;
        }
        int temChageMax = advancedSetting.getTempCharge()&0xFF;
        Integer temDisChageMin = (advancedSetting.getTempDisCharge() >> 8) & 0xFF;
        if(temDisChageMin > 127){
            temDisChageMin |= 0xFFFFFF00;
        }
        Integer temDisChageMax = advancedSetting.getTempDisCharge()&0xFF;
        if(advancedSetting.getSwitchDelayTime() < 0 || advancedSetting.getSwitchDelayTime() > 120){
            return "开关灯延时时间应在1分钟到120分钟之间";
        }else if(advancedSetting.getInspectionTime() < 0 || advancedSetting.getInspectionTime()>3600){
            return "巡检时间应在1到3600分钟";
        }else if(advancedSetting.getTimeTurnOn() < 0 || advancedSetting.getTimeTurnOn() > 1440){
            return "开灯时刻应在0-24小时内";
        }else if(advancedSetting.getTimeTurnOff() < 0 || advancedSetting.getTimeTurnOff() >1440){
            return "关灯时刻应在0-24小时内";
        }else if(advancedSetting.getTime1()<0 || advancedSetting.getTime1() > 770){
            return "一时段时常应该为0至12小时五十分钟";
        }else if(advancedSetting.getTime2()<0 || advancedSetting.getTime2() >770){
            return "一时段时常应该为0至12小时五十分钟";
        }else if(advancedSetting.getTime3()<0 || advancedSetting.getTime3() > 770){
            return "一时段时常应该为0至12小时五十分钟";
        }else if(advancedSetting.getTime4()<0 || advancedSetting.getTime4() > 770){
            return "一时段时常应该为0至12小时五十分钟";
        } else if(advancedSetting.getTime5()<0 || advancedSetting.getTime5() > 770){
            return "一时段时常应该为0至12小时五十分钟";
        }else if(advancedSetting.getTimeDown()<0 || advancedSetting.getTimeDown() > 770){
            return "一时段时常应该为0至12小时五十分钟";
        }else if(advancedSetting.getInductionLightOnDelay() < 1 || advancedSetting.getInductionLightOnDelay()>600){
            return "人体感应后的亮灯延时应为1-600s";
        }else if(advancedSetting.getSavingSwitch()==1){
            if(advancedSetting.getFirDownPower() > advancedSetting.getVolCharge() || advancedSetting.getFirDownPower() < advancedSetting.getVolOverDisCharge()){
                return "一阶降功率电压不能大于充电电压或者小于过放电压";
            }else if(advancedSetting.getTwoDownPower() > advancedSetting.getFirDownPower() || advancedSetting.getTwoDownPower() < advancedSetting.getVolOverDisCharge()){
                return "二阶降功率电压不能大于一阶降功率电压或者小于过放电压";
            }else if(advancedSetting.getThreeDownPower() > advancedSetting.getTwoDownPower() || advancedSetting.getThreeDownPower() < advancedSetting.getVolOverDisCharge()){
                return "三阶降功率电压不能大于二阶降功率电压或者小于过放电压";
            }else if(advancedSetting.getTwoReducAmplitude() > advancedSetting.getFirReducAmplitude()){
                return "二阶降功率幅度不能大于一阶降功率幅度";
            }else if(advancedSetting.getThreeReducAmplitude() > advancedSetting.getTwoReducAmplitude()){
                return "三阶降功率幅度不能大于二阶降功率幅度";
            }
        }else if(advancedSetting.getAutoSleepTime() < 0 || advancedSetting.getAutoSleepTime() > 120){
            return "自动休眠延时应在0到120分钟之内";
        }else if(advancedSetting.getVpv() < 0 || advancedSetting.getVpv() > 2000){
            return "光控电压(即光电池电压)应在0~20V";
        }else if(advancedSetting.getLigntOnDuration() < 5 || advancedSetting.getLigntOnDuration()>3600){
            return "光控延时时间应在5s到1小时";
        } else if(advancedSetting.getVolOverDisCharge()<250 || advancedSetting.getVolOverDisCharge() > 4000){
            return "过放电压的范围应该在2.5V到40V之间";
        }else if(advancedSetting.getVolCharge()<250 || advancedSetting.getVolCharge() > 4000){
            return "充电电压的范围应该在2.5V到40V之间";
        }else if(advancedSetting.getICharge() < 0 || advancedSetting.getICharge() > 4000){
            return "充电电流的范围应该在0到40A之间";
        }else if(temDisChageMin < -40|| temDisChageMin >99 || temDisChageMax < -40 ||temDisChageMax > 99 ){
            if(temDisChageMin > temDisChageMax){
                return "放电温度的最小值不能比放电温度最大值大";
            }
            return "放电温度范围应该在-40到99度之间";
        }else if(temChageMin < -40 || temChageMin >99 || temChageMax < -40 || temChageMax > 99){
            if(temChageMin > temChageMax){
                return "充电电温度的最小值不能比充电电温度最大值大";
            }
            return "充电电温度范围应该在-40到99度之间";
        }
        return "";
    }
    /**
     * 通过设备code查询充电电压跟放电电压
     * */
    @RequestMapping(value = "/queryVol",method = RequestMethod.GET)
    public R queryVol(@RequestParam("deviceCode")String deviceCode){
        if(StringUtils.isBlank(deviceCode)||"undefined".equals(deviceCode)){
            return  ErrorMsg.errorMsg(asc,ErrorCode.NOT_EMPTY,"设备code不能为空");
        }
        try {
            AdvancedSettingResult result = advancedSettingService.queryVol(deviceCode);
            return R.ok().put("result",result);
        }catch (Exception e){
            logger.error(ErrorCode.ABNORMAL+"updateGroup error:",e);
            return R.error("数据保存失败");
        }
    }

}
