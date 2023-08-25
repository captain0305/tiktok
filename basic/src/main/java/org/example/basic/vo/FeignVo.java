package org.example.basic.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;
import org.example.basic.utils.StatusEnum;

@Data
public class FeignVo {

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

    public static FeignVo success() {
        FeignVo feignVo = new FeignVo();
        feignVo.setStatusCode(StatusEnum.FEIGN_SUCCESS.getCode());
        feignVo.setStatusMsg(StatusEnum.FEIGN_SUCCESS.getMsg());
        return feignVo;
    }

    public static FeignVo fail() {
        FeignVo feignVo = new FeignVo();
        feignVo.setStatusCode(StatusEnum.FEIGN_FAIL.getCode());
        feignVo.setStatusMsg(StatusEnum.FEIGN_FAIL.getMsg());
        return feignVo;
    }
}
