package com.remote.history.dao;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.history.entity.HistoryYear;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface HistoryYearMapper extends BaseMapper<HistoryYear> {
    /*
     * @Author zhangwenping
     * @Description 查询年表统计
     * @Date 9:10 2019/6/17
     * @Param deviceCode year month
     * @return int
     **/
    int queryHistoryYear(@Param("deviceCode") String deviceCode, @Param("year") String year, @Param("month") String month);
    /*
     * @Author zhangwenping
     * @Description 修改年表统计数据
     * @Date 9:12 2019/6/17
     * @Param historyYear
     * @return int
     **/
    int updateHistroyByCode(@Param("historyYear") HistoryYear historyYear);

}
