package com.remote.device.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
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
     * 加密
     * @param devSN
     * @return String
     */
    public static String encrypt(String devSN){
        String devKeyStr = "";
        try {
            byte[] bytes1 = devSN.substring(10,11).getBytes();
            bytes1[0] = (byte) (bytes1[0] - 0x30 + 5);
            byte[] bytes = devSN.getBytes();
            for(int i=0;i<bytes.length;i++){
                bytes[i] += bytes1[0];
            }
            devKeyStr = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return devKeyStr;
    }

    public static void main(String[] args) {
       System.out.println(encrypt("359759002513931"));
    }
}
