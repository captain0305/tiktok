package org.example.basic.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;
import org.example.basic.utils.StatusEnum;


@Data
public class VideoFeedVo<T> {
    @Setter(onMethod_ = {@JsonProperty("status_code")})
    private Integer code; //编码：1成功，0和其它数字为失败

    @Setter(onMethod_ = {@JsonProperty("status_msg")})
    private String msg; //错误信息

    @Setter(onMethod_ = {@JsonProperty("next_time")})
    private String nextTime;


    @Setter(onMethod_ = {@JsonProperty("video_list")})
    private T data; //数据



    public static <T> VideoFeedVo<T> success(T object) {
        VideoFeedVo<T> videoFeedVo = new VideoFeedVo<T>();
        videoFeedVo.setData(object);
        videoFeedVo.setCode(StatusEnum.VideoFeed_SUCCESS.getCode());
        videoFeedVo.setMsg(StatusEnum.VideoFeed_SUCCESS.getMsg());

        return videoFeedVo;
    }

    public static <T> VideoFeedVo<T> error() {
        VideoFeedVo<T> videoFeedVo = new VideoFeedVo<T>();
        videoFeedVo.setCode(StatusEnum.VideoFeed_FAIL.getCode());
        videoFeedVo.setMsg(StatusEnum.VideoFeed_FAIL.getMsg());

        return videoFeedVo;
    }



}

