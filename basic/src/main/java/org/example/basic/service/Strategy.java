package org.example.basic.service;

import org.example.basic.entity.Video;

import java.sql.Timestamp;
import java.util.List;

public interface Strategy {

    /**
     * 获取特定时间后上传的视频
     * @param timestamp
     * @return
     */
    public List<Video> getVideo(Timestamp timestamp);
}
