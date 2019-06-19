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
    public static final String CURRENT_COOKIE = "JSESSIONID=30f8314f-c1c7-4441-90f5-50802b6a3a28";
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
       /* map.put("userId","32");*/
        map.put("username","zsm33");
        map.put("password","111");
        map.put("mobile","1111");
        map.put("email","123@11.com");
        map.put("roleId",1);
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
        map.put("userId","48");
      /*  map.put("username","zsm11");*/
        map.put("password","111");
        map.put("roleId",1);
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
