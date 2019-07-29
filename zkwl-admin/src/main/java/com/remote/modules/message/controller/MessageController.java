package com.remote.modules.message.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import com.remote.common.utils.IdGenerate;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.R;
import com.remote.common.validator.ValidatorUtils;
import com.remote.modules.message.entity.MessageEntity;
import com.remote.modules.message.service.MessageService;
import com.remote.modules.sys.controller.AbstractController;
import com.remote.modules.sys.service.MsgBackReadedService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




/**
 * @author zsm
 */
@RestController
@RequestMapping("msg/message")
public class MessageController extends AbstractController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private MsgBackReadedService msgBackReadedService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = messageService.queryPage(params,getUser());
        return R.ok().put("page", page);
    }

    /**
     * 查询当前用户未读消息总数
     * */
    @RequestMapping(value = "/queryCount")
    public R queryCount(){
        int total = messageService.queryCount(getUserId());
        int queryCount = msgBackReadedService.queryCount(getUserId(),2);
        int isRead = total - queryCount;
        if(total < queryCount){
            return R.error("信息错误");
        }

        return R.ok().put("queryCount",isRead);
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{msgId}")
   /* @RequiresPermissions("sys:message:info")*/
    public R info(@PathVariable("msgId") String msgId){
        MessageEntity message = messageService.getById(msgId);

        return R.ok().put("message", message);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
/*    @RequiresPermissions("sys:message:save")*/
    public R save(MessageEntity message){
        message.setMsgId(IdGenerate.getUUIDString());
        messageService.save(message);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
/*    @RequiresPermissions("sys:message:update")*/
    public R update(MessageEntity message){
        ValidatorUtils.validateEntity(message);
        messageService.updateById(message);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  /*  @RequiresPermissions("sys:message:delete")*/
    public R delete(String[] msgIds){
        messageService.removeByIds(Arrays.asList(msgIds));
        return R.ok();
    }
}
