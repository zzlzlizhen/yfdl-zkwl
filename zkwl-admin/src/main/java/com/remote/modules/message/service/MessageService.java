package com.remote.modules.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.remote.common.utils.PageUtils;
import com.remote.modules.message.entity.MessageEntity;
import com.remote.modules.sys.entity.SysUserEntity;

import java.util.Map;

/**
 * 
 *
 * @author zsm
 * @date 2019-06-06 10:32:51
 */
public interface MessageService extends IService<MessageEntity> {

    PageUtils queryPage(Map<String, Object> params,SysUserEntity curUser);
    int queryCount(Long curUid);
}

