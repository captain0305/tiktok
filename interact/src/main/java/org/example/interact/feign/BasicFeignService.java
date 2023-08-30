package org.example.interact.feign;

import org.example.interact.vo.FeignVo;
import org.example.interact.vo.PublishListVo;
import org.example.interact.vo.UserInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author carey
 */

@FeignClient("douyin-basic")
public interface BasicFeignService {
    /**
     * 通过用户id查询用户
     * @param userId 用户id
     * @param token 查询者token
     * @return 返回用户实体
     */
    @GetMapping("/user/getUserById")
    UserInfoVo getUserById(@RequestParam("user_id") long userId, @RequestParam("token") String token);
    /**
     * 通过视频id查询视频
     * @param videoIds 视频id列表
     * @param id 用户id
     * @param token 用户token
     * @return 返回视频列表
     */
    @GetMapping("video/videoList")
    PublishListVo videoList(@RequestParam("videoIds") List<Long> videoIds, @RequestParam("id") long id, @RequestParam("token") String token);
    /**
     * 通过视频id查询投稿用户
     * @param videoId 视频id
     * @param token 用户token
     * @return 返回用户实体
     */
    @GetMapping("/video/getUserByVideoId")
    UserInfoVo getUserByVideoId(@RequestParam("video_id") long videoId,@RequestParam("token") String token);
    /**
     * 所有作品获赞数量统计
     * @param userId 用户id
     * @param addOrSub 操作类型
     * @param token 用户token
     * @return 返回视图
     */
    @PutMapping("/user/operateTotalFavorited")
    FeignVo operateIsFavorited(@RequestParam("user_id")int userId, @RequestParam("add_or_sub")int addOrSub, @RequestParam("token")String token);
    /**
     * 给别人作品点赞数量统计
     * @param userId 用户id
     * @param addOrSub 操作类型
     * @param token 用户token
     * @return 返回视图
     */
    @PutMapping("/user/operateFavorite")
    FeignVo operateFavorite(@RequestParam("user_id")int userId, @RequestParam("add_or_sub")int addOrSub,@RequestParam("token")String token);
    /**
     * 作品点赞数量统计
     * @param videoId 用户id
     * @param addOrSub 操作类型
     * @param token 用户token
     * @return 返回视图
     */
    @PutMapping("/video/operateFavoriteCount")
    FeignVo operateFavoriteCount(@RequestParam("video_id")int videoId, @RequestParam("add_or_sub")int addOrSub, @RequestParam("token")String token);

    /**
     * 视频评论数量统计
     * @param videoId 视频id
     * @param addOrSub 操作类型
     * @param token 用户token
     * @return 返回视图
     */
    @PutMapping("/video/operateCommentCount")
    FeignVo operateCommentCount(@RequestParam("video_id")int videoId, @RequestParam("add_or_sub")int addOrSub, @RequestParam("token")String token);


}
