package org.example.basic.controller;

import org.example.basic.entity.User;
import org.example.basic.entity.Video;
import org.example.basic.service.UserService;
import org.example.basic.service.VideoService;
import org.example.basic.utils.JwtUtils;
import org.example.basic.utils.aop.auth;
import org.example.basic.vo.FeignVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/video")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @auth
    @PutMapping("/operateCommentCount")
    public FeignVo operateCommentCount(@RequestParam("video_id")int videoId, @RequestParam("add_or_sub")int addOrSub, @RequestParam("token")String token){
        Video video = videoService.getById(videoId);
        if (video==null){
            return FeignVo.fail();
        }
        if (addOrSub==0) {
            Integer commentCount = video.getCommentCount();
            video.setCommentCount(commentCount+1);
        }else {
            Integer commentCount = video.getCommentCount();
            video.setCommentCount(commentCount-1);
        }
        videoService.updateById(video);
        return FeignVo.success();

    }

    @auth
    @PutMapping("/operateFavoriteCount")
    public FeignVo operateFavoriteCount(@RequestParam("video_id")int userId, @RequestParam("add_or_sub")int addOrSub, @RequestParam("token")String token){

        Video video = videoService.getById(userId);
        if (video==null){
            return FeignVo.fail();
        }
        if (addOrSub==0) {
            Integer favoriteCount = video.getFavoriteCount();
            video.setFavoriteCount(favoriteCount+1);
        }else {
            Integer favoriteCount = video.getFavoriteCount();
            video.setFavoriteCount(favoriteCount+1);
        }
        videoService.updateById(video);
        return FeignVo.success();

    }
}
