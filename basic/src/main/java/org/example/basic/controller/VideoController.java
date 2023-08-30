package org.example.basic.controller;

import org.example.basic.dto.UserDto;
import org.example.basic.dto.VideoDto;
import org.example.basic.entity.User;
import org.example.basic.entity.Video;
import org.example.basic.service.UserService;
import org.example.basic.service.VideoService;
import org.example.basic.utils.JwtUtils;
import org.example.basic.utils.aop.auth;
import org.example.basic.vo.FeignVo;
import org.example.basic.vo.PublishListVo;
import org.example.basic.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @auth
    @GetMapping("/videoList")
    public PublishListVo videoList(@RequestParam("videoIds") List<Long> videoIds, @RequestParam("id") Long id,@RequestParam("token") String token){
        VideoDto[] videoList = new VideoDto[0];
        try {
            videoList = videoService.getVideoListByVideoIds(videoIds, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        PublishListVo success = PublishListVo.success();
        success.setVideoList(videoList);

        return success;
    }


    @auth
    @GetMapping("/getUserByVideoId")
    public UserInfoVo getUserByVideoId(@RequestParam("video_id") long videoId,@RequestParam("token") String token){
        long userId = JwtUtils.getUserId(token);
        UserDto userDto = videoService.getUserByVideoId(videoId,userId);
        if (userDto==null){
            UserInfoVo fail = UserInfoVo.fail();
            fail.setStatusMsg("未能查询到对应作者");

            return fail;
        }
        UserInfoVo success = UserInfoVo.success();
        success.setUserDto(userDto);
       return success;

    }
}
