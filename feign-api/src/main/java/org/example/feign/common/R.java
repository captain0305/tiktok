package org.example.feign.common;

import java.util.HashMap;
import java.util.Map;

public class R<T> {
    private Integer statusCode; //编码：1成功，0和其它数字为失败

    private String statusMsg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.statusCode = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.statusMsg = msg;
        r.statusCode = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
