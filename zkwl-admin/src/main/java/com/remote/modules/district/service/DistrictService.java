package com.remote.modules.district.service;

import com.remote.modules.district.entity.DistrictEntity;

import java.util.List;

public interface DistrictService {
    /*
     * @Author zhangwenping
     * @Description 根据等级查询城市信息
     * @Date 13:19 2019/7/29
     * @Param type
     * @return List<DistrictEntity>
     **/
    List<DistrictEntity> queryDistrictByType(Integer type);
    /*
     * @Author zhangwenping
     * @Description 根据城市id查询城市详情
     * @Date 13:38 2019/7/29
     * @Param id
     * @return DistrictEntity
     **/
    DistrictEntity queryDistrictById(Integer id);
}
