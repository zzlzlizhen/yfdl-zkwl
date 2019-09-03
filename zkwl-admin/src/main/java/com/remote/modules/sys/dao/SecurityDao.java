package com.remote.modules.sys.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.remote.modules.sys.entity.SecurityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author zsm
 * @date 2019-06-06 10:32:51
 */
@Mapper
public interface SecurityDao extends BaseMapper<SecurityEntity> {
    SecurityEntity queryEmailSecurity(@Param("contact") String contact ,@Param("securityCode") String securityCode, @Param("userId")Long userId );
    SecurityEntity querySmsSecurity(@Param("mobile") String mobile, @Param("securityCode")String securityCode,@Param("userId")Long userId);
}
