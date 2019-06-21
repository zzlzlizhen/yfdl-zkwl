package com.remote.modules.history.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.modules.history.entity.HistoryDay;
import com.remote.modules.history.entity.HistoryYear;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HistoryYearMapper extends BaseMapper<HistoryYear> {
    /*
     * @Author zhangwenping
     * @Description 按照年统计
     * @Date 18:04 2019/6/14
     * @Param deviceCode year
     * @return List<HistoryYear>
     **/
    List<HistoryYear> queryHistoryYear(@Param("deviceCode") String deviceCode, @Param("year")String year);
    /*
     * @Author zhagnwenping
     * @Description 删除5年前的数据
     * @Date 14:31 2019/6/21
     * @return int
     **/
    int deleteBatchYear();
}
