package com.remote.modules.faultlog.controller;

import com.remote.common.utils.R;
import com.remote.modules.faultlog.entity.FaultlogEntity;
import com.remote.modules.faultlog.service.FaultlogService;
import com.remote.modules.group.entity.GroupEntity;
import com.remote.modules.sys.controller.AbstractController;
import com.remote.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * @Author zhangwenping
 * @Date 2019/6/19 17:47
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fun/faultlog")
public class FaultlogController extends AbstractController {

    @Autowired
    private FaultlogService faultlogService;

    @RequestMapping(value = "/queryFaultlog", method= RequestMethod.GET)
    public R queryFaultlog(String deviceId,Integer status){
        //1 故障日志  2 操作日志
        return R.ok(faultlogService.queryFaultlogByDeviceId(deviceId,status));
    }

}
