package org.example.interact.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.interact.dto.CommentDto;
import org.example.interact.entity.CommentEntity;

/**
 * @author carey
 */
public interface CommentService extends IService<CommentEntity> {
    /**
     * 查询视频的评论数量
     * @param videoId 视频id
     * @return 返回查询结果
     */
    Integer countByVideoId(Long videoId);
    /**
     * 添加或删除评论
     * @param userId 用户id
     * @param videoId 视频id
     * @param actionType 操作类型 1添加评论 2删除评论
     * @param commentText 评论内容
     * @param commentId 要删除的评论id
     * @param token 用户token
     * @return 操作结果
     */
    CommentDto postComment(long userId, long videoId, int actionType, String commentText, Long commentId,String token);
    /**
     * 查询视频的评论列表
     * @param userId 用户id
     * @param videoId 视频id
     * @return 返回查询结果
     * @throws Exception 异常处理
     */
    CommentDto[] getCommentList(long userId, long videoId) throws Exception;

}
