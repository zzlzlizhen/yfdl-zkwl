package com.remote.modules.device.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/8/27 15:02
 * @Version 1.0
 **/
@Data
public class VersionResult {
   private List<String> versionList = new ArrayList<>();

   private List<String> gprsList = new ArrayList<>();
}
