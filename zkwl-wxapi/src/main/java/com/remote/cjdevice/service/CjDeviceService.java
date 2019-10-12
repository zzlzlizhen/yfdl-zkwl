package com.remote.cjdevice.service;

import com.github.pagehelper.PageInfo;
import com.remote.cjdevice.entity.CjDevice;

import java.util.List;

public interface CjDeviceService {
    /*
     * @Author zhangwenping
     * @Description 添加设备
     * @Date 13:40 2019/9/6
     * @Param cjDevice
     * @return boolean
     **/
    boolean saveCjDevice(CjDevice cjDevice);

    /*
     * @Author zhangwenping
     * @Description 删除设备
     * @Date 13:51 2019/9/6
     * @Param deviceCodes
     * @return boolean
     **/
    boolean deleteCjDeviceByCodes(List<String> deviceCodes);
    /*
     * @Author zhangwenping
     * @Description 修改
     * @Date 13:58 2019/9/6
     * @Param  cjDevice
     * @return boolean
     **/
    boolean updateById(CjDevice cjDevice);
    /*
     * @Author zhangwenping
     * @Description 根据code查询设备
     * @Date 14:48 2019/9/6
     * @Param deviceCode
     * @return CjDevice
     **/
    CjDevice queryCjDeviceByDeviceCode(String deviceCode);
    /*
 * @Author zhangwenping
 * @Description 厂家查询设备
 * @Date 9:35 2019/9/10
 * @Param  userId,pageNum,pageSize
 * @return  PageInfo<CjDevice>
 **/
    PageInfo<CjDevice> queryDeviceByMysql(Integer pageNum, Integer pageSize, Long userId);
}
