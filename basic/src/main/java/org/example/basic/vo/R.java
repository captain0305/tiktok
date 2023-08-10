package org.example.basic.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Data
public class R<T> {

    @Setter(onMethod_ = {@JsonProperty("status_code")})
    private Integer code; //编码：1成功，0和其它数字为失败

    @Setter(onMethod_ = {@JsonProperty("status_msg")})
    private String msg; //错误信息

    private T data; //数据


    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 0;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 1;
        return r;
    }



}
