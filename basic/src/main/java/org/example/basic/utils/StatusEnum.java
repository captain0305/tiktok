package org.example.basic.utils;

public  enum StatusEnum {
    REGIST_SUCCESS(0,"注册成功"),
    REGIST_FAIL(1,"注册失败"),
    LOGIN_SUCCESS(0,"登陆成功"),
    LOGIN_FAIL(1,"登陆失败"),
    VideoFeed_SUCCESS(0,"视频流获取成功"),
    VideoFeed_FAIL(1,"视频流获取失败"),
    QUERY_SUCCESS(0,"查询成功"),
    QUERY_FAIL(1,"查询失败"),
    PUBLISH_SUCCESS(0,"视频发布成功"),
    PUBLISH_FAIL(1,"视频发布失败"),
    GET_VIDEO_LIST_SUCCESS(0,"获取视频列表成功"),
    GET_VIDEO_LIST_FAIL(1,"获取视频列表失败"),
    FEIGN_SUCCESS(0,"操作成功"),
    FEIGN_FAIL(0,"操作失败");
    private int code;
    private String msg;

    StatusEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
