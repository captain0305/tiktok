package org.example.interact.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;
import org.example.interact.dto.UserDto;
import org.example.interact.utils.StatusEnum;

@Data
public class UserInfoVo {
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
     * 用户信息
     */
    private UserDto userDto;

    public static UserInfoVo success() {
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setStatusCode(StatusEnum.QUERY_SUCCESS.getCode());
        userInfoVo.setStatusMsg(StatusEnum.QUERY_SUCCESS.getMsg());
        return userInfoVo;
    }

    public static UserInfoVo fail() {
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setStatusCode(StatusEnum.QUERY_FAIL.getCode());
        userInfoVo.setStatusMsg(StatusEnum.QUERY_FAIL.getMsg());
        return userInfoVo;
    }
}


