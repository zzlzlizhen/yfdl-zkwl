package com.remote.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zsm
 * @date 2019/6/18 17:08
 * @description:
 */
@Component
@ConfigurationProperties(prefix="message")
@PropertySource(value = {"classpath:file-info.properties"},encoding = "utf-8")
public class FileInfoConfig {

    private long fileSize;  //压缩大小

    private double scaleRatio; //压缩比例

    private String upPath; //保存路径

    private String relativePath;

    private String imageType; //图片类型

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public double getScaleRatio() {
        return scaleRatio;
    }

    public void setScaleRatio(double scaleRatio) {
        this.scaleRatio = scaleRatio;
    }

    public String getUpPath() {
        return upPath;
    }

    public void setUpPath(String upPath) {
        this.upPath = upPath;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}
