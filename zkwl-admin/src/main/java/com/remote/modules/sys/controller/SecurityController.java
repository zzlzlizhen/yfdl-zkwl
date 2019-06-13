package com.remote.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;


import com.remote.common.utils.PageUtils;
import com.remote.common.utils.R;
import com.remote.common.validator.ValidatorUtils;
import com.remote.modules.sys.entity.SecurityEntity;
import com.remote.modules.sys.service.SecurityService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * 
 *
 * @author zsm
 * @date 2019-06-06 10:32:51
 */
@RestController
@RequestMapping("sys/security")
public class SecurityController {
    @Autowired
    private SecurityService securityService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:security:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = securityService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:security:info")
    public R info(@PathVariable("id") Long id){
        SecurityEntity security = securityService.getById(id);

        return R.ok().put("security", security);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:security:save")
    public R save(@RequestBody SecurityEntity security){
        securityService.save(security);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:security:update")
    public R update(@RequestBody SecurityEntity security){
        ValidatorUtils.validateEntity(security);
        securityService.updateById(security);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:security:delete")
    public R delete(@RequestBody Long[] ids){
        securityService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
