package org.example.interact.controller;

import org.example.interact.service.CommentService;
import org.example.interact.utils.JwtUtils;
import org.example.interact.dto.CommentDto;
import org.example.interact.vo.CommentVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author carey
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/count")
    public int commentCount(@RequestParam("videoId") Long videoId) {
        return commentService.countByVideoId(videoId);
    }


    @PostMapping("/action")
    public CommentVo postComment(
            @RequestParam("token") String token,
            @RequestParam("video_id") long videoId,
            @RequestParam("action_type") int actionType,
            @RequestParam(value = "comment_text", required = false) String commentText,
            @RequestParam(value = "comment_id", required = false) Long commentId) {

        if (!JwtUtils.verifyTokenOfUser(token)) {
            return CommentVo.fail();
        }
        long userId = JwtUtils.getUserId(token);

        CommentDto comment = commentService.postComment(userId, videoId, actionType, commentText, commentId,token);

        if (comment == null) {
            return CommentVo.fail();
        }

        CommentVo success = CommentVo.success();
        success.setComment(comment);
        return success;
    }


    @GetMapping("/list")
    public CommentVo getCommentList(@RequestParam("token") String token, @RequestParam("video_id") long videoId) {
        CommentDto[] commentList = new CommentDto[0];
        if (!JwtUtils.verifyTokenOfUser(token)) {
            return CommentVo.fail();
        }
        long userId = JwtUtils.getUserId(token);
        try {
            commentList = commentService.getCommentList(userId, videoId);
        } catch (Exception e) {
            e.printStackTrace();
            return CommentVo.fail();
        }

        CommentVo success = CommentVo.success();
        success.setCommentList(commentList);
        return success;
    }

}
