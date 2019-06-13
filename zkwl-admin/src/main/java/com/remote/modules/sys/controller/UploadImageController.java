package com.remote.modules.sys.controller;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/upload/")
public class UploadImageController {
    //@Value("${admin.images.uploadPath}")
  private String uploadPath;
/*
    @Resource
    SysQrCodeLogic sysQrCodeLogic;

    @Resource
    GlobalConfigLogic globalConfigLogic;
*/


    @ApiOperation(value = "上传文件")
    @PostMapping("uploadimg")
    public R<Map> uploadImages1(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response){
     /*   GlobalConfig config = globalConfigLogic.getByGlobalKey("admin.images.uploadPath");
        String uploadPath = config.getGlobalValue();
*/
        Map resultMap = new HashMap();
       String uploadPath = "F:/java/project/images/";
        String fileName = uploadFile(uploadPath,file);

       /* SysQrCode code = new SysQrCode();
        code.setPath(fileName);
        code.setStatus("1");
        code.setCtime(new Date());
        code.setCtime(new Date());
        sysQrCodeLogic.save(code);
        resultMap.put("path",fileName);*/
        return R.ok(resultMap);
    }


    private String uploadFile(String uploadPath,MultipartFile file){
        InputStream inputStream = null;
        OutputStream os = null;
        String path = null;
        String fileName = new Date().getTime()+"_"+file.getOriginalFilename();
        try{
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            File tempFile = new File(uploadPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            inputStream = file.getInputStream();
            path = tempFile.getPath() + File.separator + fileName;
            os = new FileOutputStream(path);
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            // 完毕，关闭所有链接
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  fileName;
    }

/*    @ApiOperation(value = "批量上传文件")
    @PostMapping("uploadimgs")
    public R<Map> uploadImages1(@RequestParam("file") MultipartFile[] files){
        GlobalConfig config = globalConfigLogic.getByGlobalKey("admin.images.uploadPath");
        String uploadPath = config.getGlobalValue();
//        uploadPath = "c:/java/project/images/";
        Map resultMap = new HashMap();
        uploadPath = "c:/java/project/images/";
        for(MultipartFile file:files){
            String fileName = uploadFile(uploadPath,file);
            SysQrCode code = new SysQrCode();
            code.setPath(fileName);
            code.setStatus("1");
            code.setCtime(new Date());
            code.setCtime(new Date());
            sysQrCodeLogic.save(code);
        }

        resultMap.put("path","");
        return R.ok(resultMap);

    }*/
}
