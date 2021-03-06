package com.remote.modules.overview.controller;

import com.remote.common.utils.CollectionUtils;
import com.remote.common.utils.R;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.service.DeviceService;
import com.remote.modules.history.service.HistoryService;
import com.remote.modules.overview.entity.OverViewEntity;
import com.remote.modules.sys.controller.AbstractController;

import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/overview")
public class OverViewController extends AbstractController{
    @Autowired
    DeviceService deviceService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    HistoryService historyService;
    /**
     * 总览接口
     * */
    @RequestMapping(value = "/overview")
    @ResponseBody
    public R getOverview(){
        List<SysUserEntity> sysUserEntities = sysUserService.queryAllChild(getUser());
        List<Long> userIds = new ArrayList<Long>();
        OverViewEntity overViewEntity = new OverViewEntity();
        if(CollectionUtils.isEmpty(sysUserEntities)){
           return R.ok().put("info","");
        }
        for(SysUserEntity sysUserEntity: sysUserEntities){
            userIds.add(sysUserEntity.getUserId());
        }
        List<String> deviceCodes = deviceService.getDeviceCode(userIds);
        DecimalFormat df = new DecimalFormat("#.000");
        Integer lampsNum = getDeviceCount(userIds);
        overViewEntity.setLampsNum(lampsNum);
        overViewEntity.setGatewaysNum(0);
        overViewEntity.setFailRate((getDevFailRate(userIds)*100) + "%");
        double lightArea = getLightArea(userIds)*lampsNum;
        String lightAreaStr = df.format(lightArea);
        overViewEntity.setLightingArea(Double.parseDouble(lightAreaStr));
        //总放电量（碳排放量）
        double reduCarEmi = getTotalDischarge(deviceCodes)*0.875 ;
        String str = df.format(reduCarEmi);
        overViewEntity.setReduCarbonEmi(Double.parseDouble(str));
        overViewEntity.setCuntGroupByCity(queryCountGroupByCity());
        overViewEntity.setDeviceInfoList(getDeviceInfoList(userIds));
        overViewEntity.setTotalDc(getDischargeCapacity(deviceCodes));
        return R.ok().put("info",overViewEntity);
    }
    /**
     * 通过当前用户id查询所有的子用户下的设备数量
     * */
    public Integer getDeviceCount(List<Long> userIds){
        return deviceService.getDeviceCount(userIds);
    }
    /**
     * 通过当前用户查询所有子用户的故障率
     * */
    public double getDevFailRate(List<Long> userIds){
        Integer failRate = deviceService.getDevFailNum(userIds);
        Integer getTotal = getDeviceCount(userIds);
        if(getTotal != null&&getTotal != 0){
            return failRate/getTotal;
        }
        return 0.0;
    }
    /**
     * 取照明面积
     * */
    public double getLightArea(List<Long> userIds){
       double lightArea = 1 - getDevFailRate(userIds);
       return lightArea * 15;
    }
    /**
     * 通过设备codes获取当前设备的放电量
     * */
    public double getTotalDischarge(List<String> deviceCodes){
        Double chage = historyService.getTotalDischarge(deviceCodes);
        if(chage != null){
            return chage;
        }else{
            return 0;
        }
    }
    /**
     *获取当前用户
     * */
    List<Map<Object,Object>> queryCountGroupByCity(){
        return  deviceService.queryCountGroupByCity(getUserId());
    }

    /**
     * 获取所有的设备信息
     * */
    List<Map<Object,Object>> getDeviceInfoList(List<Long> userIds){
        return deviceService.getDeviceInfoList(userIds);
    }
    /**
     * 获取所有的放电量
     * */
    List<Map<Object,Object>> getDischargeCapacity(List<String> deviceCodes){
        return historyService.getDischargeCapacity(deviceCodes);
    }

}
