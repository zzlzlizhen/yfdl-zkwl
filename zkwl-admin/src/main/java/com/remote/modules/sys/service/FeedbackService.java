package com.remote.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.remote.common.utils.PageUtils;
import com.remote.modules.sys.entity.FeedbackEntity;
import com.remote.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zsm
 * @date 2019-06-06 10:32:51
 */
public interface FeedbackService extends IService<FeedbackEntity> {

    PageUtils queryPage(Map<String, Object> params, SysUserEntity curUser);
    /***
     * 通过当前用户的下级用户id找到当前用户要展示的反馈列表
     * */
    List<FeedbackEntity> queryBackList(List<Long> userIds);

    PageInfo<FeedbackEntity> queryBackList(List<Long> userIds, SysUserEntity curUser);
}

