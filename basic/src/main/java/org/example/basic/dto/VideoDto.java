package org.example.basic.dto;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;


@Data
public class VideoDto {

    @Setter(onMethod_ = {@JsonProperty("id")})
    private Long videoId;

    /**
     * 作者id 创建时token携带
     */
    @Setter(onMethod_ = {@JsonProperty("author")})
    private UserDto author;

    /**
     * 视频标题
     */
    @Setter(onMethod_ = {@JsonProperty("title")})
    private String title;

    /**
     * 视频地址——必须在创建时一同上传？这俩接口没给
     */
    @Setter(onMethod_ = {@JsonProperty("play_url")})
    private String playUrl;

    /**
     * 封面地址——必须在创建时一同上传？这俩接口没给请求路径
     */
    @Setter(onMethod_ = {@JsonProperty("cover_url")})
    private String coverUrl;

    /**
     * 获赞总数
     */
    @Setter(onMethod_ = {@JsonProperty("favorite_count")})
    private Integer favoriteCount;

    /**
     * 评论总数
     */
    @Setter(onMethod_ = {@JsonProperty("comment_count")})
    private Integer commentCount;

    @Setter(onMethod_ = {@JsonProperty("is_favorite")})
    private boolean isFavorite;
}
