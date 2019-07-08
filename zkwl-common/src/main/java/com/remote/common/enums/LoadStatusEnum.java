package com.remote.common.enums;


public enum LoadStatusEnum {
    SHUT("关",0),
    OPEN("开",1),
    OPENCIRCUITPROECTION("开路保护",2),
    THROUGHPROTECTION("直通保护",3),
    SHORTCIRCUITPROECTION("短路保护",4),
    OVERLOADPROTECTION("过载保护",5),
    OVERLOADWARNING("过载警告",6);
    private String name;
    private int code;


    LoadStatusEnum(String name, int code) {
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
