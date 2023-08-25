package org.example.interact.utils;

/**
 * @author carey
 */
public class StatusConstant {

    public enum  StatusEnum{
        SUCCESS(0,"成功"),FAIL(1,"失败");
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
}
