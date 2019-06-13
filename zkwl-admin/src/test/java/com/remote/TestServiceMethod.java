package com.remote;

import com.alibaba.fastjson.JSON;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestServiceMethod {

    @Autowired
    private SysUserService sysUserService;

    @Test
    public void test1(){
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUserId(1L);
        sysUserEntity.setParentId(1L);
        sysUserEntity.setAllParentId("1");
        List<SysUserEntity> sysUserEntityList = sysUserService.queryChild(sysUserEntity);
        System.out.println("----------------------------------------------------");
        System.out.println(JSON.toJSONString(sysUserEntityList));
        System.out.println("----------------------------------------------------");
    }

    @Test
    public void test2(){
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUserId(1L);
        sysUserEntity.setParentId(1L);
        sysUserEntity.setAllParentId("1");
        List<SysUserEntity> sysUserEntityList = sysUserService.queryAllChild(sysUserEntity);
        System.out.println("----------------------------------------------------");
        System.out.println(JSON.toJSONString(sysUserEntityList));
        System.out.println("----------------------------------------------------");
    }
}
