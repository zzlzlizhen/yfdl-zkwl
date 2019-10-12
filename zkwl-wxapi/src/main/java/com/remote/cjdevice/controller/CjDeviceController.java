package com.remote.cjdevice.controller;


import com.github.pagehelper.PageInfo;
import com.remote.cjdevice.entity.CjDevice;
import com.remote.cjdevice.service.CjDeviceService;
import com.remote.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/9/6 10:57
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fun/cjdevice")
public class CjDeviceController{

    @Autowired
    private CjDeviceService cjDeviceService;


    @RequestMapping(value = "/add", method= RequestMethod.POST)
    public R add(@RequestBody CjDevice cjDevice) throws Exception {
        cjDevice.setCreateUser(cjDevice.getCreateUser());
        cjDevice.setCreateTime(new Date());
        boolean b = cjDeviceService.saveCjDevice(cjDevice);
        if(!b){
            return R.error(400,"添加设备失败");
        }
        return R.ok();
    }



    @RequestMapping(value = "/delete", method= RequestMethod.GET)
    public R delete(String deviceCodes) {
        List<String> deviceCodeList = Arrays.asList(deviceCodes.split(","));
        boolean b = cjDeviceService.deleteCjDeviceByCodes(deviceCodeList);
        if (!b) {
            return R.error(400, "删除设备失败");
        }
        return R.ok();
    }



    @RequestMapping(value = "/updateDevice", method= RequestMethod.POST)
    public R updateDevice(@RequestBody CjDevice cjDevice) throws Exception {
        cjDevice.setUpdateUser(cjDevice.getCreateUser());
        cjDevice.setUpdateTime(new Date());
        boolean b = cjDeviceService.updateById(cjDevice);
        if (!b) {
            return R.error(400, "修改设备失败");
        }
        return R.ok();
    }

    @RequestMapping(value = "/queryDevice", method= RequestMethod.GET)
    public R queryDevice(@RequestParam(value="pageNum",defaultValue="1")Integer pageNum,
                         @RequestParam(value="pageSize",defaultValue="10")Integer pageSize,
                         Long createUser) throws Exception {
        if(createUser == null){
            return R.error(400,"账号id不能为空");
        }
        PageInfo<CjDevice> pageInfo = cjDeviceService.queryDeviceByMysql(pageNum,pageSize,createUser);
        if(pageInfo != null){
            return R.ok(pageInfo);
        }
        return R.error(400,"查询设备失败");
    }
}
