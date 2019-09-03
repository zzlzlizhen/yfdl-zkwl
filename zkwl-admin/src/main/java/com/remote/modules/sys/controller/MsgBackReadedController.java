package com.remote.modules.sys.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import com.remote.common.errorcode.ErrorCode;
import com.remote.common.errorcode.ErrorMsg;
import com.remote.common.utils.PageUtils;
import com.remote.common.utils.R;
import com.remote.common.validator.ValidatorUtils;
import com.remote.modules.sys.entity.MsgBackReadedEntity;
import com.remote.modules.sys.service.MsgBackReadedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能模块：反馈回复
 * @author zsm
 * @date 2019-06-06 10:32:51
 */
@RestController
@RequestMapping("sys/msgbackreaded")
public class MsgBackReadedController extends AbstractController{
    @Autowired
    private MsgBackReadedService msgBackReadedService;
    private Class mrc= MsgBackReadedController.class;
    /**
     * 列表
     */
    @RequestMapping("/list")
    /*@RequiresPermissions("sys:msgbackreaded:list")*/
    public R list(@RequestParam Map<String, Object> params){
        String msg = "";
        try {
            PageUtils page = msgBackReadedService.queryPage(params,getUser());
            return R.ok().put("page", page);
        }catch (Exception e){
            e.printStackTrace();
            return ErrorMsg.errorMsg(mrc,ErrorCode.ABNORMAL,"反馈回复列表查询异常");
        }
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
/*    @RequiresPermissions("sys:msgbackreaded:info")*/
    public R info(@PathVariable("id") Long id){
        if(id == null){
            return ErrorMsg.errorMsg(mrc,ErrorCode.NOT_EMPTY,"反馈id不能为空");
        }
        MsgBackReadedEntity msgBackReaded = msgBackReadedService.getById(id);
        if(msgBackReaded == null){
            return ErrorMsg.errorMsg(mrc,ErrorCode.NOT_EMPTY,"反馈内容查询为空");
        }
        return R.ok().put("msgBackReaded", msgBackReaded);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
/*    @RequiresPermissions("sys:msgbackreaded:save")*/
    public R save(MsgBackReadedEntity msgBackReaded){
        msgBackReaded.setUid(this.getUserId());
        msgBackReaded.setMsgBackId(UUID.randomUUID().toString().replace("-", ""));
        msgBackReaded.setCreateTime(new Date());
        boolean falg = msgBackReadedService.save(msgBackReaded);
        if(!falg){
            return ErrorMsg.errorMsg(mrc,ErrorCode.FAIL,"反馈信息保存失败");
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
/*    @RequiresPermissions("sys:msgbackreaded:update")*/
    public R update(MsgBackReadedEntity msgBackReaded){
        ValidatorUtils.validateEntity(msgBackReaded);
        boolean falg = msgBackReadedService.updateById(msgBackReaded);
        if(!falg){
            return ErrorMsg.errorMsg(mrc,ErrorCode.FAIL,"更新反馈信息失败");
        }
        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
/*    @RequiresPermissions("sys:msgbackreaded:delete")*/
    public R delete(@RequestBody Long[] ids){
        boolean falg = msgBackReadedService.removeByIds(Arrays.asList(ids));
        if(!falg){
            return ErrorMsg.errorMsg(mrc,ErrorCode.FAIL,"删除反馈信息失败");
        }
        return R.ok();
    }
}
