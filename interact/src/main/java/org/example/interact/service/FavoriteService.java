package org.example.interact.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.interact.entity.FavorEntity;
import org.example.interact.dto.VideoDto;

/**
 * @author carey
 */
public interface FavoriteService extends IService<FavorEntity> {
    /**
     * 点赞或取消赞
     * @param userId 用户id
     * @param videoId 视频id
     * @param actionType 操作类型 1点赞 2取消赞
     * @return 返回操作结果
     */
    boolean action(long userId, Long videoId, Integer actionType,String token);
    /**
     * 查询用户的点赞视频列表
     * @param userId 用户id
     * @param id 操作者id
     * @param token 用户token
     * @return 返回用户的点赞视频列表
     */
    VideoDto[] favoriteList(Long userId, Long id,String token);
    /**
     * 查询视频的点赞数量
     * @param videoId 视频id
     * @return 返回视频的点赞数
     */
    Integer countByVideoId(Long videoId);
    /**
     * 查询用户是否给视频点赞
     * @param userId 用户id
     * @param videoId 视频id
     * @return 返回查询结果
     */
    boolean isFavorite(Long userId, Long videoId);

}
