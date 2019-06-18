package com.remote.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.remote.common.utils.PageUtils;
import com.remote.modules.sys.entity.SecurityEntity;

import java.util.Map;

/**
 * 
 *
 * @author zsm
 * @date 2019-06-06 10:32:51
 */
public interface SecurityService extends IService<SecurityEntity> {

    PageUtils queryPage(Map<String, Object> params);
    SecurityEntity querySecurity(String contact,String checkCode);
}

