package com.remote.common.es.utils;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
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

import java.lang.reflect.Field;
import java.util.*;

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

    public Map<String,Object> convertAddES(Object obj){
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        Map<String,Object> temp = new HashMap<String,Object>();
        for (Field field : declaredFields){
            field.setAccessible(true);
            try {
                if (field.getGenericType().toString().equals("class java.lang.String")) {
                    temp.put(field.getName(),field.get(obj) == null ? "" : field.get(obj));
                }else if (field.getGenericType().toString().equals("class java.lang.Integer")) {
                    temp.put(field.getName(),field.get(obj) == null ? 0 : field.get(obj));
                }else if (field.getGenericType().toString().equals("class java.lang.Double")) {
                    temp.put(field.getName(),field.get(obj) == null ? 0.0 :field.get(obj));
                }else if(field.getGenericType().toString().equals("class java.util.Date")){
                    temp.put(field.getName(),field.get(obj) == null ? new Date() :field.get(obj));
                }
            } catch (Exception e1) {
                log.error("添加es：",e1);
            }
        }
        return temp;
    }


    public Map<String,Object> convertUpdateES(Object obj){
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        Map<String,Object> temp = new HashMap<String,Object>();
        for (Field field : declaredFields){
            field.setAccessible(true);
            try {
                if(field.get(obj) != null){
                    temp.put(field.getName(),field.get(obj));
                }
            } catch (Exception e) {
                log.error("添加es：",e);
            }
        }
        return temp;
    }


    public RestStatus addES(Map<String,Object> temp,String indexName,String indexId) {
        try{
            IndexRequest request=new IndexRequest(indexName);
            request.id(indexId).opType("create").source(temp);
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
            IndexResponse index = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            return index.status();
        }catch (Exception e){
            log.error("添加es：",e);
        }
        return null;
    }

    public RestStatus updateES(Map<String,Object> temp,String indexId,String indexName) {
        try{

            UpdateRequest request=new UpdateRequest (indexName,indexId);
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
            request.doc(temp);
            UpdateResponse update = restHighLevelClient.update(request, RequestOptions.DEFAULT);

            return update.status();
        }catch (Exception e){
            log.error("更新es"+indexId+":",e);
        }
        return null;
    }



    public RestStatus addListES(List<Map<String,Object>> temp,String indexName) {
        try{
            BulkRequest request = new BulkRequest();
            for (Map<String,Object> map : temp){
                String deviceId = map.get("deviceId").toString();
                request.add(new IndexRequest(indexName).id(deviceId).opType("create").source(map));
            }
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
            BulkResponse bulk = restHighLevelClient.bulk(request,RequestOptions.DEFAULT);
            return bulk.status();
        }catch (Exception e){
            log.error("批量更新es失败:",e);
        }
        return null;
    }

    public RestStatus updateListES(List<Map<String,Object>> temp,String indexName,String indexId) {
        try{
            BulkRequest request = new BulkRequest();
            for (Map<String,Object> map : temp){
                String deviceId = map.get(indexId).toString();
                request.add(new UpdateRequest (indexName,deviceId).doc(map));
            }
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
            BulkResponse bulk = restHighLevelClient.bulk(request,RequestOptions.DEFAULT);
            return bulk.status();
        }catch (Exception e){
            log.error("批量更新es失败:",e);
        }
        return null;
    }

    public List<Map<String,Object>> queryDevice(SearchSourceBuilder searchSourceBuilder,String indexName){
        List<Map<String, Object>> listES = new ArrayList<>();
        try{
            SearchRequest request = new SearchRequest(indexName);
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

    public List<Map<String,Object>> queryAllDevice(SearchSourceBuilder searchSourceBuilder,String indexName){
        List<Map<String, Object>> listES = new ArrayList<>();
        try{
            SearchRequest request = new SearchRequest(indexName);
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
