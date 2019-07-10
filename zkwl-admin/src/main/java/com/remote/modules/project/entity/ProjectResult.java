package com.remote.modules.project.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhangwenping
 * @Date 2019/7/9 16:51
 * @Version 1.0
 **/
@Data
public class ProjectResult {

    private Integer sumCount;

    private Integer deviceCount;

    private List<Map<String,Integer>> deviceMap = new ArrayList<>();

    private List<Map<String,BigDecimal>> deviceScale = new ArrayList<>();
}
