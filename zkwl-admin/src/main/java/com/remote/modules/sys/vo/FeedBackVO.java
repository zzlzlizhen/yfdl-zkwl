package com.remote.modules.sys.vo;

public class FeedBackVO {
    private String backId;
    /**
     * 反馈用户id
     */
    private Long uid;
    /**
     * 反馈内容
     */
    private String backContent;
    /**
     * 时间
     */
    private String backCreateTime;
    /**
     * 反馈解答内容
     */
    private String answerContent;
    /**
     * 反馈解答人
     */
    private String answerUser;
    /**
     * 反馈解答时间
     */
    private String answerCreateTime;

    /**
     * 下级用户ids
     *
     * */
    private String subIds;
}
