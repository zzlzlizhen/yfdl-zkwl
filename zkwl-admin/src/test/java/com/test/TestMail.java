package com.test;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import org.junit.Test;

public class TestMail {
    private static final String baseURL="127.0.0.1:8085/remote-admin";
    private static final String CURRENT_COOKIE = "JSESSIONID=182d59fc-39bc-475b-8c88-450df166d74f";
    @Test
    public void testSendBindEmail(){
        String url = baseURL + "/contact/sendBindEmail?email=1648925727@qq.com";
        String result3= HttpRequest.get(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .execute().body();
        System.out.println(result3);
    }
    @Test
    public void checkBindEmail(){
        String url = baseURL + "/contact/checkBindEmail?email=1648925727@qq.com&securityCode=018671";
        String result4= HttpRequest.get(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .execute().body();
        System.out.println(result4);
    }
    @Test
    public void checkRePwdEmail(){
        String url = baseURL + "/contact/checkRePwdEmail?email=1648925727@qq.com&securityCode=236881";
        String result4= HttpRequest.get(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .execute().body();
        System.out.println(result4);
    }
    @Test
    public void sendBindMobile(){
        String url = baseURL + "/contact/sendBindMobile?mobile=15810669164";
        String result3= HttpRequest.get(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .execute().body();
        System.out.println(result3);
    }
    @Test
    public void checkBindMobile(){
        String url = baseURL + "/contact/checkBindMobile?mobile=15810669164&securityCode=642813";
        String result4= HttpRequest.get(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .execute().body();
        System.out.println(result4);
    }
    @Test
    public void checkRePwdMobile(){
        String url = baseURL + "/contact/checkRePwdMobile?mobile=15810669164&securityCode=642813";
        String result4= HttpRequest.get(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .execute().body();
        System.out.println(result4);
    }
    @Test
    public void testSendForPwd(){
        String url = baseURL + "/repwd/sendForPwd?contact=1648925727@qq.com&fPwdType=isEmail";
        String result4= HttpRequest.get(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .execute().body();
        System.out.println(result4);
    }
}
