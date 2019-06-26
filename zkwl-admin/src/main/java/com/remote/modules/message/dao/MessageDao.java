package com.remote.modules.message.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.modules.message.entity.MessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author zsm
 * @date 2019-06-06 10:32:51
 */
@Mapper
public interface MessageDao extends BaseMapper<MessageEntity> {
    int queryCount(@Param(value = "uid") Long uid);
}
