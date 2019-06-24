package com.remote.device.util;

import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/6/11 14:57
 * @Version 1.0
 **/
public class HexConvert {

    public static byte[] hexStringToBytes(DeviceInfo deviceInfo) {
        Integer dataLen = deviceInfo.getDataLen();
        Integer cmdID = deviceInfo.getCmdID();

        String devKey = deviceInfo.getDevKey();
        String devSN = deviceInfo.getDevSN();

        int i = 0;
        int size = 46+deviceInfo.getKey().size() * 4;
        byte[] bytes = new byte[size];
        //代表是dataLen
        bytes[i++]=(byte)( dataLen&0xFF);
        bytes[i++]=(byte)( dataLen/0xFF);
        //代表cmdId
        bytes[i++]=(byte)( cmdID&0xFF);
        bytes[i++]=(byte)( cmdID/0xFF);

        //代表devType
        bytes[i++]=(byte)( 1&0xFF);
        bytes[i++]=(byte)( 1/0xFF);

        byte[] bytes1 = devKey.getBytes();
        for(byte b : bytes1){
            bytes[i++] = b;
        }
        byte[] bytes2 = devSN.getBytes();
        for (byte b : bytes2){
            bytes[i++] = b;
        }
        List<Integer> key = deviceInfo.getKey();
        List<Integer> value = deviceInfo.getValue();
        for(int j = 0 ;j<dataLen;j++){
            bytes[i++]=(byte)( key.get(j)&0xFF);
            bytes[i++]=(byte)( key.get(j)/0xFF);
            if(value.size() != 0){
                bytes[i++]=(byte)( value.get(j)&0xFF);
                bytes[i++]=(byte)( value.get(j)/0xFF);
            }else{
                bytes[i++]=(byte)(0);
                bytes[i++]=(byte)(0);
            }
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

            int newBytes[] = new int[bytes.length];

            for (int l=0;l< bytes.length;l++){
                newBytes[l] = bytes[l] & 0xff;
            }


            int i=0;

            dataLen =newBytes[i++];
            dataLen +=newBytes[i++]<<8;

            cmdID =newBytes[i++];
            cmdID +=newBytes[i++]<<8;

            devType =newBytes[i++];
            devType +=newBytes[i++]<<8;

            int size = 0;
            for(int j=0;j<24;j++){
                if(bytes[i+j] != 0){
                    size ++ ;
                }
            }
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

            List<Integer> keyList = new ArrayList<>();
            List<Integer> valueList = new ArrayList<>();
            int key = 0;
            int value = 0;
            for(int j=0;j<dataLen;j++){
                key = newBytes[i++];
                key += newBytes[i++]<<8;
                keyList.add(key);
                value = newBytes[i++];
                value += newBytes[i++]<<8;
                valueList.add(value);
            }
            deviceInfo.setKey(keyList);
            deviceInfo.setValue(valueList);

        }catch (Exception e){
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
        DeviceInfo deviceInfo = BinaryToDeviceInfo(bytes);

        byte[] bytes1 = hexStringToBytes(deviceInfo);

        System.out.println(bytes1);

    }
}
