package com.remote.common.enums;


public enum TransportEnum {
    NO("退出休眠",0),
    YES("进入休眠",1);

    private String name;
    private int code;


    TransportEnum(String name, int code) {
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
