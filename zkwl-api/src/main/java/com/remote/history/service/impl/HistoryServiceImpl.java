package com.remote.history.service.impl;

import com.remote.history.dao.HistoryDayMapper;
import com.remote.history.dao.HistoryMouthMapper;
import com.remote.history.dao.HistoryYearMapper;
import com.remote.history.entity.HistoryDay;
import com.remote.history.entity.HistoryMouth;
import com.remote.history.entity.HistoryYear;
import com.remote.history.service.HistoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author zhangwenping
 * @Date 2019/6/14 13:27
 * @Version 1.0
 **/
@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryMouthMapper historyMouthMapper;

    @Autowired
    private HistoryDayMapper historyDayMapper;

    @Autowired
    private HistoryYearMapper historyYearMapper;

    @Override
    public int insertHistoryData(HistoryMouth historyMouth) {
        historyMouth.setCreateTime(new Date());
        HistoryDay historyDay = new HistoryDay();
        BeanUtils.copyProperties(historyMouth, historyDay);
        historyDay.setDayId(UUID.randomUUID().toString());
        //添加日表数据
        int insert = historyDayMapper.insert(historyDay);
        if(insert > 0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String time = sdf.format(historyMouth.getCreateTime());
            historyMouth.setMouthId(UUID.randomUUID().toString());
            List<HistoryMouth> historyMouths = historyMouthMapper.queryHistoryMouth(historyMouth.getDeviceCode(), time);
            HistoryYear historyYear = new HistoryYear();
            BeanUtils.copyProperties(historyMouth, historyYear);
            historyYear.setYearId(UUID.randomUUID().toString());
            //代表月表中有数据
            if(historyMouths != null && historyMouths.size() > 0){
                historyMouth.setTime(time);
                //代表今天已经上报过历史数据，去修改月表中数据
                historyMouth.setMouthId(historyMouths.get(0).getMouthId());
                int history = historyMouthMapper.updateHistoryByTime(historyMouth);
                //月表修改成功，查询年表中数据，如果有历史数据，修改年表数据
                if(history > 0){
                    Date date = historyMouth.getCreateTime();
                    String year=String.format("%tY", date);
                    String mon=String .format("%tm", date);
                    List<HistoryYear> historyYears = historyYearMapper.queryHistoryYear(historyYear.getDeviceCode(), year, mon);
                    if(historyYears != null && historyYears.size() > 0){
                        historyYear.setYear(year);
                        historyYear.setMonth(mon);
                        historyYear.setYearId(historyYears.get(0).getYearId());
                        return historyYearMapper.updateHistroyByCode(historyYear);
                    }else{
                        return historyYearMapper.insert(historyYear);
                    }
                }else{
                    return historyYearMapper.insert(historyYear);
                }
            }else{
                int insert1 = historyMouthMapper.insert(historyMouth);
                return historyYearMapper.insert(historyYear);
            }
        }
        return 0;
    }
}
