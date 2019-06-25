package com.remote.common.utils;

import com.remote.modules.sys.entity.SysUserEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CollectionUtils extends org.apache.commons.collections.CollectionUtils {

    public static Map<Long,SysUserEntity> getMapByList(Collection<SysUserEntity> collection){
        Map<Long,SysUserEntity> map = new HashMap<Long,SysUserEntity>();
        if(isNotEmpty(collection)){
            for(SysUserEntity sysUserEntity:collection){
                map.put(sysUserEntity.getUserId(),sysUserEntity);
            }
        }
        return map;
    }
}
