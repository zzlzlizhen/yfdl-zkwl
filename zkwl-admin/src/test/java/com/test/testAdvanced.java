package com.test;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class testAdvanced {
    private static final String baseURL="127.0.0.1:8080/remote-admin";
    private static final String CURRENT_COOKIE = "JSESSIONID=d47c6a70-dcf7-4eca-a620-bdcec28efd19";
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
