package com.remote.modules.sys.service;

import com.remote.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

public interface FileUpAndDownService {

    R uploadPicture(MultipartFile file) throws Exception;
}
