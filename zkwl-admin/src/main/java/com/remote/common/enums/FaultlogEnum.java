package com.remote.common.enums;

public enum FaultlogEnum {

    FAULTLOG("故障日志",1),
    OPERATIONALLOG("操作日志",2);

    private String name;
    private int code;


    FaultlogEnum(String name, int code) {
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
