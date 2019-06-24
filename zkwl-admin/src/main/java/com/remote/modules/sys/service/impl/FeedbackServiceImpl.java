package com.remote.modules.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.Query;
import com.remote.modules.sys.dao.FeedbackDao;
import com.remote.modules.sys.entity.FeedbackEntity;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.FeedbackService;
import com.remote.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("feedbackService")
public class FeedbackServiceImpl extends ServiceImpl<FeedbackDao, FeedbackEntity> implements FeedbackService {

    @Autowired
    FeedbackDao feedbackDao;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    FeedbackService feedbackService;
    @Override
    public PageUtils queryPage(Map<String, Object> params,SysUserEntity curUser) {
       List<SysUserEntity> sysUserEntitys = sysUserService.queryChild(curUser);
        List<Long> userIds = new ArrayList<Long>();
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        if(CollectionUtils.isNotEmpty(sysUserEntitys)){
            for(SysUserEntity sysUserEntity : sysUserEntitys){
                userIds.add(sysUserEntity.getUserId());
                feedbackEntity.setUserName(sysUserEntity.getUsername());
            }
        }
        IPage<FeedbackEntity> page = this.page(
                new Query<FeedbackEntity>().getPage(params),
                new QueryWrapper<FeedbackEntity>().in("uid",userIds)
        );
        return new PageUtils(page);
    }

    @Override
    public PageInfo<FeedbackEntity> queryBackList(List<Long> userIds,SysUserEntity curUser) {
        List<SysUserEntity> sysUserEntitys = sysUserService.queryChild(curUser);
        if(CollectionUtils.isNotEmpty(sysUserEntitys)){
            if(sysUserEntitys.isEmpty()){
                for(SysUserEntity sysUserEntity : sysUserEntitys){
                    userIds.add(sysUserEntity.getUserId());
                }
            }
            PageHelper.startPage(1,10);
            List<FeedbackEntity> list = feedbackService.queryBackList(userIds);;
            PageInfo<FeedbackEntity> proPage = new PageInfo<FeedbackEntity>(list);
            return proPage;
        }
        return null;
    }


    /***
     * 通过当前用户的下级用户id找到当前用户要展示的反馈列表
     * */
    @Override
    public List<FeedbackEntity> queryBackList(List<Long> userIds) {
        return feedbackDao.queryBackList(userIds);
    }

    @Override
    public FeedbackEntity queryDetailInfo(String backId) {
        return this.baseMapper.selectOne(new QueryWrapper<FeedbackEntity>().eq("back_id",backId));
    }


}
