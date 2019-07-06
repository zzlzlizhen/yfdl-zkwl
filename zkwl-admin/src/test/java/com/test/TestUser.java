package com.test;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestUser {

    public static final String URL = "http://127.0.0.1:8085/remote-admin";
    public static final String CURRENT_COOKIE = "JSESSIONID=6a1c7934-97a1-43e5-9baf-8efd40bf0440";
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
    }

    @Test
    public void updatePwd(){
        String url = URL + "/sys/user/updatePwd";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("username","zsm2");
        map.put("password","123456");
        HttpRequest.post(url).form(map).execute().body();
    }
}
