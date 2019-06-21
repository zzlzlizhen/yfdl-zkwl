package com.test;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import org.junit.Test;

public class TestMail {
    private static final String baseURL="127.0.0.1:8080/remote-admin";
    private static final String CURRENT_COOKIE = "JSESSIONID=0157ccd9-70f0-452b-8992-c221da5d6778";
    @Test
    public void testSendBindEmail(){
        String url = baseURL + "/mail/sendBindEmail?email=1648925727@qq.com";
        String result3= HttpRequest.get(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .execute().body();
        System.out.println(result3);
    }
    @Test
    public void testScurityCode(){
        String url = baseURL + "/mail/checkScurityCode?email=1648925727@qq.com&scurityCode=990801";
        String result4= HttpRequest.get(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .execute().body();
        System.out.println(result4);
    }
    @Test
    public void testSendBindSms(){
        String url = baseURL + "/sms/sendBindSms?mobile=15810669164";
        String result3= HttpRequest.get(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .execute().body();
        System.out.println(result3);
    }
    @Test
    public void testSmsScurityCode(){
        String url = baseURL + "/sms/checkScurityCode?mobile=15810669164&securityCode=196395";
        String result4= HttpRequest.get(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .execute().body();
        System.out.println(result4);
    }
}
