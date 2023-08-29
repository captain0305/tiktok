package org.example.basic.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.basic.dto.VideoDto;
import org.example.basic.entity.User;
import org.example.basic.service.UserService;
import org.example.basic.service.VideoService;
import org.example.basic.utils.JwtUtils;
import org.example.basic.utils.aop.auth;
import org.example.basic.vo.PublishActionVo;
import org.example.basic.vo.PublishListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/publish")
public class PublishController {
    @Value("${video-config.video-save-path}")
    private String VIDEO_PATH;

    @Value("${video-config.video-cover-save-path}")
    private String VIDEO_COVER_PATH;

    private static final String DEFAULT_VIDEO_FORMAT = ".mp4";
    private static final String DEFAULT_IMG_FORMAT = ".jpg";

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;

    @auth
    @GetMapping("/list")
    public PublishListVo list(@RequestParam("token") String token, @RequestParam("user_id") int userId) {

        // 查询当前用户信息
        User user = userService.getById(userId);

        // 如果用户存在则成功
        if (user != null) {
            // 获取所有需要返回的视频以及视频作者信息
            VideoDto[] videoList = new VideoDto[0];
            try {
                videoList = videoService.listVideoList(user);
            } catch (Exception e) {
                e.printStackTrace();
                return PublishListVo.fail();
            }
            // 成功
            PublishListVo success = PublishListVo.success();
            success.setVideoList(videoList);
            return success;
        }

        return PublishListVo.fail();
    }

    @PostMapping("/action")
    @auth
    public PublishActionVo action(@RequestParam("data") MultipartFile data, @RequestParam("token") String token,
                                  @RequestParam("title") String title, HttpSession session) throws IOException {

        // 通过token获取用户id
        int userId = JwtUtils.getUserId(token);
        if (userId == 0){
            return PublishActionVo.fail();
        }
        // 生成视频名称
        String filename= UUID.randomUUID().toString();
        // 视频保存路径
        String videoTargetPath = VIDEO_PATH + "/" +filename+DEFAULT_VIDEO_FORMAT;
        String videoPath = videoService.fetchVideoToFile(videoTargetPath, data);

        //保存封面进本地
        // 封面路径
        String coverTargetPath=VIDEO_COVER_PATH + "/" + filename+DEFAULT_IMG_FORMAT;
        String coverPath = videoService.fetchFrameToFile(videoPath, coverTargetPath);

        //向mysql中存入视频数据
        videoService.saveVideoMsg(userId, videoPath, coverPath, title);

        return PublishActionVo.success();
    }


}
