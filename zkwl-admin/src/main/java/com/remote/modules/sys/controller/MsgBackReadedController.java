package com.remote.modules.sys.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.remote.common.utils.PageUtils;
import com.remote.common.utils.R;
import com.remote.common.validator.ValidatorUtils;
import com.remote.modules.sys.entity.MsgBackReadedEntity;
import com.remote.modules.sys.service.MsgBackReadedService;
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
@RequestMapping("sys/msgbackreaded")
public class MsgBackReadedController extends AbstractController{
    @Autowired
    private MsgBackReadedService msgBackReadedService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:msgbackreaded:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = msgBackReadedService.queryPage(params,getUser());

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:msgbackreaded:info")
    public R info(@PathVariable("id") Long id){
        MsgBackReadedEntity msgBackReaded = msgBackReadedService.getById(id);

        return R.ok().put("msgBackReaded", msgBackReaded);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:msgbackreaded:save")
    public R save(MsgBackReadedEntity msgBackReaded){
        msgBackReaded.setUid(this.getUserId());
        msgBackReaded.setMsgBackId(UUID.randomUUID().toString().replace("-", ""));
        msgBackReaded.setCreateTime(new Date());
        msgBackReadedService.save(msgBackReaded);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:msgbackreaded:update")
    public R update(MsgBackReadedEntity msgBackReaded){
        ValidatorUtils.validateEntity(msgBackReaded);
        msgBackReadedService.updateById(msgBackReaded);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:msgbackreaded:delete")
    public R delete(@RequestBody Long[] ids){
        msgBackReadedService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
