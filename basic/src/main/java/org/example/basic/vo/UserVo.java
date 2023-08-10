package org.example.basic.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;
import org.example.basic.utils.StatusEnum;


/**
 * 用户登陆注册返回对象
 */
@Data
public class UserVo {
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
     * 用户鉴权token
     */
    private String token;
    /**
     * 用户id
     */
    @Setter(onMethod_ = {@JsonProperty("user_id")})
    private long userid;

    public static UserVo success() {
        UserVo userVo = new UserVo();
        userVo.setStatusCode(StatusEnum.REGIST_SUCCESS.getCode());
        userVo.setStatusMsg(StatusEnum.REGIST_SUCCESS.getMsg());
        return userVo;
    }

    public static UserVo fail() {
        UserVo userVo = new UserVo();
        userVo.setStatusCode(StatusEnum.REGIST_FAIL.getCode());
        userVo.setStatusMsg(StatusEnum.REGIST_FAIL.getMsg());
        return userVo;
    }
}
