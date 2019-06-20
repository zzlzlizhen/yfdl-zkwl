package com.remote.common.utils;

import java.util.Random;

/**
 * @author zsm
 * @date 2019/6/20 17:17
 * @description:
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

    public static String getSecurityCode(int len){
        Random random = new Random();
        String result = "";
        for(int i=0;i<len;i++){
            result = result + random.nextInt(10);
        }
        return result;
    }

    public static void main(String[] args){
        for(int i=0;i<100;i++){
            System.out.println(getSecurityCode(6));
        }
    }
}
