package com.remote.modules.sys.controller;

import java.util.*;

import com.remote.common.errorcode.ErrorCode;
import com.remote.common.utils.IdGenerate;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.R;
import com.remote.modules.sys.entity.FeedbackEntity;
import com.remote.modules.sys.entity.MsgBackReadedEntity;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.FeedbackService;
import com.remote.modules.sys.service.MsgBackReadedService;
import com.remote.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    private MsgBackReadedService msgBackReadedService;
    private String msg = "";
    Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    /**
     * 列表
     */
    @RequestMapping("/list")
  /*  @RequiresPermissions("sys:feedback:list")*/
    public R list(@RequestParam Map<String, Object> params){
        SysUserEntity curUser = getUser();
        PageUtils page = feedbackService.queryPage(params,curUser);

        return R.ok().put("page", page);
    }

    /**
     * 查询当前用户未读反馈以及回复总数
     * */
    @RequestMapping(value = "/queryCount")
    public R queryCount(){
        Map<String, Object> params = new HashMap<String, Object>();
        int total = feedbackService.queryPageCount(params,getUserId());
        int queryCount = msgBackReadedService.queryCount(getUserId(),1);
        if(total < queryCount){
            return R.error("信息错误");
        }
        int isRead = total - queryCount;
        return R.ok().put("queryCount",isRead);
    }
    /**
     * 查询出所有用户uid在当前用户的ids中的反馈信息
     * */
    @RequestMapping("/backList")
    public R backList(@RequestParam Map<String, Object> params){
        PageUtils page = feedbackService.queryPage(params,getUser());

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{backId}")
    /*@RequiresPermissions("sys:feedback:info")*/
    public R info(@PathVariable("backId") String backId){
        if(StringUtils.isBlank(backId)){
            return R.error("反馈信息id不能为空");
        }
        FeedbackEntity feedback = feedbackService.getById(backId);
        SysUserEntity sysUserEntity = null;
        try {
            sysUserEntity = sysUserService.queryById(feedback.getUid());
            if(sysUserEntity != null){
                feedback.setUsername(sysUserEntity.getUsername());
                feedback.setHeadUrl(sysUserEntity.getHeadUrl());
            }else{
                feedback.setUsername("");
                feedback.setHeadUrl("");
            }
        }catch (Exception e){
            msg = "反馈用户信息查询异常";
            logger.error(ErrorCode.ABNORMAL+msg);
            e.printStackTrace();
        }

        MsgBackReadedEntity msgBackReadedEntity = msgBackReadedService.queryBackIdAndUid(backId,getUserId());
        if(msgBackReadedEntity == null){
            //如果反馈不为空
            if(feedback != null){
                insert(feedback.getBackId());
            }
        }
        return R.ok().put("feedback", feedback);
    }
   /**
    * 通过反馈id跟uid查询是不是给自身回复
    * */
   @RequestMapping("/isSelf")//true是自身，false不是自身
   public R isSelf(@RequestParam("backId")String backId){
       boolean falg = feedbackService.isSelf(backId,getUserId());
       return R.ok().put("falg",falg);
   }

    /**
     * 保存
     */
    @RequestMapping("/save")
 /*   @RequiresPermissions("sys:feedback:save")*/
    public R save(FeedbackEntity feedback){
        if(StringUtils.isBlank(feedback.getEmail())){
            return R.error("反馈的邮箱不能为空");
        }
        if(StringUtils.isBlank(feedback.getMobile())){
            return R.error("反馈的手机号不能为空");
        }
        if(StringUtils.isBlank(feedback.getBackContent())){
            return R.error("反馈内容不能为空");
        }
        feedback.setUid(this.getUserId());
        feedback.setBackId(IdGenerate.getUUIDString());
        feedback.setBackCreateTime(new Date());
        boolean flag =  feedbackService.save(feedback);
        if(!flag){
            return R.error("反馈消息保存失败");
        }
        insert(feedback.getBackId());

        return R.ok();
    }

    /**
     * 新增反馈回复
     */
    @RequestMapping("/update")
    /*@RequiresPermissions("sys:feedback:update")*/
    public R update( FeedbackEntity feedback){
        if(feedback.getUid() == this.getUserId()){
            return R.error("不能给自己回复");
        }
        if(StringUtils.isBlank(feedback.getBackId())|| "undefined".equals(feedback.getBackId())){
            return R.error("反馈id不能为空");
        }
        if(StringUtils.isBlank(feedback.getAnswerContent())){
            return R.error("回复内容不能为空");
        }
        feedback.setAnswerUser(this.getUserId());
        feedback.setAnswerCreateTime(new Date());
        boolean flag = feedbackService.updateById(feedback);
        if(!flag){
            return R.error("反馈回复失败");
        }
        msgBackReadedService.delete(feedback.getBackId());
        insert(feedback.getBackId());

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   /* @RequiresPermissions("sys:feedback:delete")*/
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


    /**
     * 增加信息到已读表
     * */
    public void insert(String backId){
        MsgBackReadedEntity msgBackReadedEntity = new MsgBackReadedEntity();
        msgBackReadedEntity.setUid(getUserId());
        msgBackReadedEntity.setMsgBackId(backId);
        msgBackReadedEntity.setType(1);
        msgBackReadedEntity.setCreateTime(new Date());
        msgBackReadedEntity.setDeviceId("");
        msgBackReadedService.insert(msgBackReadedEntity);
    }
}
