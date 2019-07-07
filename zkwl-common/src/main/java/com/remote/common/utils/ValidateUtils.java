package com.remote.common.utils;


import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author:zhangwenping
 */
public class ValidateUtils {

    private static final Logger logger = LoggerFactory.getLogger(ValidateUtils.class);

    /**
     * @description:(单个对象的属性校验-无提示信息)
     * @author:gjz
     * @param:[parameterList, object]
     * @date:18/9/8
     */
    public static void validate(Object object,List<String> parameterList)throws Exception{

        Preconditions.checkArgument(checkNull(object)!=null,"校验参数对象不能为空");
        for (String param:parameterList){
            checkArgument(param,"",object);
        }
    }
    /**
     * @description:(单个对象的属性校验-有提示信息)
     * @author:gjz
     * @param:[parameterList, object]
     * @date:18/9/8
     */
    public static void validate(Object object,List<String> parameterList,Map<String,String> messageMap)throws Exception{
        Preconditions.checkArgument(checkNull(object)!=null,"校验参数对象不能为空");
        for (String param:parameterList){
            String clazzName = object.getClass().getSimpleName();
            checkArgument(param,messageMap.get(clazzName+":"+param),object);
        }
    }
    /**
     * @description:(多个对象校验--有提示语)
     * @author:gjz
     * @param:[map]
     * @date:18/9/8
     */
    public static void validate(Map<String,String>messageMap,List<Object> parentList)throws Exception{
        Preconditions.checkArgument(checkNull(parentList)!=null,"校验参数对象不能为空");
        for (Object object:parentList){
            String clazzName = object.getClass().getSimpleName();
            Preconditions.checkArgument(checkNull(object)!=null,StringUtils.isEmpty(messageMap.get(clazzName))?clazzName+"不能为空":messageMap.get(clazzName)+"不能为空");
        }
    }
    /**
     * @description:(都对象多属性校验)
     * @author:gjz
     * @param:[parentList, childrenMap, messageMap]
     * @date:18/9/8
     */
    public static void validate(List<Object> parentList,Map<Object,List<String>> childrenMap,Map<String,String> messageMap)throws Exception{
        /** 校验对象是否为空*/
        if (CollectionUtils.isEmpty(parentList) && childrenMap.isEmpty()){
           Preconditions.checkArgument(false,"校验参数不能为空");
       }
        /** 校验多对象是否为空*/
        if (CollectionUtils.isNotEmpty(parentList)){
            validate(messageMap,parentList);
        }
        /** 校验多对象多属性是否为空*/
        if (!childrenMap.isEmpty()){
            for (Object object:childrenMap.keySet()){
                validate(object,childrenMap.get(object),messageMap);
            }
        }
    }

    private static void checkArgument(String param,String message,Object object)throws Exception{
        Object object1 =getFieldValueByName(param,object);
        Preconditions.checkArgument(object1!=null , StringUtils.isEmpty(message)?param+"不能为空":message);
    }

    private static Object getFieldValueByName(String fieldName, Object o) throws Exception{

        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        Class clazz = o.getClass();
        Method method = clazz.getMethod(getter, new Class[] {});
        Object value = method.invoke(o, new Object[] {});
        return checkNull(value);

    }

    private static Object checkNull(Object object){
        if (object instanceof Collection && CollectionUtils.isEmpty((Collection) object)){
            return null;
        }else if (object instanceof Map && CollectionUtils.isEmpty(((Map) object).entrySet())){
            return null;
        }else if (object instanceof String && StringUtils.isEmpty((String)object)){
            return null;
        }
      return object;
    }

    public static void main(String[] args) {
//        OrderInfoDto orderInfoDto = new OrderInfoDto();
//        orderInfoDto.setInformUserName("哈哈");
//        ExtInfoDto extInfoDto = new ExtInfoDto();
//        extInfoDto.setReferOrderId("asdf");
//        extInfoDto.setReviewMemo("asdwewe");
//        List<Object> parentList = new ArrayList<>();
//        Object []obj = {orderInfoDto,extInfoDto};
//        parentList.add(obj);
//        Map<Object,List<String>> childrenMap =new HashMap<>(1);
//        childrenMap.put(orderInfoDto,Arrays.asList("informUserName","informMobile"));
//        childrenMap.put(extInfoDto,Arrays.asList("referOrderId","reviewMemo"));
//        Map<String,String> messageMap = new HashMap<>(1);
//        try {
//            validate(parentList,childrenMap,messageMap);
//        }catch (Exception e){
//            e.printStackTrace();
//        }



    }

}
