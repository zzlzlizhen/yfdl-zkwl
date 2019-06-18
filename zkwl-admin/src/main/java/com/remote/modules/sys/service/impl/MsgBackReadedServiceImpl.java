package com.remote.modules.sys.service.impl;

import com.remote.common.utils.Constant;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.Query;
import com.remote.modules.sys.dao.MsgBackReadedDao;
import com.remote.modules.sys.entity.MsgBackReadedEntity;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.MsgBackReadedService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("msgBackReadedService")
public class MsgBackReadedServiceImpl extends ServiceImpl<MsgBackReadedDao, MsgBackReadedEntity> implements MsgBackReadedService {

    @Override
    public PageUtils queryPage(Map<String, Object> params, SysUserEntity curUser) {
        String parentId = curUser.getUserId().toString();
        IPage<MsgBackReadedEntity> page = this.page(
                new Query<MsgBackReadedEntity>().getPage(params),
                new QueryWrapper<MsgBackReadedEntity>()
                        .like("parent_id",parentId).or().like("parent_id",parentId+",%")
                        .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);
    }

    @Override
    public void insert(MsgBackReadedEntity msgBackReadedEntity) {
        baseMapper.insert(msgBackReadedEntity);
    }



    @Override
    public void delete(String backId) {
       baseMapper.delete(new QueryWrapper<MsgBackReadedEntity>().eq("msg_back_id",backId));
    }

    @Override
    public List<MsgBackReadedEntity> queryBackIds(List<String> backIds, Long uid) {
        return  baseMapper.selectList( new QueryWrapper<MsgBackReadedEntity>().in("msg_back_id",backIds).eq("uid",uid));
    }

}
