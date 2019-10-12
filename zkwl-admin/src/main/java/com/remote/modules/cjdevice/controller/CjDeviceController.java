package com.remote.modules.cjdevice.controller;

import com.github.pagehelper.PageInfo;
import com.remote.common.utils.R;
import com.remote.modules.cjdevice.entity.CjDevice;
import com.remote.modules.cjdevice.service.CjDeviceService;
import com.remote.modules.sys.controller.AbstractController;
import com.remote.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author zhangwenping
 * @Date 2019/9/6 10:57
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fun/cjdevice")
public class CjDeviceController  extends AbstractController {

    @Autowired
    private CjDeviceService cjDeviceService;


    @RequestMapping(value = "/add", method= RequestMethod.POST)
    public R add(@RequestBody CjDevice cjDevice) throws Exception {
        SysUserEntity user = getUser();
        cjDevice.setCreateUser(user.getUserId());
        cjDevice.setCreateTime(new Date());
        boolean b = cjDeviceService.saveCjDevice(cjDevice);
        if(!b){
            return R.error(400,"添加设备失败");
        }
        return R.ok();
    }



    @RequestMapping(value = "/delete", method= RequestMethod.GET)
    public R delete(String deviceCodes) {
        SysUserEntity user = getUser();
        List<String> deviceCodeList = Arrays.asList(deviceCodes.split(","));
        boolean b = cjDeviceService.deleteCjDeviceByCodes(deviceCodeList);
        if (!b) {
            return R.error(400, "删除设备失败");
        }
        return R.ok();
    }



    @RequestMapping(value = "/updateDevice", method= RequestMethod.POST)
    public R updateDevice(@RequestBody CjDevice cjDevice) throws Exception {
        SysUserEntity user = getUser();
        cjDevice.setUpdateUser(user.getUserId());
        cjDevice.setUpdateTime(new Date());
        boolean b = cjDeviceService.updateById(cjDevice);
        if (!b) {
            return R.error(400, "修改设备失败");
        }
        return R.ok();
    }

    @RequestMapping(value = "/queryDevice", method= RequestMethod.GET)
    public R queryDevice(Integer pageNum,Integer pageSize) throws Exception {
        SysUserEntity user = getUser();
        PageInfo<CjDevice> pageInfo = cjDeviceService.queryDeviceByMysql(pageNum,pageSize,user.getUserId());
        if(pageInfo != null){
            return R.ok(pageInfo);
        }
        return R.error(400,"查询设备失败");
    }


}
