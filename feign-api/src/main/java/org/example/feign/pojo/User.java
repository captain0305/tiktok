package org.example.feign.pojo;

import lombok.Data;

@Data
public class User {
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户个人页顶部大图
     */
    private String backgroundImage;
    /**
     * 喜欢数
     */
    private long favoriteCount;
    /**
     * 关注总数
     */
    private long followCount;
    /**
     * 粉丝总数
     */
    private long followerCount;
    /**
     * 用户id
     */
    private long id;
    /**
     * true-已关注，false-未关注
     */
    private boolean isFollow;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 个人简介
     */
    private String signature;
    /**
     * 获赞数量
     */
    private String totalFavorited;
    /**
     * 作品数
     */
    private long workCount;
}
