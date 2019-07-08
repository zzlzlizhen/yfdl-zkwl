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
    /**
     * 各城市路灯数
     * */
    List<Map<Object,Object>> cuntGroupByCity;
    List<Map<Object,Object>> deviceInfoList;
    /**
     * 每月放电量
     * */
    List<Map<Object,Object>> totalDc;

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

    public List<Map<Object, Object>> getCuntGroupByCity() {
        return cuntGroupByCity;
    }

    public void setCuntGroupByCity(List<Map<Object, Object>> cuntGroupByCity) {
        this.cuntGroupByCity = cuntGroupByCity;
    }

    public List<Map<Object, Object>> getDeviceInfoList() {
        return deviceInfoList;
    }

    public void setDeviceInfoList(List<Map<Object, Object>> deviceInfoList) {
        this.deviceInfoList = deviceInfoList;
    }

    public List<Map<Object, Object>> getTotalDc() {
        return totalDc;
    }

    public void setTotalDc(List<Map<Object, Object>> totalDc) {
        this.totalDc = totalDc;
    }
}
