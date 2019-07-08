package com.remote.common.enums;


public enum BatteryStatusEnum {
    DISCHARGE("过放",0),
    UNDERVOLTAGE("欠压",1),
    NORMAL("正常",2),
    LIMITING("限压",3),
    OVERPRESSURE("超压",4),
    TEMPERATURE("过温",5),
    ACTIVATION("激活",6);
    private String name;
    private int code;


    BatteryStatusEnum(String name, int code) {
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

}
