package com.remote.modules.sys.controller;

import java.util.*;

import com.remote.common.utils.IdGenerate;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.R;
import com.remote.common.validator.ValidatorUtils;
import com.remote.modules.sys.entity.FeedbackEntity;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.FeedbackService;
import com.remote.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping("sys/feedback")
public class FeedbackController extends AbstractController{
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:feedback:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = feedbackService.queryPage(params,getUser());

        return R.ok().put("page", page);
    }

    /**
     * 查询出所有用户uid在当前用户的ids中的反馈信息
     * */
    @RequestMapping("/backList")
    public R backList(@RequestParam Map<String, Object> params){
  /*      List<SysUserEntity> sysUserEntitys = sysUserService.queryChild(getUser());
        List<FeedbackEntity> feedbackEntities = new ArrayList<FeedbackEntity>();
        List<Long> userIds = new ArrayList<Long>();

        if(sysUserEntitys.isEmpty()){
            for(SysUserEntity sysUserEntity : sysUserEntitys){
                userIds.add(sysUserEntity.getUserId());
            }
            feedbackEntities = feedbackService.queryBackList(userIds);
            if(feedbackEntities.isEmpty()){
                for(FeedbackEntity feedbackEntity: feedbackEntities){
                    params.put("uid",feedbackEntity.getUid());
                }

            }
        }*/
        PageUtils page = feedbackService.queryPage(params,getUser());

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{backId}")
    @RequiresPermissions("sys:feedback:info")
    public R info(@PathVariable("backId") String backId){
        FeedbackEntity feedback = feedbackService.getById(backId);

        return R.ok().put("feedback", feedback);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:feedback:save")
    public R save(FeedbackEntity feedback){
        feedback.setUid(this.getUserId());
        feedback.setBackId(IdGenerate.getUUIDString());
        feedbackService.save(feedback);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:feedback:update")
    public R update( FeedbackEntity feedback){
        ValidatorUtils.validateEntity(feedback);
        feedbackService.updateById(feedback);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:feedback:delete")
    public R delete(String ids){
        if(StringUtils.isBlank(ids)){
            return R.error("数据不能为空");
        }
        String[] backIds = ids.split(",");
        for(String backId : backIds){
            feedbackService.removeById(backId);
        }
        /*feedbackService.removeByIds(Arrays.asList(backIds));*/

        return R.ok();
    }

}
