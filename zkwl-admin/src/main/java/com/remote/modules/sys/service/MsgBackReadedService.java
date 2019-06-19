package com.remote.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.remote.common.utils.PageUtils;
import com.remote.modules.sys.entity.MsgBackReadedEntity;
import com.remote.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zsm
 * @date 2019-06-06 10:32:51
 */
public interface MsgBackReadedService extends IService<MsgBackReadedEntity> {

    PageUtils queryPage(Map<String, Object> params,SysUserEntity curUser);
    void insert(MsgBackReadedEntity msgBackReadedEntity);
    void delete(String backId);
    List<MsgBackReadedEntity> queryBackIds(List<String> backIds, Long uid);
    MsgBackReadedEntity queryBackIdAndUid(String backId,Long uid,SysUserEntity cur);
}

