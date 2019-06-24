package com.remote.modules.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remote.common.utils.*;
import com.remote.modules.sys.dao.FeedbackDao;
import com.remote.modules.sys.entity.FeedbackEntity;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.FeedbackService;
import com.remote.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        if(CollectionUtils.isNotEmpty(sysUserEntitys)){
            for(SysUserEntity sysUserEntity : sysUserEntitys){
                userIds.add(sysUserEntity.getUserId());
            }
        }

        /*IPage<FeedbackEntity> page = this.page(
                new Query<FeedbackEntity>().getPage(params),
                new QueryWrapper<FeedbackEntity>().in("uid",userIds)
        );
        PageUtils pageUtils = new PageUtils(page);
        if(CollectionUtils.isEmpty(pageUtils.getList())){
            return pageUtils;
        }
        List<FeedbackEntity> feedbackEntities = (List<FeedbackEntity>)pageUtils.getList();
        //填充反馈列表中的用户头像和用户名
        List<Long> feedbackUserIds = new ArrayList<Long>();
        for(FeedbackEntity feedbackEntity:feedbackEntities){
            if(!feedbackUserIds.contains(feedbackEntity.getUid())){
                feedbackUserIds.add(feedbackEntity.getUid());
            }
        }
        Map<Long,SysUserEntity> map = CollectionUtils.getMapByList(sysUserService.listByIds(feedbackUserIds));
        for(FeedbackEntity feedbackEntity:feedbackEntities){
            SysUserEntity sysUserEntity = map.get(feedbackEntity.getUid());
            if(sysUserEntity != null){
                feedbackEntity.setHeadUrl(sysUserEntity.getHeadUrl());
                feedbackEntity.setUserName(sysUserEntity.getUsername());
            }
        }*/
        //return new PageUtils(page);
        int page = 1;
        if(params.get(Constant.PAGE) != null){
            page = Integer.parseInt((String)params.get(Constant.PAGE));
        }
                ;
        int limit = 10;
        if(params.get(Constant.LIMIT) != null){
            limit = Integer.parseInt((String)params.get(Constant.LIMIT));
        }

        int offset = (page-1)*limit;
        params.put("offset",offset);
        params.put("userIds",userIds);
        params.put("limit",limit);
        List<FeedbackEntity> list = this.feedbackDao.queryPageList(params);
        Integer total = this.feedbackDao.queryPageCount(params);
        return new PageUtils(list,total,page,limit);
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
