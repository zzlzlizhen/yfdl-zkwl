package com.test;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import io.swagger.models.auth.In;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class testAdvanced {
    private static final String baseURL="127.0.0.1:8080/remote-admin";
    private static final String CURRENT_COOKIE = "JSESSIONID=bb96fe6e-2895-4cae-8a47-56fb9ad7f3ee";

    @Test
    public void testChange(){
        String url = baseURL + "/fun/device/change";
        Map<String,Object> map= new HashMap<String,Object>();
        map.put("groupId","0339c920-ea0b-4e4d-80fc-e22e25b5751a");
        List<String> qaKey = new ArrayList<String>();
        qaKey.add("loadWorkMode");
        qaKey.add("powerLoad");
        qaKey.add("powerPeople1");
        List<Integer> value = new ArrayList<Integer>();
        value.add(1);
        value.add(2);
        value.add(3);
        map.put("qaKey",qaKey);
        map.put("value",value);
        String result3= HttpRequest.post(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .form(map)
                .execute().body();
        System.out.println(result3);
    }
    @Test
    public void updateGroup(){
        String url = baseURL + "/advancedsetting/updateGroup";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("groupId","52027ee5-ba61-4c32-8342-f233fd3ab19a");
        map.put("loadWorkMode",3);
        map.put("powerLoad",5);
        String result4 = HttpRequest.post(url).header(Header.COOKIE,CURRENT_COOKIE).form(map).execute().body();
        System.out.println(result4);
    }
    @Test
    public void updateDevice(){
        String url = baseURL + "/advancedsetting/updateDevice";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("groupId","52027ee5-ba61-4c32-8342-f233fd3ab19a");
        map.put("deviceCode","1133545");
        map.put("loadWorkMode",2);
        map.put("powerLoad",55);
        map.put("powerPeople1",75);
        String result4 = HttpRequest.post(url).header(Header.COOKIE,CURRENT_COOKIE).form(map).execute().body();
        System.out.println(result4);
    }

    @Test
    public void updateMqtt(){
        String url = baseURL + "/sys/mqtt/dynamic_pub";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("topic","test3");
        map.put("message","哈哈哈");
        String result4 = HttpRequest.post(url).header(Header.COOKIE,CURRENT_COOKIE).form(map).execute().body();
        System.out.println(result4);
    }
    @Test
    public void updateSubMqtt(){
        String url = baseURL + "/sys/mqtt/pub";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("topic","test3");
        map.put("message","哈哈哈");
        String result4 = HttpRequest.post(url).header(Header.COOKIE,CURRENT_COOKIE).form(map).execute().body();
        System.out.println(result4);
    }
}
