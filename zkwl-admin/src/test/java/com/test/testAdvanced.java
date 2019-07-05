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
    private static final String baseURL="127.0.0.1:8085/remote-admin";
    private static final String CURRENT_COOKIE = "JSESSIONID=88633c5d-2c90-4e1e-bdd1-44874dc09b6d";
    @Test
    public void testAdvancedSave(){
        String url = baseURL + "/advancedsetting/save";
        Map<String,Object> map= new HashMap<String,Object>();
     /*   map.put("deptId","2");*/
        map.put("projectId","16b26934-6d1b-4149-80b6-ad94b0d40f35");
        map.put("groupId","0");
        map.put("loadWorkMode",1);
        map.put("powerLoad",50);
        map.put("powerPeople1",70);
        String result3= HttpRequest.post(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .form(map)
                .execute().body();
        System.out.println(result3);
    }
    @Test
    public void testChange(){
        String url = baseURL + "/fun/device/change";
        Map<String,Object> map= new HashMap<String,Object>();
        map.put("projectId","0339c920-ea0b-4e4d-80fc-e22e25b5751a");
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
    public void testAdvancedUpdate(){
        String url = baseURL + "/advancedsetting/update";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id","1");
        map.put("loadWorkMode",2);
        map.put("powerLoad",55);
        map.put("powerPeople1",75);
        String result4 = HttpRequest.post(url).header(Header.COOKIE,CURRENT_COOKIE).form(map).execute().body();
        System.out.println(result4);
    }
}
