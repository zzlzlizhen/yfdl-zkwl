package com.remote.modules.sys.service.impl;

import com.remote.common.config.FileInfoConfig;
import com.remote.common.exception.RRException;
import com.remote.common.utils.R;
import com.remote.modules.sys.service.FileUpAndDownService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author lizhen
 * @date 2019/6/18 17:01
 * @description:
 */
@Service("fileUpAndDownService")
public class FileUpAndDownServiceImpl implements FileUpAndDownService {

    @Autowired
    private FileInfoConfig config;

    @Override
    public R uploadPicture(MultipartFile file) throws Exception {
        try {
            R r = R.ok();
            String[] IMAGE_TYPE = config.getImageType().split(",");
            String path = null;
            boolean flag = false;
            for (String type : IMAGE_TYPE) {
                if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), type)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                // 获得文件类型
                String fileType = file.getContentType();
                // 获得文件后缀名称
                String imageName = fileType.substring(fileType.indexOf("/") + 1);
                // 原名称
                String oldFileName = file.getOriginalFilename();
                // 新名称
                String newFileName = uuid + "." + imageName;
                // 年月日文件夹
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String basedir = sdf.format(new Date());
                // 进行压缩(大于4M)
                if (file.getSize() > config.getFileSize()) {
                    // 重新生成
                    String newUUID = UUID.randomUUID().toString().replaceAll("-", "");
                    newFileName = newUUID + "." + imageName;
                    path = config.getUpPath() + "/" + basedir + "/" + newUUID + "." + imageName;
                    // 如果目录不存在则创建目录
                    File oldFile = new File(path);
                    if (!oldFile.exists()) {
                        oldFile.mkdirs();
                    }
                    file.transferTo(oldFile);
                    // 压缩图片
                    Thumbnails.of(oldFile).scale(config.getScaleRatio()).toFile(path);
                    // 显示路径
                    r.put("url", "/" + basedir + "/" + newUUID + "." + imageName);
                } else {
                    path = config.getUpPath() + "/" + basedir + "/" + uuid + "." + imageName;
                    // 如果目录不存在则创建目录
                    File uploadFile = new File(path);
                    if (!uploadFile.exists()) {
                        uploadFile.mkdirs();
                    }
                    file.transferTo(uploadFile);
                    // 显示路径
                    r.put("url", "/" + basedir + "/" + uuid + "." + imageName);
                }
                r.put("oldFileName", oldFileName);
                r.put("newFileName", newFileName);
                r.put("fileSize", file.getSize());
            } else {
                throw new RRException("图片格式不正确,支持png|jpg|jpeg");
            }
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RRException("内部异常，请联系管理员");
        }
    }
}
