package org.example.basic.utils;

public enum ServiceEnum {

    LOGIN("登陆"),
    REGISTER("注册");



    private String type;
    ServiceEnum(String type){
        this.type = type;

    }



    public String getType() {
        return type;
    }
}
