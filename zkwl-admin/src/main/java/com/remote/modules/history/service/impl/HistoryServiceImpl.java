package com.remote.modules.history.service.impl;

import com.remote.modules.history.dao.HistoryDayMapper;
import com.remote.modules.history.dao.HistoryMouthMapper;
import com.remote.modules.history.dao.HistoryYearMapper;
import com.remote.modules.history.entity.HistoryDay;
import com.remote.modules.history.entity.HistoryMouth;
import com.remote.modules.history.entity.HistoryYear;
import com.remote.modules.history.entity.QueryHistory;
import com.remote.modules.history.service.HistoryService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/6/14 14:11
 * @Version 1.0
 **/
@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryDayMapper historyDayMapper;

    @Autowired
    private HistoryMouthMapper historyMouthMapper;

    @Autowired
    private HistoryYearMapper historyYearMapper;

    @Override
    public QueryHistory queryHistoryDay(String deviceCode,String time) {
        QueryHistory queryHistory = new QueryHistory();
        List<HistoryDay> historyDays = historyDayMapper.queryHistoryDay(deviceCode,time);
        if(CollectionUtils.isNotEmpty(historyDays)){
            for (HistoryDay historyDay : historyDays){
                queryHistory.getDischargeCapacityList().add(historyDay.getDischargeCapacity());
                queryHistory.getChargingCapacityList().add(historyDay.getChargingCapacity());
                queryHistory.getChargingCurrentList().add(historyDay.getChargingCurrent());
                queryHistory.getDischargeCurrentList().add(historyDay.getDischargeCurrent());
                queryHistory.getBatteryVoltageList().add(historyDay.getBatteryVoltage());
                queryHistory.getAmbientTemperatureList().add(historyDay.getAmbientTemperature());
                queryHistory.getInternalTemperatureList().add(historyDay.getInternalTemperature());
                queryHistory.getVisitorsFlowrateList().add(historyDay.getVisitorsFlowrate());
                queryHistory.getInductionFrequencyList().add(historyDay.getInductionFrequency());
                queryHistory.getMeteorologicalList().add(historyDay.getMeteorological());
            }
        }
        return queryHistory;
    }

    @Override
    public QueryHistory queryHistoryMouth(String deviceCode,String year,String month) {
        QueryHistory queryHistory = new QueryHistory();
        List<HistoryMouth> historyMouths = historyMouthMapper.queryHistoryMouth(deviceCode, year, month);
        if(CollectionUtils.isNotEmpty(historyMouths)){
            for (HistoryMouth historyMouth : historyMouths){
                queryHistory.getDischargeCapacityList().add(historyMouth.getDischargeCapacity());
                queryHistory.getChargingCapacityList().add(historyMouth.getChargingCapacity());
                queryHistory.getChargingCurrentList().add(historyMouth.getChargingCurrent());
                queryHistory.getDischargeCurrentList().add(historyMouth.getDischargeCurrent());
                queryHistory.getBatteryVoltageList().add(historyMouth.getBatteryVoltage());
                queryHistory.getAmbientTemperatureList().add(historyMouth.getAmbientTemperature());
                queryHistory.getInternalTemperatureList().add(historyMouth.getInternalTemperature());
                queryHistory.getVisitorsFlowrateList().add(historyMouth.getVisitorsFlowrate());
                queryHistory.getInductionFrequencyList().add(historyMouth.getInductionFrequency());
                queryHistory.getMeteorologicalList().add(historyMouth.getMeteorological());
            }
        }
        return queryHistory;
    }

    @Override
    public QueryHistory queryHistoryYear(String deviceCode, String year) {
        QueryHistory queryHistory = new QueryHistory();
        List<HistoryYear> historyYears = historyYearMapper.queryHistoryYear(deviceCode, year);
        if(CollectionUtils.isNotEmpty(historyYears)){
            for (HistoryYear historyYear : historyYears){
                queryHistory.getDischargeCapacityList().add(historyYear.getDischargeCapacity());
                queryHistory.getChargingCapacityList().add(historyYear.getChargingCapacity());
                queryHistory.getChargingCurrentList().add(historyYear.getChargingCurrent());
                queryHistory.getDischargeCurrentList().add(historyYear.getDischargeCurrent());
                queryHistory.getBatteryVoltageList().add(historyYear.getBatteryVoltage());
                queryHistory.getAmbientTemperatureList().add(historyYear.getAmbientTemperature());
                queryHistory.getInternalTemperatureList().add(historyYear.getInternalTemperature());
                queryHistory.getVisitorsFlowrateList().add(historyYear.getVisitorsFlowrate());
                queryHistory.getInductionFrequencyList().add(historyYear.getInductionFrequency());
            }
        }
        return queryHistory;
    }
}
