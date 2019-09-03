package com.remote.modules.sys.service.impl;

import com.remote.common.utils.PageUtils;
import com.remote.common.utils.Query;
import com.remote.modules.sys.dao.SecurityDao;
import com.remote.modules.sys.entity.SecurityEntity;
import com.remote.modules.sys.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("securityService")
public class SecurityServiceImpl extends ServiceImpl<SecurityDao, SecurityEntity> implements SecurityService {
    @Autowired
    SecurityDao securityDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SecurityEntity> page = this.page(
                new Query<SecurityEntity>().getPage(params),
                new QueryWrapper<SecurityEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public SecurityEntity queryEmailSecurity(String contact ,String securityCode,Long userId ) {
        return securityDao.queryEmailSecurity(contact,securityCode,userId);
    }
    @Override
    public SecurityEntity querySmsSecurity(String mobile ,String securityCode,Long userId ) {
        return securityDao.querySmsSecurity(mobile,securityCode,userId);
    }
}
