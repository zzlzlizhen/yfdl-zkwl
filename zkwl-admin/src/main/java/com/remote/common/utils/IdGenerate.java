package com.remote.common.utils;

import java.util.UUID;

public class IdGenerate {
    /**
     * 以UUID的策略生成一个长度为32的字符串，在同一时空中具有唯一性。
     * @return UUID128位长度字符串
     */
    public static String getUUIDString() {
        String id = UUID.randomUUID().toString();
        id = id.replace("-", "");
        return id;
    }
}
