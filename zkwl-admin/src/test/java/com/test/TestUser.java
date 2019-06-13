package com.test;

import cn.hutool.core.util.ReUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestUser {

    public static final String URL = "http://127.0.0.1:8080/remote-admin";
    public static final String CURRENT_COOKIE = "JSESSIONID=11d86102-cfb5-4d41-b796-469eed2bfa8c";
    @Test
    public void infoTest(){
        String url = URL + "/sys/user/info";
        String result3= HttpRequest.get(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .execute().body();
        System.out.println(result3);
    }

    @Test
    public void saveUserTest(){
        String url = URL + "/sys/user/save";
        Map<String,Object> map= new HashMap<String,Object>();
     /*   map.put("deptId","2");*/
        map.put("username","123");
        map.put("password","234");
        map.put("email","123@11.com");
        String result3= HttpRequest.post(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .form(map)
                .execute().body();
        System.out.println(result3);
    }

    @Test
    public void updateUserTest(){
        String url = URL + "/sys/user/update";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("username","zsmtest1");
        map.put("status","1");
        String result4 = HttpRequest.post(url).header(Header.COOKIE,CURRENT_COOKIE).form(map).execute().body();
        System.out.println(result4);
    }
    @Test
    public void deleteUserTest(){
        String url = URL + "/sys/user/delete";
        Map<String,Object> map= new HashMap<String,Object>();
        //map.put("ids","21,22,23");
        String result3= HttpRequest.post(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .form(map)
                .execute().body();
        System.out.println(result3);
    }
}
