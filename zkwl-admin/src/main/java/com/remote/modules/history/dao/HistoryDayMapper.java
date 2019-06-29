package com.remote.modules.history.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.modules.history.entity.HistoryDay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HistoryDayMapper extends BaseMapper<HistoryDay> {
    /*
     * @Author zhangwenping
     * @Description 查询分组信息
     * @Date 14:30 2019/6/14
     * @Param deviceCode time
     * @return List<HistoryDay>
     **/
    List<HistoryDay> queryHistoryDay(@Param("deviceCode") String deviceCode, @Param("time")String time);
    /*
     * @Author zhagnwenping
     * @Description 删除一个月前的数据
     * @Date 14:31 2019/6/21
     * @return int
     **/
    int deleteBatchDay();

    /*
     * @Author zhangwenping
     * @Description 查询充满次数，过放次数
     * @Date 14:30 2019/6/14
     * @Param deviceCode time
     * @return List<HistoryDay>
     **/
    List<HistoryDay> queryHistoryDayOther(@Param("deviceCode") String deviceCode, @Param("time")String time);
}
