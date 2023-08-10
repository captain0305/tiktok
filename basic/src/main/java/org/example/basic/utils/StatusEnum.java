package org.example.basic.utils;

public  enum StatusEnum {
    REGIST_SUCCESS(0,"注册成功"),
    REGIST_FAIL(1,"注册失败"),
    VideoFeed_SUCCESS(0,"注册成功"),
    VideoFeed_FAIL(1,"注册失败");
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
