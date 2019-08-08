package com.remote.common.es.utils;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author zhangwenping
 * @Date 2019/8/6 15:57
 * @Version 1.0
 **/
@Component
public class ESUtil {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    public IndexResponse addES(Map<String,Object> temp) throws Exception{
        IndexRequest request=new IndexRequest("device_index");
        request.id(temp.get("deviceId").toString()).opType("create").source(temp);
        return restHighLevelClient.index(request,RequestOptions.DEFAULT);
    }

}
