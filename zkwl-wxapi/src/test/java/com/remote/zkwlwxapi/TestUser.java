package com.remote.zkwlwxapi;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestUser {

    public static final String URL = "http://127.0.0.1:8086/remote-wxapi";

/*
    @Test
    public void infoTest(){
        String url = URL + "/sys/user/info";
        String result3= HttpRequest.get(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .execute().body();
        System.out.println(result3);
    }
*/

    @Test
    public void saveUserTest(){
   /*     String url = URL + "/sys/user/login";
        Map<String,Object> map= new HashMap<String,Object>();
        map.put("username","admin");
        map.put("password","1111");
        String result3= HttpUtil.post(url,map);
        System.out.println(result3);*/
    }

  /*  @Test
    public void updateUserTest(){
        String url = URL + "/sys/user/update";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId","48");
      *//*  map.put("username","zsm11");*//*
        map.put("password","111");
        map.put("roleId",1);
        map.put("status",1);
        String result4 = HttpRequest.post(url).header(Header.COOKIE,CURRENT_COOKIE).form(map).execute().body();
        System.out.println(result4);
    }
    @Test
    public void updateStatus(){
        String url = URL + "/sys/user/status";
        Map<String,Object> map = new HashMap<String,Object>();

        map.put("userId",44);
        map.put("status",0);
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

    @Test
    public void updateBaseInfo(){
        String url = URL + "/sys/user/updateBaseInfo";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("mobile","15810669164");
        map.put("email","1648925727@qq.com");
        String result4 = HttpRequest.post(url).header(Header.COOKIE,CURRENT_COOKIE).form(map).execute().body();
        System.out.println(result4);
    }*/
}
