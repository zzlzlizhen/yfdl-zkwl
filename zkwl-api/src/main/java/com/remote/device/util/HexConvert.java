package com.remote.device.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/6/11 14:57
 * @Version 1.0
 **/
public class HexConvert {
    private static Logger logger = LoggerFactory.getLogger(HexConvert.class);


    public static byte[] hexStringToBytes(DeviceInfo deviceInfo) {
        Integer cmdID = deviceInfo.getCmdID();
        Integer nextCmdID = deviceInfo.getNextCmdID();

        String devKey = deviceInfo.getDevKey();
        String devSN = deviceInfo.getDevSN();

        int i = 0;
        int size = 56+deviceInfo.getKey().size() * 4;
        byte[] bytes = new byte[size];
        //代表是dataLen
        bytes[i++]=(byte)( size&0xFF);
        bytes[i++]=(byte)( size>>8);

        //代表cmdId
        bytes[i++]=(byte)( cmdID&0xFF);
        bytes[i++]=(byte)( cmdID>>8);

        //nextId
        bytes[i++]=(byte)( nextCmdID&0xFF);
        bytes[i++]=(byte)( nextCmdID>>8);

        //代表devType
        bytes[i++]=(byte)( 1&0xFF);
        bytes[i++]=(byte)( 1>>8);



        byte[] bytes1 = devKey.getBytes();
        for(int j = 0 ; j < 24 ; j++){
            if(j< bytes1.length){
                bytes[i++] = bytes1[j];
            }else{
                bytes[i++] = 0;
            }
        }

        byte[] bytes2 = devSN.getBytes();
        for(int j = 0 ; j < 24 ; j++){
            if(j< bytes2.length){
                bytes[i++] = bytes2[j];
            }else{
                bytes[i++] = 0;
            }
        }

        List<Integer> key = deviceInfo.getKey();
        List<Integer> value = deviceInfo.getValue();

        for(int j = 0 ;j<key.size();j++){
            value.get(j);
            bytes[i++]=(byte)( key.get(j)&0xFF);
            bytes[i++]=(byte)( key.get(j)>>8);
            if(value.size() != 0){
                bytes[i++]=(byte)( value.get(j)&0xFF);
                bytes[i++]=(byte)( value.get(j)>>8);
            }else{
                bytes[i++]=(byte)(0);
                bytes[i++]=(byte)(0);
            }
        }

        return bytes;
    }


    public static byte[] updateVersionToBytes(DeviceVersionInfo deviceInfo) {

        Integer cmdID = deviceInfo.getCmdID();
        Integer nextCmdID = deviceInfo.getNextCmdID();
        Integer upgradeFlag = deviceInfo.getUpgradeFlag();
        Integer binSize = deviceInfo.getBinSize();
        Integer binLastSize = deviceInfo.getBinLastSize();
        Integer checkSum = deviceInfo.getCheckSum();
        String devKey = deviceInfo.getDevKey();
        String devSN = deviceInfo.getDevSN();

        int i = 0;
        int size = 1088;
        byte[] bytes = new byte[1088];
        //代表是dataLen
        bytes[i++]=(byte)( size&0xFF);
        bytes[i++]=(byte)( size>>8);
        //代表cmdId
        bytes[i++]=(byte)( cmdID&0xFF);
        bytes[i++]=(byte)( cmdID>>8);
        //nextId
        bytes[i++]=(byte)( nextCmdID&0xFF);
        bytes[i++]=(byte)( nextCmdID>>8);
        //代表devType
        bytes[i++]=(byte)( 1&0xFF);
        bytes[i++]=(byte)( 1>>8);
        //devKey
        byte[] bytes1 = devKey.getBytes();
        for(int j = 0 ; j < 24 ; j++){
            if(j< bytes1.length){
                bytes[i++] = bytes1[j];
            }else{
                bytes[i++] = 0;
            }
        }
        //SN
        byte[] bytes2 = devSN.getBytes();
        for(int j = 0 ; j < 24 ; j++){
            if(j< bytes2.length){
                bytes[i++] = bytes2[j];
            }else{
                bytes[i++] = 0;
            }
        }
        //代表upgradeFlag
        bytes[i++]=(byte)(upgradeFlag&0xFF);
        bytes[i++]=(byte)(upgradeFlag>>8);
        //binSize
        bytes[i++]=(byte)(binSize&0xFF);
        bytes[i++]=(byte)(binSize>>8);
        //binLastSize
        bytes[i++]=(byte)(binLastSize&0xFF);
        bytes[i++]=(byte)(binLastSize>>8);
        //checkSum
        bytes[i++]=(byte)(checkSum&0xFF);
        bytes[i++]=(byte)(checkSum>>8);
        //数据
        Byte[] bin = deviceInfo.getBin();
        for (int j = 0 ; j < bin.length ; j++){
            bytes[i++] = bin[j];
        }
        return bytes;
    }



    //将字节数组转换为16进制字符串
    public static DeviceInfo BinaryToDeviceInfo(byte[] bytes)  {
        DeviceInfo deviceInfo = new DeviceInfo();
        try{

            int dataLen = 0;
            int cmdID = 0;
            int devType = 0;
            int nextId = 0;

            int newBytes[] = new int[bytes.length];

            for (int l=0;l< bytes.length;l++){
                newBytes[l] = bytes[l] & 0xff;
            }

            int i=0;

            dataLen =newBytes[i++];
            dataLen +=newBytes[i++]<<8;

            int newByte[] = new int[dataLen];
            for (int j = 0 ; j < newByte.length;j++){
                newByte[j] = newBytes[j];
            }

            cmdID =newByte[i++];
            cmdID +=newByte[i++]<<8;

            nextId =newByte[i++];
            nextId +=newByte[i++]<<8;

            devType =newByte[i++];
            devType +=newByte[i++]<<8;



            int size = 0;
            for(int j=0;j<24;j++){
                if(bytes[i+j] != 0){
                    size ++ ;
                }
            }
            logger.info("SN长度："+size);
            byte devSN[] = new byte[size];
            byte devKey[] = new byte[size];
            for(int j=0;j<24;j++){
                if(bytes[i] != 0){
                    devKey[j]=bytes[i++];
                }else {
                    i++;
                }
            }
            for(int j=0;j<24;j++){
                if(bytes[i] != 0){
                    devSN[j]=bytes[i++];
                }else{
                    i++;
                }
            }
            String devKeyStr = new String(devKey, "UTF-8");
            String devSNStr = new String(devSN, "UTF-8");

            deviceInfo.setDataLen(dataLen);
            deviceInfo.setDevSN(devSNStr);
            deviceInfo.setCmdID(cmdID);
            deviceInfo.setDevKey(devKeyStr);
            deviceInfo.setDevType(String.valueOf(devType));
            deviceInfo.setNextCmdID(nextId);
            List<Integer> keyList = new ArrayList<>();
            List<Integer> valueList = new ArrayList<>();
            int key = 0;
            int value = 0;
            dataLen = (dataLen - 56) / 4;
            for(int j=0;j<dataLen;j++){
                key = newByte[i++];
                key += newByte[i++]<<8;
                keyList.add(key);
                value = newByte[i++];
                value += newByte[i++]<<8;
                valueList.add(value);
            }
            deviceInfo.setKey(keyList);
            deviceInfo.setValue(valueList);

        }catch (Exception e){
            logger.error("数据解析异常:"+e.getMessage(),e);
            e.printStackTrace();
        }
        return deviceInfo;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {


//        System.out.println("======ASCII码转换为16进制======");
//        String str = "A";
//        System.out.println("字符串: " + str);
//        String hex = HexConvert.convertStringToHex(str);
//        System.out.println("====转换为16进制=====" + hex);
//
//        System.out.println("======16进制转换为ASCII======");
//        System.out.println("Hex : " + hex);
//        System.out.println("ASCII : " + HexConvert.convertHexToString(hex));
//
//        byte[] bytes = HexConvert.hexStringToBytes( hex );
//
//        System.out.println(HexConvert.BinaryToHexString( bytes ));

        byte bytes[] ={
                0x03, 0x00,//dataLen
                0x01, 0x00,//cmdId
                0x01, 0x00,//devType
                0x37, 0x39, 0x33, 0x31, 0x39, 0x33, 0x34, 0x34, 0x36, 0x39, 0x35, 0x38, 0x33, 0x37, 0x35, 0x00, 0x00, 0x00, 0x00, 0x00,//devKey 20位  加密
                0x33, 0x35, 0x39, 0x37, 0x35, 0x39, 0x30, 0x30, 0x32, 0x35, 0x31, 0x33, 0x39, 0x33, 0x31, 0x00, 0x00, 0x00, 0x00, 0x00,//devSN
                0x05, 0x00,     -120, 0x00, //开灯 --- 值
                0x01, 0x00,     0x08, 0x00,
                0x07, 0x00,     0x60, 0x00,
        };
        for (int i = 0 ; i < bytes.length;i++){
            System.out.print(bytes[i]+",");
        }

       // System.out.println(bytes1);

    }
}
