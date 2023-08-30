package org.example.socialize.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Setter;
import org.example.socialize.entity.Message;
import org.example.socialize.util.StatusEnum;

@Data
public class MessageVo {
    /**
     * 状态码，0-成功，其他值-失败
     */
    @Setter(onMethod_ = {@JsonProperty("status_code")})
    private long statusCode;
    /**
     * 返回状态描述
     */
    @Setter(onMethod_ = {@JsonProperty("status_msg")})
    private String statusMsg;
    /**
     * 用户列表
     */
    @Setter(onMethod_ = {@JsonProperty("message_list")})
    private Message[] messageList;

    public static MessageVo success() {
        MessageVo messageVo = new MessageVo();
        messageVo.setStatusCode(StatusEnum.MESSAGE_SUCCESS.getCode());
        messageVo.setStatusMsg(StatusEnum.MESSAGE_SUCCESS.getMsg());
        return messageVo;
    }

    public static MessageVo fail() {
        MessageVo messageVo = new MessageVo();
        messageVo.setStatusCode(StatusEnum.MESSAGE_FAIL.getCode());
        messageVo.setStatusMsg(StatusEnum.MESSAGE_FAIL.getMsg());
        return messageVo;
    }
}
