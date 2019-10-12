package com.remote.common.enums;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CommunicationTypeEnum {

    G2(1,"2G"),
    LORA(2,"Lora"),
    NBLOT(3,"NBLot");

    private String name;
    private int code;

    CommunicationTypeEnum(int code,String name) {
        this.name = name;
        this.code = code;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    //讲枚举转换成list格式，这样前台遍历的时候比较容易，列如 下拉框 后台调用toList方法，你就可以得到code 和name了
    public static List toList() {
        List list = Lists.newArrayList();//Lists.newArrayList()其实和new ArrayList()几乎一模
        for (CommunicationTypeEnum airlineTypeEnum : CommunicationTypeEnum.values()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", airlineTypeEnum.getCode());
            map.put("name", airlineTypeEnum.getName());
            list.add(map);
        }
        return list;
    }

}
