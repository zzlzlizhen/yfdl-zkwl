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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

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
            List<HistoryDay> historyDays1 = historyDayMapper.queryHistoryDayOther(deviceCode, time);
            queryHistory.setHistoryDays(historyDays1);
            for (HistoryDay historyDay : historyDays){
                String time1 = historyDay.getTime();
                String substring = time1.substring(11,13);
                historyDay.setTime(substring);
                queryHistory.getHours().add(substring);
                queryHistory.getDischargeCapacityList().add(historyDay.getDischargeCapacity());
                queryHistory.getChargingCapacityList().add(historyDay.getChargingCapacity());
                queryHistory.getInductionFrequencyList().add(historyDay.getInductionFrequency());
                queryHistory.getVisitorsFlowrateList().add(historyDay.getVisitorsFlowrate());
            }
        }
        List<HistoryDay> historyDays1 = historyDayMapper.queryDeviceByCode(deviceCode, time);
        if(CollectionUtils.isNotEmpty(historyDays1)){
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            for(HistoryDay historyDay : historyDays1){
                String format = dateFormat.format(historyDay.getCreateTime());
                queryHistory.getNewHours().add(format);
                queryHistory.getChargingCurrentList().add(historyDay.getChargingCurrent());
                queryHistory.getDischargeCurrentList().add(historyDay.getDischargeCurrent());
                queryHistory.getBatteryVoltageList().add(historyDay.getBatteryVoltage());
                queryHistory.getAmbientTemperatureList().add(historyDay.getAmbientTemperature());
                queryHistory.getInternalTemperatureList().add(historyDay.getInternalTemperature());
            }
        }
        return queryHistory;
    }

    @Override
    public QueryHistory queryHistoryMouth(String deviceCode,String year,String month) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        QueryHistory queryHistory = new QueryHistory();
        List<HistoryMouth> historyMouths = historyMouthMapper.queryHistoryMouth(deviceCode, year, month);
        if(CollectionUtils.isNotEmpty(historyMouths)){
            queryHistory.setHistoryMouths(historyMouths);
            for (HistoryMouth historyMouth : historyMouths){
                String format = dateFormat.format(historyMouth.getCreateTime());
                historyMouth.setTime(format);
                queryHistory.getHours().add(format);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        QueryHistory queryHistory = new QueryHistory();
        List<HistoryYear> historyYears = historyYearMapper.queryHistoryYear(deviceCode, year);
        if(CollectionUtils.isNotEmpty(historyYears)){
            queryHistory.setHistoryYears(historyYears);
            for (HistoryYear historyYear : historyYears){
                String format = dateFormat.format(historyYear.getCreateTime());
                historyYear.setTime(format);
                queryHistory.getHours().add(format);
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

    @Override
    public int deleteBatchDay() {
        return historyDayMapper.deleteBatchDay();
    }

    @Override
    public int deleteBatchMonth() {
        return historyMouthMapper.deleteBatchMouth();
    }

    @Override
    public int deleteBatchYear() {
        return historyYearMapper.deleteBatchYear();
    }

    /**
     * 通过当前设备codes查询所有设备的放电量
     * */
    @Override
    public Double getTotalDischarge(List<String> deviceCode) {
        return historyDayMapper.getTotalDischarge(deviceCode);
    }

    @Override
    public List<Map<Object, Object>> getDischargeCapacity(List<String> deviceCodes) {
        return historyDayMapper.getDischargeCapacity(deviceCodes);
    }
}
