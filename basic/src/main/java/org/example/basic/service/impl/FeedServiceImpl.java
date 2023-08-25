package org.example.basic.service.impl;

import org.example.basic.entity.Video;
import org.example.basic.service.FeedService;
import org.example.basic.service.Strategy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;


@Service
public class FeedServiceImpl implements FeedService {
    @Override
    public List<Video> getVideoByStrategy(Strategy strategy, Timestamp timestamp) {
        return strategy.getVideo(timestamp);
    }
}
