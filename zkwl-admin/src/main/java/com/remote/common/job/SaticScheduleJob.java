package com.remote.common.job;

import com.remote.modules.history.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @Author zhangwenping
 * @Date 2019/6/21 14:15
 * @Version 1.0
 **/
@Component
@Configuration
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleJob {
    private Logger logger = LoggerFactory.getLogger(SaticScheduleJob.class);

    @Autowired
    private HistoryService historyService;
    //3.添加定时任务
    //每天中午12点执行
    @Scheduled(cron = "0 0 12 * * ? ")
    //测试使用
    //@Scheduled(cron = "0/5 * * * * ?")
    private void configureTasks() {
        logger.info("定时任务开始执行----------start");
        //删除一个月前的 历史数据
        int i = historyService.deleteBatchDay();
        //删除一年前 历史数据
        historyService.deleteBatchMonth();
        //删除五年前 历史数据
        historyService.deleteBatchYear();
        logger.info("定时任务执行结束----------end");
    }
}
