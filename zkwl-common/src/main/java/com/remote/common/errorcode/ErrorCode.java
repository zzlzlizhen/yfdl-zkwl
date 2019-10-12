package com.remote.common.errorcode;

/**
 * 200统一表示成功。500统一表示系统内部程序异常。
 * 后台的错误码10000-19999，
 * 小程序的错误码20000-29999，
 * 其他的系统依次类推
 */
public class ErrorCode {
    /**
     * 1结束的代表数据的合法性和必要性检查
     * 非空
     */
    public static final String NOT_EMPTY="10001";
    /**
     * 2结束的代表库操作失败
     *失败
     */
    public static final String FAIL ="10002";
    /**
     * 错误
     */
    public static final String ERROR="10003";
    /**
     * 4结束代表数据库查询后错误检查
     *数据库查询检查
     */
    public static final String DATA_QUERY_CHECKING="10004";
    /**
     * 5结束不能修改
     */
    public static final String CAN_NOT_BE_MODIFIED="10005";
    /**
     * 6结束代表对数据库增删改查发生异常
     */
    public static final String ABNORMAL = "10006";
    /**
     * 7只用于打印普通日志
     */
    public static final String LOGGER="1007";
    /**
     * 1结束的代表数据的合法性和必要性检查
     * 非空
     */
    public static final String X_NOT_EMPTY="20001";
    /**
     * 2结束的代表库操作失败
     *失败
     */
    public static final String X_FAIL ="20002";
    /**
     * 错误
     */
    public static final String X_ERROR="20003";
    /**
     * 4结束代表数据库查询后错误检查
     *数据库查询检查
     */
    public static final String X_DATA_QUERY_CHECKING="20004";
    /**
     * 5结束不能修改
     */
    public static final String X_CAN_NOT_BE_MODIFIED="20005";
    /**
     * 6结束代表对数据库增删改查发生异常
     */
    public static final String X_ABNORMAL = "20006";

}
