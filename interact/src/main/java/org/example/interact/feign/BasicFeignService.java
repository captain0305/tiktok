package org.example.interact.feign;

import org.example.interact.dto.UserDto;
import org.example.interact.dto.VideoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
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
     * @return 返回用户实体
     */
    @GetMapping("/user/getUserById")
    UserDto getUserById(@RequestParam("userId") long userId);
    /**
     * 通过视频id查询视频
     * @param videoIds 视频id列表
     * @param id 用户id
     * @return 视频列表
     */
    @GetMapping("/publish/videoList")
    VideoDto[] videoList(@RequestParam("videoIds") List<Long> videoIds, @RequestParam("id") long id);
    /**
     * 通过视频id查询投稿用户
     * @param videoId 视频id
     * @return 用户id
     */
    @GetMapping("/publish/getUserIdByVideoId")
    int getUserIdByVideoId(@RequestParam("videoId") long videoId);
}
