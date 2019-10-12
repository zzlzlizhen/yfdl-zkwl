package com.remote.modules.history.service;

import com.remote.modules.history.entity.QueryHistory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface HistoryService {
    /*
     * @Author zhangwenping
     * @Description 按小时统计历史数据
     * @Date 14:44 2019/6/14
     * @Param time
     * @return QueryHistory
     **/
    QueryHistory queryHistoryDay(String deviceCode,String time);
    /*
     * @Author zhangwenping
     * @Description 按照天数统计历史数据
     * @Date 17:56 2019/6/14
     * @Param deviceCode year month
     * @return QueryHistory
     **/
    QueryHistory queryHistoryMouth(String deviceCode,String year,String month);
    /*
     * @Author zhangwenping
     * @Description 按照年统计历史数据
     * @Date 18:07 2019/6/14
     * @Param deviceCode year
     * @return QueryHistory
     **/
    QueryHistory queryHistoryYear(String deviceCode, String year);
    /*
     * @Author zhagnwenping
     * @Description 删除一个月前的数据
     * @Date 14:31 2019/6/21
     * @return int
     **/
    int deleteBatchDay();
    /*
     * @Author zhagnwenping
     * @Description 删除一年前的数据
     * @Date 14:31 2019/6/21
     * @return int
     **/
    int deleteBatchMonth();
    /*
     * @Author zhagnwenping
     * @Description 删除5年前的数据
     * @Date 14:31 2019/6/21
     * @return int
     **/
    int deleteBatchYear();
    /**
     * 通过当前用户的所有设备codes获取总放电量
     * */
    Double getTotalDischarge(List<String> deviceCode);
    /**
     * 通过用户ids获取每月总放电量
     * */
    List<Map<Object,Object>> getDischargeCapacity(List<String> deviceCodes);

}
