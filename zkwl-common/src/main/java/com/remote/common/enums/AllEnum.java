package com.remote.common.enums;


public enum AllEnum {
    NO("否",0),
    YES("是",1);

    private String name;
    private int code;


    AllEnum(String name, int code) {
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
