package com.remote.common.utils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConvertAddES {
    public Map<String,Object> convertAddES(Object obj){
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        Map<String,Object> temp = new HashMap<String,Object>();
        for (Field field : declaredFields){
            field.setAccessible(true);
            try {
                if (field.getGenericType().toString().equals("class java.lang.String")) {
                    temp.put(field.getName(),field.get(obj) == null ? "" : field.get(obj));
                }else if (field.getGenericType().toString().equals("class java.lang.Integer")) {
                    temp.put(field.getName(),field.get(obj) == null ? 0 : field.get(obj));
                }else if (field.getGenericType().toString().equals("class java.lang.Double")) {
                    temp.put(field.getName(),field.get(obj) == null ? 0.0 :field.get(obj));
                }else if(field.getGenericType().toString().equals("class java.util.Date")){
                    temp.put(field.getName(),field.get(obj) == null ? new Date() :field.get(obj));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return temp;
    }
}
