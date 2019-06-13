package com.remote.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author zhangwenping
 * @Date 2019/5/31 13:35
 * @Version 1.0
 **/
public class ProjectCodeUitls {

    /*
     * @Author zwp
     * @Description 返回项目编号
     * @Date 13:41 2019/5/31
     * @Param
     * @return String
     **/
    public static String getProjectCode(){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");//设置日期格式
        return "no"+LocalDateTime.now().format(fmt);
    }

}
