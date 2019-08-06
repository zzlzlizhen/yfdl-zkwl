package com.remote.district.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author zhangwenping
 * @Date 2019/7/29 13:16
 * @Version 1.0
 **/
@Data
@TableName("fun_district")
public class DistrictEntity {

    private Integer id;

    private Integer pid;

    private String districtName;

    private Integer type;

    private Integer hierarchy;

    private String districtSqe;
}
