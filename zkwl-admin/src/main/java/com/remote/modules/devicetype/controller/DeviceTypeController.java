package com.remote.modules.devicetype.controller;

import com.remote.common.utils.R;
import com.remote.modules.devicetype.entity.DeviceTypeEntity;
import com.remote.modules.devicetype.service.DeviceTypeService;
import com.remote.modules.sys.controller.AbstractController;
import com.remote.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhangwenping
 * @Date 2019/9/19 14:47
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fun/deviceType")
public class DeviceTypeController extends AbstractController {

    @Autowired
    private DeviceTypeService deviceTypeService;

    @RequestMapping(value = "/add", method= RequestMethod.POST)
    public R queryDevice(@RequestBody DeviceTypeEntity deviceTypeEntity) throws Exception {
        SysUserEntity user = getUser();
        deviceTypeEntity.setCreateUser(user.getUserId());
        DeviceTypeEntity entity = deviceTypeService.getDeviceTypeByCode(deviceTypeEntity.getDeviceTypeCode(), 1);
        if(entity != null){
            return R.error(101,"设备编号重复");
        }
        return R.ok(deviceTypeService.addDeviceType(deviceTypeEntity));
    }


    @RequestMapping(value = "/getDeviceType", method= RequestMethod.GET)
    public R getDeviceType(){
        return R.ok(deviceTypeService.getDeviceType(1));
    }

}
