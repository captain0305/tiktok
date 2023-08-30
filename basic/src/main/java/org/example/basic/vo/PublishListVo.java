package org.example.basic.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;
import org.example.basic.dto.VideoDto;
import org.example.basic.utils.StatusEnum;

@Data
public class PublishListVo {

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
     * 视频列表
     */
    @Setter(onMethod_ = {@JsonProperty("video_list")})
    private VideoDto[] videoList;

    public static PublishListVo success() {
        PublishListVo publishListVo = new PublishListVo();
        publishListVo.setStatusCode(StatusEnum.GET_VIDEO_LIST_SUCCESS.getCode());
        publishListVo.setStatusMsg(StatusEnum.GET_VIDEO_LIST_SUCCESS.getMsg());
        return publishListVo;
    }

    public static PublishListVo fail() {
        PublishListVo publishListVo = new PublishListVo();
        publishListVo.setStatusCode(StatusEnum.GET_VIDEO_LIST_FAIL.getCode());
        publishListVo.setStatusMsg(StatusEnum.GET_VIDEO_LIST_FAIL.getMsg());
        return publishListVo;
    }
}
