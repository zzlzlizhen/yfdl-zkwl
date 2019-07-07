package com.remote.modules.overview.entity;

import com.remote.modules.device.entity.DeviceEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class OverViewEntity {
    /**
     * 路灯数
     * */
    private Integer lampsNum;
    /**
     * 网关数
     * */
    private Integer gatewaysNum;
    /**
     * 照明面积
     * */
    private double lightingArea;
    /**
     * 降低碳排放量
     * */
    private double reduCarbonEmi;
    /**
     * 故障率
     * */
    private String FailRate;
    List<Map<String,Integer>> cuntGroupByCity;
    List<DeviceEntity> deviceInfoList;

    public Integer getLampsNum() {
        return lampsNum;
    }

    public void setLampsNum(Integer lampsNum) {
        this.lampsNum = lampsNum;
    }

    public Integer getGatewaysNum() {
        return gatewaysNum;
    }

    public void setGatewaysNum(Integer gatewaysNum) {
        this.gatewaysNum = gatewaysNum;
    }

    public double getLightingArea() {
        return lightingArea;
    }

    public void setLightingArea(double lightingArea) {
        this.lightingArea = lightingArea;
    }

    public double getReduCarbonEmi() {
        return reduCarbonEmi;
    }

    public void setReduCarbonEmi(double reduCarbonEmi) {
        this.reduCarbonEmi = reduCarbonEmi;
    }

    public String getFailRate() {
        return FailRate;
    }

    public void setFailRate(String failRate) {
        FailRate = failRate;
    }

    public List<Map<String, Integer>> getCuntGroupByCity() {
        return cuntGroupByCity;
    }

    public void setCuntGroupByCity(List<Map<String, Integer>> cuntGroupByCity) {
        this.cuntGroupByCity = cuntGroupByCity;
    }

    public List<DeviceEntity> getDeviceInfoList() {
        return deviceInfoList;
    }

    public void setDeviceInfoList(List<DeviceEntity> deviceInfoList) {
        this.deviceInfoList = deviceInfoList;
    }
}
