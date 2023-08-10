package org.example.basic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;

@Data
public class UserDto {
    @Setter(onMethod_ = {@JsonProperty("id")})
    private Long userId;

    /**
     * 随用户注册上传
     */
    @Setter(onMethod_ = {@JsonProperty("name")})
    private String name;


    /**
     * 头像可以更换——默认为初始照片上传到服务器中
     */
    @Setter(onMethod_ = {@JsonProperty("avatar")})
    private String avatar;

    /**
     * 背景图片可以更换——默认为初始照片上传到服务器中
     */
    @Setter(onMethod_ = {@JsonProperty("background_image")})
    private String backgroudImage;

    /**
     * 个性签名--默认为空字符串可以更改
     */
    @Setter(onMethod_ = {@JsonProperty("signature")})
    private String sinature;

    @Setter(onMethod_ = {@JsonProperty("follow_count")})
    private long followCount;
    /**
     * 粉丝总数
     */
    @Setter(onMethod_ = {@JsonProperty("follower_count")})
    private long followerCount;

    /**
     * true-已关注，false-未关注
     */
    @Setter(onMethod_ = {@JsonProperty("is_follow")})
    private boolean isFollow;
    /**
     * 用户名称
     */
    @Setter(onMethod_ = {@JsonProperty("total_favorited")})
    private Integer totalFavorited;

    /**
     * 作品总数
     */
    @Setter(onMethod_ = {@JsonProperty("work_count")})
    private Integer workCount;

    /**
     * 获赞总数
     */
    @Setter(onMethod_ = {@JsonProperty("favorite_count")})
    private Integer favoriteCount;

}
