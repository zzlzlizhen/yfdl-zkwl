package com.test;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import org.junit.Test;

public class TestMail {
    private static final String baseURL="127.0.0.1:8080/remote-admin";
    private static final String CURRENT_COOKIE = "JSESSIONID=24a01f57-a439-4d29-9735-0796563b97b2";
    @Test
    public void testMail(){
        String url = baseURL + "/mail/getCheckCode?email=1648925727@qq.com";
        String result3= HttpRequest.get(url)
                .header(Header.COOKIE, CURRENT_COOKIE)
                .execute().body();
        System.out.println(result3);
    }
}
