package org.example.interact.controller;

import org.example.interact.feign.BasicFeignService;
import org.example.interact.service.FavoriteService;
import org.example.interact.utils.JwtUtils;
import org.example.interact.vo.FavoriteVo;
import org.example.interact.dto.VideoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author carey
 */
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private BasicFeignService basicFeignService;

    @PostMapping("/action")
    public FavoriteVo action(@RequestParam("token") String token, @RequestParam("video_id") Long videoId, @RequestParam("action_type") Integer actionType) {
        if (!JwtUtils.verifyTokenOfUser(token)) {
            return FavoriteVo.fail();
        }
        long userId = JwtUtils.getUserId(token);
        boolean flag = favoriteService.action(userId, videoId, actionType);

        if (flag) {
            return FavoriteVo.success();
        }

        return FavoriteVo.fail();
    }


    @GetMapping("/list")
    public FavoriteVo list(@RequestParam("user_id") Long userId, @RequestParam("token") String token) {
        if (!JwtUtils.verifyTokenOfUser(token)) {
            return FavoriteVo.fail();
        }
        long id = JwtUtils.getUserId(token);
        VideoDto[] videolist = favoriteService.favoriteList(userId, id);
        FavoriteVo success = FavoriteVo.success();
        success.setVideoList(videolist);
        return success;
    }

    @GetMapping("/count")
    public int favoriteCount(@RequestParam("videoId") Long videoId) {
        return favoriteService.countByVideoId(videoId);
    }

    @GetMapping("/isFavorite")
    public boolean isFavorite(@RequestParam("userId") Long userId, @RequestParam("videoId") Long videoId) {
        return favoriteService.isFavorite(userId, videoId);
    }
}
