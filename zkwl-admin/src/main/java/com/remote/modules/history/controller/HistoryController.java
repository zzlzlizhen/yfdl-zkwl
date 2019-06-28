package com.remote.modules.history.controller;

import com.remote.common.utils.R;
import com.remote.modules.history.entity.QueryHistory;
import com.remote.modules.history.service.HistoryService;
import com.remote.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/6/14 14:08
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fun/history")
public class HistoryController extends AbstractController {

    @Autowired
    private HistoryService historyService;


    @RequestMapping(value = "/queryHistoryByDay", method= RequestMethod.GET)
    public R queryDevice(String deviceCode,String time){
        StringBuffer sb = new StringBuffer();
        String[] split = time.split("-");
        sb.append(split[0]).append("-");
        if(split[1].length() != 2){
            sb.append("0").append(split[1]).append("-");
        }else{
            sb.append(split[1]).append("-");
        }
        if(split[2].length() != 2){
            sb.append("0").append(split[2]);
        }else{
            sb.append(split[2]);
        }
        QueryHistory queryHistory = historyService.queryHistoryDay(deviceCode,sb.toString());
        if(queryHistory != null){
            return R.ok(queryHistory);
        }
        return R.error(400,"查询历史数据失败");
    }

    @RequestMapping(value = "/queryHistoryByMouth", method= RequestMethod.GET)
    public R queryHistoryByMouth(String deviceCode,String year,String month){
        QueryHistory queryHistory = historyService.queryHistoryMouth(deviceCode, year, month);
        if(queryHistory != null){
            return R.ok(queryHistory);
        }
        return R.error(400,"查询历史数据失败");
    }

    @RequestMapping(value = "/queryHistoryByYear", method= RequestMethod.GET)
    public R queryHistoryByYear(String deviceCode,String year){
        QueryHistory queryHistory = historyService.queryHistoryYear(deviceCode, year);
        if(queryHistory != null){
            return R.ok(queryHistory);
        }
        return R.error(400,"查询历史数据失败");
    }


}
