package org.example.interact.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;
import org.example.interact.dto.VideoDto;
import org.example.interact.utils.StatusConstant;

/**
 * @author carey
 */
@Data
public class FavoriteVo {
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
     * 用户点赞视频列表
     */
    @Setter(onMethod_ = {@JsonProperty("video_list")})
    private VideoDto[] videoList;

    public static FavoriteVo success() {
        FavoriteVo favoriteVo = new FavoriteVo();
        favoriteVo.setStatusCode(StatusConstant.StatusEnum.SUCCESS.getCode());
        favoriteVo.setStatusMsg(StatusConstant.StatusEnum.SUCCESS.getMsg());
        return favoriteVo;
    }

    public static FavoriteVo fail() {
        FavoriteVo favoriteVo = new FavoriteVo();
        favoriteVo.setStatusCode(StatusConstant.StatusEnum.FAIL.getCode());
        favoriteVo.setStatusMsg(StatusConstant.StatusEnum.FAIL.getMsg());
        return favoriteVo;
    }
}
