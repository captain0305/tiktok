package org.example.socialize.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;
import org.example.socialize.dto.UserDto;
import org.example.socialize.util.StatusEnum;

@Data
public class RelationVo {
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
     * 用户信息列表
     */
    @Setter(onMethod_ = {@JsonProperty("user_list")})
    private UserDto[] userList;

    public static RelationVo success(){
        RelationVo relationVo=new RelationVo();
        relationVo.setStatusCode(StatusEnum.FOLLOW_SUCCESS.getCode());
        relationVo.setStatusMsg(StatusEnum.FOLLOW_SUCCESS.getMsg());
        return relationVo;
    }

    public static RelationVo fail(){
        RelationVo relationVo=new RelationVo();
        relationVo.setStatusCode(StatusEnum.FOLLOW_FAIL.getCode());
        relationVo.setStatusMsg(StatusEnum.FOLLOW_FAIL.getMsg());
        return relationVo;
    }

}
