package com.remote.history.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.history.entity.HistoryMouth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HistoryMouthMapper extends BaseMapper<HistoryMouth> {
    /*
     * @Author zhangwenping
     * @Description 修改历史数据
     * @Date 20:09 2019/6/14
     * @Param historyMouth
     * @return int
     **/
    int updateHistoryByTime(@Param("historyMouth") HistoryMouth historyMouth);
    /*
     * @Author zhangwenping
     * @Description 查询表中是否有记录
     * @Date 20:16 2019/6/14
     * @Param deviceCode time
     * @return int
     **/
    int queryHistoryMouth(@Param("deviceCode") String deviceCode,@Param("time") String time);
}
