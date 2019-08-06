package com.remote.modules.district.dao;

import com.remote.modules.district.entity.DistrictEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistrictMapper {
    /*
     * @Author zhangwenping
     * @Description 根据等级查询城市信息
     * @Date 13:21 2019/7/29
     * @Param type
     * @return List<DistrictEntity>
     **/
    List<DistrictEntity> queryDistrictByType(@Param("type") Integer type);
    /*
     * @Author zhangwenping
     * @Description 根据城市id查询城市详情
     * @Date 13:39 2019/7/29
     * @Param id
     * @return DistrictEntity
     **/
    DistrictEntity queryDistrictById(@Param("id")Integer id);
}
