package com.test;


import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestFeedBack {
    private static final String baseURL="127.0.0.1:8080/remote-admin";
    private static final String CURRENT_COOKIE = "JSESSIONID=47e0b6d1-1c0c-437f-a3ca-4dc14a0f01a5";
    @Test
    public void testFeedBackSave(){
        String url = baseURL + "/sys/feedback/save";
        Map<String,Object> map= new HashMap<String,Object>();
     /*   map.put("deptId","2");*/
        map.put("backContent","123");
        map.put("email","12@qq.com");
        map.put("mobile","1233455");
        String result3= HttpRequest.post(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .form(map)
                .execute().body();
        System.out.println(result3);
    }
    @Test
    public void testFeedBackDelete(){

    }
    @Test
    public void testFeedBackUpdate(){
        String url = baseURL + "/sys/feedback/update";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("backId","43f3ba14cea04181a77bc8898b3b36bb");
        map.put("answerContent","zsmtest1");
        String result4 = HttpRequest.post(url).header(Header.COOKIE,CURRENT_COOKIE).form(map).execute().body();
        System.out.println(result4);
    }
}
