package com.remote.modules.advancedsetting.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.remote.common.utils.PageUtils;
import com.remote.common.utils.R;
import com.remote.common.validator.ValidatorUtils;
import com.remote.modules.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.modules.advancedsetting.service.AdvancedSettingService;
import com.remote.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 *
 * @author zsm
 * @email 1648925727@qq.com
 * @date 2019-06-20 09:22:19
 */
@RestController
@RequestMapping("/advancedsetting")
public class AdvancedSettingController extends AbstractController{
    @Autowired
    private AdvancedSettingService advancedSettingService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = advancedSettingService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/settingInfo")
    public R info(String projectId,String groupId){
        AdvancedSettingEntity advancedSetting =  advancedSettingService.queryByProGroupId(projectId,groupId);
        return R.ok().put("advancedSetting", advancedSetting);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(AdvancedSettingEntity advancedSetting){
        advancedSetting.setCreateTime(new Date());
        advancedSetting.setUid(getUserId());
        advancedSetting.setUpdateUser(getUser().getRealName());
        advancedSettingService.save(advancedSetting);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(AdvancedSettingEntity advancedSetting){
        ValidatorUtils.validateEntity(advancedSetting);
        advancedSetting.setUpdateTime(new Date());
        advancedSetting.setUpdateUser(getUser().getRealName());
        advancedSettingService.updateById(advancedSetting);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(Long[] ids){
        advancedSettingService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
