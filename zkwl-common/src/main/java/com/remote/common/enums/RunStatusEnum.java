package com.remote.common.enums;


public enum RunStatusEnum {
    NORAML("正常",1),
    WARNING("警告",2),
    FAULT("故障",3),
    OFFLINE("离线",4);

    private String name;
    private int code;


    RunStatusEnum(String name, int code) {
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
