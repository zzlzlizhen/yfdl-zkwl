package com.remote.common.es.utils;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author zhangwenping
 * @Date 2019/8/6 15:57
 * @Version 1.0
 **/
@Component
public class ESUtil {
    private Logger log = LoggerFactory.getLogger(ESUtil.class);
    @Autowired
    private RestHighLevelClient restHighLevelClient;


    public RestStatus addES(Map<String,Object> temp) {
        try{
            IndexRequest request=new IndexRequest("device_index");
            request.id(temp.get("deviceId").toString()).opType("create").source(temp);
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
            IndexResponse index = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            return index.status();
        }catch (Exception e){
            log.error("添加es：",e);
        }
        return null;
    }

    public RestStatus updateES(Map<String,Object> temp,String id) {
        try{
            UpdateRequest request=new UpdateRequest ("device_index",id);
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
            request.doc(temp);
            UpdateResponse update = restHighLevelClient.update(request, RequestOptions.DEFAULT);

            return update.status();
        }catch (Exception e){
            log.error("更新es"+id+":",e);
        }
        return null;
    }

    public List<Map<String,Object>> queryDevice(SearchSourceBuilder searchSourceBuilder){
        List<Map<String, Object>> listES = new ArrayList<>();
        try{
            SearchRequest request = new SearchRequest("device_index");
            //排序
            searchSourceBuilder.sort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));
            request.source(searchSourceBuilder);
            for (SearchHit s : restHighLevelClient.search(request, RequestOptions.DEFAULT).getHits().getHits()) {
                listES.add(s.getSourceAsMap());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listES;

    }

    public List<Map<String,Object>> queryAllDevice(SearchSourceBuilder searchSourceBuilder){
        List<Map<String, Object>> listES = new ArrayList<>();
        try{
            SearchRequest request = new SearchRequest("device_index");
            ///设置每批读取的数据量 待改进
            searchSourceBuilder.size(10000000);
            //排序
            searchSourceBuilder.sort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));

            request.source(searchSourceBuilder);
            for (SearchHit s : restHighLevelClient.search(request, RequestOptions.DEFAULT).getHits().getHits()) {
                listES.add(s.getSourceAsMap());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listES;

    }




}
