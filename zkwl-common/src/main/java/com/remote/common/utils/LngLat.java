package com.remote.common.utils;

import lombok.Data;

/**
 * @Author zhangwenping
 * @Date 2019/7/30 9:59
 * @Version 1.0
 **/
@Data
public class LngLat {
    private double longitude;//经度
    private double lantitude;//维度

    public LngLat() {
    }

    public LngLat(double longitude, double lantitude) {
        this.longitude = longitude;
        this.lantitude = lantitude;
    }

}
