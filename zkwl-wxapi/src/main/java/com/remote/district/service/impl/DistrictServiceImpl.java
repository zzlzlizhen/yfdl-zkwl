package com.remote.district.service.impl;

import com.remote.district.dao.DistrictMapper;
import com.remote.district.entity.DistrictEntity;
import com.remote.district.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/7/29 13:16
 * @Version 1.0
 **/
@Service
public class DistrictServiceImpl implements DistrictService {

    @Autowired
    private DistrictMapper districtMapper;

    @Override
    public List<DistrictEntity> queryDistrictByType(Integer type) {
        return districtMapper.queryDistrictByType(type);
    }

    @Override
    public DistrictEntity queryDistrictById(Integer id) {
        return districtMapper.queryDistrictById(id);
    }
}
