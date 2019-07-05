package com.remote.enums;


public enum CommunicationEnum {
    NORMAL("2G",1),
    ALARM("Lora",2),
    FAULT("NBLot",3);


    private String name;
    private int code;


    CommunicationEnum(String name, int code) {
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
