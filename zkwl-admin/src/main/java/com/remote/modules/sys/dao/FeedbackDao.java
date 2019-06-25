package com.remote.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.modules.sys.entity.FeedbackEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author zsm
 * @date 2019-06-06 10:32:51
 */
@Mapper
public interface FeedbackDao extends BaseMapper<FeedbackEntity> {

    List<FeedbackEntity> queryBackList(@Param("userIds") List<Long> userIds);

    Integer queryPageCount(Map<String,Object> map);

    List<FeedbackEntity> queryPageList(Map<String,Object> map);

}
