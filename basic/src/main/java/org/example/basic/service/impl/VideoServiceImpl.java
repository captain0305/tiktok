package org.example.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.basic.entity.Video;
import org.example.basic.mapper.VideoMapper;
import org.example.basic.service.VideoService;
import org.springframework.stereotype.Service;

/**
* @author xuchengrui
* @description 针对表【video】的数据库操作Service实现
* @createDate 2023-08-10 15:18:58
*/
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{

}




