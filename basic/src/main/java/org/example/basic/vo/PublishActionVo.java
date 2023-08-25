package org.example.basic.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;
import org.example.basic.utils.StatusEnum;


@Data
public class PublishActionVo {
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

    public static PublishActionVo success() {
        PublishActionVo publishActionVo = new PublishActionVo();
        publishActionVo.setStatusCode(StatusEnum.PUBLISH_SUCCESS.getCode());
        publishActionVo.setStatusMsg(StatusEnum.PUBLISH_SUCCESS.getMsg());
        return publishActionVo;
    }

    public static PublishActionVo fail() {
        PublishActionVo publishActionVo = new PublishActionVo();
        publishActionVo.setStatusCode(StatusEnum.PUBLISH_FAIL.getCode());
        publishActionVo.setStatusMsg(StatusEnum.PUBLISH_FAIL.getMsg());
        return publishActionVo;
    }
}
