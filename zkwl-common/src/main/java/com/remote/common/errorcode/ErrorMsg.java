package com.remote.common.errorcode;

import com.remote.common.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorMsg {

    public static R errorMsg(Class cl,String errorType,String msg){
        Logger  logger = LoggerFactory.getLogger(cl);
        StringBuffer stringBuffer = new StringBuffer("类名:").append(cl.getName()).append(";-----错误码:").append(errorType).append(";-----错误信息:").append(msg);
        logger.error(stringBuffer.toString());
        return R.error(msg);
    }
}
