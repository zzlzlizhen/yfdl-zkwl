package com.remote.device.util;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @Author zhangwenping
 * @Date 2019/6/26 17:41
 * @Version 1.0
 **/
@Component
@Data
public class UpdateVersion {
    private Byte [] bytes;
    private Integer sum;
    private long length;
}
