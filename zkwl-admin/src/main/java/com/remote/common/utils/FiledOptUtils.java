package com.remote.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FiledOptUtils {
    /**
     * 获取属性名数组
     * */
    public static List<String> getFiledName(Object o){
        List<String> filedList = new ArrayList<String>();
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for(int i = 0; i < fields.length;i++){
            fieldNames[i] = fields[i].getName();
        }
        if(fieldNames != null && fieldNames.length >=0){
            filedList = Arrays.asList(fieldNames);
        }
        return filedList;
    }
    /* 根据属性名获取属性值
    * */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }
}

