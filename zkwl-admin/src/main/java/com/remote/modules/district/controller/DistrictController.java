package com.remote.modules.district.controller;

import com.github.pagehelper.PageInfo;
import com.remote.common.utils.R;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.entity.DeviceQuery;
import com.remote.modules.district.service.DistrictService;
import com.remote.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhangwenping
 * @Date 2019/7/29 13:15
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fun/distirct")
public class DistrictController extends AbstractController {


    @Autowired
    private DistrictService districtService;

    @RequestMapping(value = "/queryDistirct", method= RequestMethod.GET)
    public R queryDistirct()  {
        return R.ok(districtService.queryDistrictByType(1));
    }
}
