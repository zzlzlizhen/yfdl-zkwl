package com.remote.common.utils;


import com.remote.common.utils.enums.FileEnum;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FileUtil {
     public static String uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        String newDir = getDirAndDateDir(fileName);
        File targetFile = new File(filePath + newDir);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + newDir + fileName);
        out.write(file);
        out.flush();
        out.close();
        return newDir + fileName;
    }

    // 物理删除；
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }




    public static String renameToUUID(String fileName) {
        return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static String renameToUUID() {
        return UUID.randomUUID().toString();
    }

    public static boolean isImage(InputStream inputStream) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        if(bufferedImage == null) {
            return false;
        }
        return true;
    }

    public static boolean isImage(File file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);
        if(bufferedImage == null) {
            return false;
        }
        return true;
    }


    public static String getDirAndDateDir(String fileName){
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String dir = "";
        for (FileEnum f : FileEnum.values()) {
            if(suffix.equals(f.getSuffix())){
                dir = f.getDir();
            }
        }
/*        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        String dateTime = year+"-"+month+"-"+day;*/
      /*  return dir+"/"+dateTime+"/";*/
        return "/" + dir+"/";
    }
}
