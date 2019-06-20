package com.remote.history.service;

import com.remote.history.entity.HistoryMouth;

public interface HistoryService {
    /*
     * @Author zhangwenping
     * @Description 添加历史数据
     * @Date 20:12 2019/6/14
     * @Param historyMouth
     * @return int
     **/
    int insertHistoryData(HistoryMouth historyMouth);
}
