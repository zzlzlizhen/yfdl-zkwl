
package com.remote.modules.sys.controller;

import com.remote.common.exception.RRException;
import com.remote.common.utils.R;
import com.remote.modules.oss.entity.SysOssEntity;
import com.remote.modules.oss.service.SysOssService;
import com.remote.modules.sys.service.FileUpAndDownService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 功能描述:
 * @author lizhen
 * @date 2019/6/18 17:30
 * @param
 * @return
 */
@RestController
@RequestMapping("sys")
public class UploadController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

	@Autowired
	private SysOssService sysOssService;

	@Autowired
	private FileUpAndDownService fileUpAndDownService;

	/**
	 * 上传文件
	 */
	@RequestMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}
		R result = new R();
		try {
			result = fileUpAndDownService.uploadPicture(file);
			//保存文件信息
			SysOssEntity ossEntity = new SysOssEntity();
			ossEntity.setUrl(result.get("url").toString());
			ossEntity.setCreateDate(new Date());
			sysOssService.save(ossEntity);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(">>>>>>图片上传异常，e={}", e.getMessage());
			if(e instanceof RRException){
				RRException rrException = (RRException)e;
				result.put("code",rrException.getCode());
				result.put("msg",rrException.getMsg());
			}
		}
		return result;
	}

}
