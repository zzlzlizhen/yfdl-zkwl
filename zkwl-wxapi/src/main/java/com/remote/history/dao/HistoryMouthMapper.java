package com.remote.history.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.history.entity.HistoryDay;
import com.remote.history.entity.HistoryMouth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HistoryMouthMapper extends BaseMapper<HistoryDay> {
    /*
     * @Author zhangwenping
     * @Description 月统计
     * @Date 17:55 2019/6/14
     * @Param deviceCode year month
     * @return List<HistoryMouth>
     **/
    List<HistoryMouth> queryHistoryMouth(@Param("deviceCode") String deviceCode, @Param("year") String year, @Param("month") String month);

    /*
     * @Author zhangwenping
     * @Description 删除一年前数据
     * @Date 14:56 2019/6/21
     * @return int
     **/
    int deleteBatchMouth();
}
