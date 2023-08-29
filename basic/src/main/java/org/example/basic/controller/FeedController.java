package org.example.basic.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.basic.dto.UserDto;
import org.example.basic.dto.VideoDto;
import org.example.basic.entity.User;
import org.example.basic.entity.Video;
import org.example.basic.feign.InteractFeignService;
import org.example.basic.feign.SocializeFeignService;
import org.example.basic.service.FeedService;
import org.example.basic.service.Strategy;
import org.example.basic.service.UserService;
import org.example.basic.service.impl.GetLatestStrategy;
import org.example.basic.utils.JwtUtils;
import org.example.basic.utils.ThreadPool;
import org.example.basic.utils.aop.auth;
import org.example.basic.vo.VideoFeedVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@Transactional
@Slf4j
public class FeedController {
    @Autowired
    private UserService userService;

    @Autowired
    private FeedService feedService;

    @Autowired
    private Strategy feedStrategy;

    @Autowired
    private InteractFeignService favoriteService;

    @Autowired
    private SocializeFeignService socializeFeignService;

    /**
     *  抖音首页返回视频流
     * @param latest_time
     * @param token
     * @return
     * @throws InterruptedException
     */
    @auth
    @GetMapping("/feed")
    public VideoFeedVo feed(@RequestParam("latest_time") String latest_time, @RequestParam("token") String token) throws InterruptedException {
        Long latestTime="0".equals(latest_time)?System.currentTimeMillis():Long.parseLong(latest_time);


        Timestamp timestamp=new Timestamp(latestTime);
        List<Video> videoList = feedService.getVideoByStrategy(feedStrategy, timestamp);
        if(videoList.size()==0){
            VideoFeedVo fail = VideoFeedVo.fail();
            fail.setStatusMsg("视频已经刷完了");
            return fail;
        }

        VideoDto[] videoDtos = new VideoDto[videoList.size()];
        List<CompletableFuture> futureList = new ArrayList<>();
        for (int i = 0; i < videoList.size(); i++){
            Video video = videoList.get(i);
            int finalI = i;
            // 放入线程池中运行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {

                //查询作者信息

                User user = userService.getById(video.getAuthorId());
                UserDto userDto = new UserDto(user);
                //TODO isfollow
                //boolean isFollow=socializeFeignService.isFollow(userId, video.getAuthorId());
                userDto.setFollow(true);

                //查询视频信息

                //boolean isFavorite = favoriteService.isFavorite(userId, video.getVideoId());
                VideoDto videoDto = new VideoDto(video);
                videoDto.setAuthor(userDto);
                //TODO favarated
                videoDto.setFavorite(true);



                videoDtos[finalI] = videoDto;
            }, ThreadPool.executor);
            futureList.add(future);
        }
        try {
            // 阻塞主线程等待，避免主线程提前返回结果，导致数据错误
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return VideoFeedVo.fail();
        }


        VideoFeedVo videoFeedVo = VideoFeedVo.success();
        videoFeedVo.setVideoList(videoDtos);

        if (videoList.size() > 0) {
            //nexttime是上次的最早的视频流
            long time = videoList.get(videoList.size() - 1).getCreatedTime().getTime();

            log.info(videoList.get(videoList.size() - 1).getVideoId().toString());
            videoFeedVo.setNextTime(time);
        } else {
            //否则设置下次返回七天内的视频，此行仅方便开发测试有用
            videoFeedVo.setNextTime(System.currentTimeMillis()-7*24*60*60*1000);
        }
        log.info("feed:拉取视频成功");
        log.info(videoFeedVo.getVideoList()[0].getTitle());
        return videoFeedVo;
    }


}
