package com.remote.modules.message.service.impl;

import com.remote.common.utils.PageUtils;
import com.remote.common.utils.Query;
import com.remote.modules.message.dao.MessageDao;
import com.remote.modules.message.entity.MessageEntity;
import com.remote.modules.message.service.MessageService;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("messageService")
public class MessageServiceImpl extends ServiceImpl<MessageDao, MessageEntity> implements MessageService {
    @Autowired
    SysUserService sysUserService;
    /**
     *
     * 如果硬件返回来的设备编号。通过设备编号找到设备id，和用户id ，把用户id跟设备id保存到数据表中，
     * */
    @Override
    public PageUtils queryPage(Map<String, Object> params,SysUserEntity curUser) {
        List<SysUserEntity> sysUserEntities =sysUserService.queryAllChild(curUser);
        List<Long> userIds = new ArrayList<Long>();
        for(SysUserEntity sysUserEntity: sysUserEntities){
            userIds.add(sysUserEntity.getUserId());
        }
        IPage<MessageEntity> page = this.page(
                new Query<MessageEntity>().getPage(params),
                new QueryWrapper<MessageEntity>().in("user_id",userIds)
        );
        return new PageUtils(page);
    }
}
