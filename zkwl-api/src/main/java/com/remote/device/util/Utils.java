package com.remote.device.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zhangwenping
 * @Date 2019/6/19 10:28
 * @Version 1.0
 **/
@Component
public class Utils {

    /**
     * 将源List按照指定元素数量拆分为多个List
     *
     * @param source 源List
     * @param splitItemNum 每个List中元素数量
     */
    public static <T> List<List<T>> averageAssign( List<T> source, int splitItemNum )
    {
        List<List<T>> result = new ArrayList<List<T>>();

        if ( source != null && source.size() > 0 && splitItemNum > 0 ){
            if ( source.size() <= splitItemNum ){
                // 源List元素数量小于等于目标分组数量
                result.add( source );
            }else{
                // 计算拆分后list数量
                int splitNum = ( source.size() % splitItemNum == 0 ) ? ( source.size() / splitItemNum ) : ( source.size() / splitItemNum + 1 );
                List<T> value = null;
                for ( int i = 0; i < splitNum; i++ ){
                    if ( i < splitNum - 1 ){
                        value = source.subList( i * splitItemNum, ( i + 1 ) * splitItemNum );
                    }else{
                        // 最后一组
                        value = source.subList( i * splitItemNum, source.size() );
                    }
                    result.add(value);
                }
            }
        }
        return result;
    }

    /**
     * 解密
     * @param devKey
     * @return String
     */
    public static String decode(String devKey){
        StringBuffer sb = new StringBuffer();
        sb.append(devKey.substring(0,devKey.length() - 3));
        Integer devK = Integer.valueOf(devKey.substring(devKey.length() - 3, devKey.length()));
        Integer bai = devK / 100;
        Integer shi = (devK / 10) % 10;
        Integer ge = devK % 10;
        if(bai >= 10){
            bai = 1;
        }else{
            bai = bai + 1;
        }
        if(shi >= 10){
            shi = 1;
        }else{
            shi = shi + 1;
        }
        if(ge >= 10){
            ge = 1;
        }else{
            ge = ge + 1;
        }
        sb.append(bai).append(shi).append(ge);
        System.out.println(sb);
        return sb.toString();
    }

    /**
     * 加密
     * @param devSN
     * @return String
     */
    public static String encrypt(String devSN){
        StringBuffer sb = new StringBuffer();
        sb.append(devSN.substring(0,devSN.length() - 3));
        Integer devK = Integer.valueOf(devSN.substring(devSN.length() - 3, devSN.length()));
        Integer bai = devK / 100;
        Integer shi = (devK / 10) % 10;
        Integer ge = devK % 10;
        if(bai == 0){
            bai = 9;
        }else{
            bai = bai - 1;
        }
        if(shi == 0){
            shi = 9;
        }else{
            shi = shi - 1;
        }
        if(ge == 0){
            ge = 9;
        }else{
            ge = ge - 1;
        }
        sb.append(bai).append(shi).append(ge);
        System.out.println(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
       String str = "{\n" +
               "\t\"dataLen\": 59,\n" +
               "\t\"cmdID\": 2,\n" +
               "\t\"nextCmdID\": 55,\n" +
               "\t\"devKey\": \"793193446958375\",\n" +
               "\t\"devType\": 59,\n" +
               "\t\"devSN\": \"359759002513931\",\n" +
               "\t\"key\": [],\n" +
               "\t\"value\": []\n" +
               "}";
       System.out.println(str.length());

    }
}
