package com.remote.common.utils.enums;

public enum FileEnum implements FileFormatEnum {

    JPG("jpg","img"),
    PNG("png","img"),
    BMP("bmp","img"),
    GIF("gif","img"),
    JPEG("jpeg","img"),
    MP4("mp4","video"),
    AVI("avi","video"),
    MOV("mov","video"),
    WMV("wmv","video"),
    FLV("flv","video"),
    RMVB("rmvb","video"),
    M3U8("m3u8","video"),
    TXT("txt","document"),
    PDF("pdf","document"),
    XLS("xls","document"),
    XLSX("xlsx","document"),
    DOC("doc","document"),
    DOCX("docx","document"),
    MP3("mp3","music")
    ;

    //文件后缀
    private String suffix;
    //文件目录
    private String dir;

    public String getSuffix() {
        return suffix;
    }

    @Override
    public String getDir() {
        return dir;
    }

    FileEnum(String suffix, String dir) {
        this.suffix = suffix;
        this.dir = dir;
    }
}
