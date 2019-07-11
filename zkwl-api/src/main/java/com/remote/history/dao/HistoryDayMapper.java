package com.remote.history.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.history.entity.HistoryDay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HistoryDayMapper extends BaseMapper<HistoryDay> {

    /*
     * @Author zhangwenping
     * @Description 取一天的记录数
     * @Date 13:43 2019/7/11
     * @Param deviceCode,time
     * @return int
     **/
    int countHistoryDayByDeviceCode(@Param("deviceCode")String deviceCode, @Param("time")String time);
    /*
     * @Author zhangwenping
     * @Description 统计一天的数据
     * @Date 14:32 2019/7/11
     * @Param
     * @return
     **/
    HistoryDay queryDay(@Param("deviceCode")String deviceCode, @Param("time")String time);

}
