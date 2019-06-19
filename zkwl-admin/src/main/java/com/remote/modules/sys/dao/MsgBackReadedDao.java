package com.remote.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.modules.sys.entity.MsgBackReadedEntity;
import com.remote.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author zsm
 * @date 2019-06-06 10:32:51
 */
@Mapper
public interface MsgBackReadedDao extends BaseMapper<MsgBackReadedEntity> {
    /* MsgBackReadedEntity queryBackIdAndUid(String backId, Long uid,SysUserEntity cur);*/
	
}
