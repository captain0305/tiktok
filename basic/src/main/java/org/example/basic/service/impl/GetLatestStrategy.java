package org.example.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.basic.entity.Video;
import org.example.basic.service.Strategy;
import org.example.basic.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class GetLatestStrategy implements Strategy {

    @Autowired
    VideoService videoService;


    //返回长列表长度
    @Value("${video-config.feed-video-max-num}")
    private String VIDEO_MAX_NUM;


    /**
     * 获取在指定时间后上传的视频，包含指定时间时刻
     * @param timestamp
     * @return
     */
    @Override
    public List<Video> getVideo(Timestamp timestamp) {

        Date date=new Date(timestamp.getTime());
        LambdaQueryWrapper<Video> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.le(Video::getCreatedTime,date).orderByDesc(Video::getCreatedTime).last("limit "+VIDEO_MAX_NUM);

        return videoService.list(lambdaQueryWrapper);
    }
}
