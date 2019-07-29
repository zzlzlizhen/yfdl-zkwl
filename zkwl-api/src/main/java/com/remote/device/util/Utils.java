package com.remote.device.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/6/19 10:28
 * @Version 1.0
 **/
@Component
public class Utils {

    public static File file = null;
    public static FileInputStream fileInputStream = null;
    public static DataInputStream  dataInputStream = null;


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



    /**
     * double 除法
     * @param d1
     * @param scale 四舍五入 小数点位数
     * @return
     */
    public static double div(double d1,int scale){
        //  当然在此之前，你要判断分母是否为0，
        //  为0你可以根据实际需求做相应的处理

        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        double v = bd1.divide(new BigDecimal(100), scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return v;
    }

    // 进行乘法运算
    public static double mul(double d1, double d2){
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        BigDecimal multiply = b1.multiply(b2);
        return multiply.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static UpdateVersion version(){
        UpdateVersion version = new UpdateVersion();
        try {
            file = new File("/home/test.bin");
            //file = new File("D:\\test1.bin");
            fileInputStream = new FileInputStream(file);
            dataInputStream = new DataInputStream(fileInputStream);
            //建立缓冲文本输入流
            long len=file.length();
            if(len % 1024 != 0){
                len= ( len / 1024 + 1 ) * 1024;
            }
            byte[] bytes = new byte[(int)len];
            Byte[] newBytes = new Byte[(int)len];
            dataInputStream.read(bytes);
            int dataLen = 0,sum=0,temp=0;
            for(int i = 0 ; i < bytes.length;i+=2){
                temp=bytes[i];
                dataLen =temp&0xFF;
                temp=bytes[i+1]<<8;
                dataLen +=temp&0xFF00;
                dataLen = dataLen & 0xffff;
                sum+=dataLen;
            }
            for(int i = 0 ; i < bytes.length ; i ++){
                newBytes[i] = bytes[i];
            }
            sum = sum & 0xffff;
            version.setLength(len);
            version.setSum(sum);
            version.setBytes(newBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }


    public static void main(String[] args) {
        BigDecimal bd1 = new BigDecimal(Double.toString(7));
        double v = bd1.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(v);
    }


}
