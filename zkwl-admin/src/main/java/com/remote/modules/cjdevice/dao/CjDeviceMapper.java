package com.remote.modules.cjdevice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.modules.cjdevice.entity.CjDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CjDeviceMapper extends BaseMapper<CjDevice> {
    /*
     * @Author zhangwenping
     * @Description 删除设备
     * @Date 13:52 2019/9/6
     * @Param deviceCodes
     * @return int
     **/
    int deleteCjDeviceByCodes(@Param("deviceCodes") List<String> deviceCodes);
    /*
     * @Author zhangwenping
     * @Description 修改设备
     * @Date 14:41 2019/9/6
     * @Param cjDevice
     * @return int
     **/
    int updateByDeviceId(CjDevice cjDevice);
    /*
     * @Author zhangwenping
     * @Description 根据code查询设备
     * @Date 14:49 2019/9/6
     * @Param deviceCode
     * @return CjDevice
     **/
    CjDevice queryCjDeviceByDeviceCode(String deviceCode);
    /*
     * @Author zhangwenping
     * @Description 工厂查询设备信息
     * @Date 9:42 2019/9/10
     * @Param userId
     * @return  List<CjDevice>
     **/
    List<CjDevice> queryDeviceByMysql(Long userId);
}
