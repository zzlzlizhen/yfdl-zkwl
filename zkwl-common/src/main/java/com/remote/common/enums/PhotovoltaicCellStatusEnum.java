package com.remote.common.enums;


public enum PhotovoltaicCellStatusEnum {
    LIGHTWEAK("光弱",0),
    LIGHTINTENSITY("光强",1),
    CHARGING("正在充电",2);

    private String name;
    private int code;


    PhotovoltaicCellStatusEnum(String name, int code) {
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
