package org.example.basic.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;
import org.example.basic.dto.VideoDto;
import org.example.basic.entity.Video;
import org.example.basic.utils.StatusEnum;


@Data
public class VideoFeedVo {
    /**
     * 本次返回的视频中，发布最早的时间，作为下次请求时的latest_time
     */
    @Setter(onMethod_ = {@JsonProperty("next_time")})
    private Long nextTime;
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

    public static VideoFeedVo success() {
        VideoFeedVo feedVo = new VideoFeedVo();
        feedVo.setStatusCode(StatusEnum.VideoFeed_SUCCESS.getCode());
        feedVo.setStatusMsg(StatusEnum.VideoFeed_SUCCESS.getMsg());
        return feedVo;
    }

    public static VideoFeedVo fail() {
        VideoFeedVo feedVo = new VideoFeedVo();
        feedVo.setStatusCode(StatusEnum.VideoFeed_FAIL.getCode());
        feedVo.setStatusMsg(StatusEnum.VideoFeed_FAIL.getMsg());
        return feedVo;
    }


}

