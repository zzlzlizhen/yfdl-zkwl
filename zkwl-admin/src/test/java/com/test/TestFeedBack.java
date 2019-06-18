package com.test;


import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestFeedBack {
    private static final String baseURL="127.0.0.1:8080/remote-admin";
    private static final String CURRENT_COOKIE = "JSESSIONID=88946cc0-c16f-48fd-a34c-244e912bde46";
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
   /*     String url = baseURL + "/sys/feedback/delete";
        Map<String,Object> map= new HashMap<String,Object>();
        //map.put("ids","21,22,23");
        String result3= HttpRequest.post(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .form(map)
                .execute().body();
        System.out.println(result3);*/
    }
    @Test
    public void testFeedBackUpdate(){
        String url = baseURL + "/sys/feedback/update";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("backId","19527ae805ff459d9f173169fe927d91");
        map.put("answerContent","zsmtest333");
        String result4 = HttpRequest.post(url).header(Header.COOKIE,CURRENT_COOKIE).form(map).execute().body();
        System.out.println(result4);
    }
}
