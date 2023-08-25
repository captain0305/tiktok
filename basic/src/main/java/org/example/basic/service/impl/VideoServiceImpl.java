package org.example.basic.service.impl;

import com.aliyun.oss.OSS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.example.basic.dto.UserDto;
import org.example.basic.dto.VideoDto;
import org.example.basic.entity.User;
import org.example.basic.entity.Video;
import org.example.basic.feign.InteractFeignService;
import org.example.basic.feign.SocializeFeignService;
import org.example.basic.mapper.VideoMapper;
import org.example.basic.service.UserService;
import org.example.basic.service.VideoService;
import org.example.basic.utils.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
* @author xuchengrui
* @description 针对表【video】的数据库操作Service实现
* @createDate 2023-08-10 15:18:58
*/
@Service
@Slf4j
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{
    @Autowired
    private SocializeFeignService socializeFeignService;

    @Autowired
    private UserService userService;

    @Autowired
    private InteractFeignService interactFeignService;

    @Autowired
    private OSS oss;

    @Value("${spring.cloud.alicloud.oss.bucket-name}")
    private String bucketName;

    private static final String DEFAULT_IMG_FORMAT = "jpg";

    /**
     * 查询当前用户发布的所有视频基础信息
     * @param userId
     * @return
     */
    @Override
    public List<Video> searchVideosByUserId(long userId) {
        LambdaQueryWrapper<Video> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Video::getAuthorId,userId);
        List<Video> list = this.list(queryWrapper);
        return list;
        //return this.list(new QueryWrapper<Video>().eq("author_id", userId));
    }


    /**
     * 查询并整合当前用户发布的所有视频以及视频作者信息
     * @param user
     * @return
     */
    @Cacheable(value = "video", key = "#user.userId", sync = true)
    @Override
    public VideoDto[] listVideoList(User user) throws Exception {
        //查询视频表获取当前用户发的视频的信息
        List<Video> videos = this.searchVideosByUserId(user.getUserId());

        //填入作者信息
        //TODO isfoloow
        //boolean isfollow=socializeFeignService.isFollow(user.getUserId(), user.getUserId());
        UserDto author = new UserDto(user);
        author.setFollow(true);

        //创建需要返回的video数组
        int size = videos.size();
        VideoDto[] videoList = new VideoDto[size];

        //查询并填入每个视频的数据
        List<CompletableFuture> futureList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int finalI = i;
            // 放入线程池中运行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                videoList[finalI] = new VideoDto();
                videoList[finalI].setVideoId(videos.get(finalI).getVideoId());
                videoList[finalI].setAuthor(author);
                videoList[finalI].setPlayUrl(videos.get(finalI).getPlayUrl());
                videoList[finalI].setCoverUrl(videos.get(finalI).getCoverUrl());

                videoList[finalI].setFavoriteCount(videos.get(finalI).getFavoriteCount());
                videoList[finalI].setCommentCount(videos.get(finalI).getCommentCount());
                //TODO isfoloow
//                boolean favorite = interactFeignService.isFavorite(user.getUserId(), videos.get(finalI).getVideoId());

                videoList[finalI].setFavorite(true);

                videoList[finalI].setTitle(videos.get(finalI).getTitle());
            }, ThreadPool.executor);
            futureList.add(future);
        }
        // 阻塞主线程等待，避免主线程提前返回结果，导致数据错误
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).get();
        return videoList;
    }

    @Override
    public String fetchVideoToFile(String videoTargetFile, MultipartFile data) throws IOException {
        // 将视频保存到oss
        oss.putObject(bucketName, videoTargetFile, new ByteArrayInputStream(data.getBytes()));
        Date date = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 60);//保存60天
        String url = oss.generatePresignedUrl(bucketName, videoTargetFile, date).toString();
        log.info("视频保存成功："+videoTargetFile);
        return url;
    }

    @Override
    public String fetchFrameToFile(String videoFile, String targetFile) {
        try {
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
            ff.start();
            int length = ff.getLengthInFrames();

            int i = 0;
            Frame f = null;
            while (i < length) {
                f = ff.grabFrame();
                // 过滤前5帧，避免出现全黑的图片，依自己情况而定
                if ((i >= 5) && (f.image != null)) {
                    break;
                }
                i++;
            }

            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage bi = converter.getBufferedImage(f);

            // 将封面保存到oss
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bi, DEFAULT_IMG_FORMAT, os);
            oss.putObject(bucketName, targetFile, new ByteArrayInputStream(os.toByteArray()));
            Date date = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 60);//+7天？
            String url = oss.generatePresignedUrl(bucketName, targetFile, date).toString();

            ff.stop();
            ff.close();
            log.info("视频封面保存成功："+url);
            return url;
        } catch (Exception e) {
            throw new RuntimeException("转换视频图片异常");
        }
    }

    // 失效模式，投稿发布更新数据库后删除“当前用户发布的视频列表”缓存数据
    @CacheEvict(value = "video", key = "#userId")
    @Override
    @Transactional
    public void saveVideoMsg(long userId, String videoPath, String coverPath, String title) {
        User user = userService.getById(userId);
        Integer workCount = user.getWorkCount();
        user.setWorkCount(workCount+1);
        userService.updateById(user);

        Video video=new Video();
        video.setAuthorId(userId);
        video.setPlayUrl(videoPath);
        video.setCoverUrl(coverPath);
        video.setTitle(title);
        video.setFavoriteCount(0);
        video.setCommentCount(0);
        Date date = new Date(System.currentTimeMillis());
        video.setCreatedTime(date);
        save(video);

    }


}




