package org.example.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.basic.dto.UserDto;
import org.example.basic.dto.VideoDto;
import org.example.basic.entity.User;
import org.example.basic.entity.Video;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
* @author xuchengrui
* @description 针对表【video】的数据库操作Service
* @createDate 2023-08-10 15:18:58
*/
public interface VideoService extends IService<Video> {

    List<Video> searchVideosByUserId(long userId);

    VideoDto[] listVideoList(User user) throws Exception;

    /**
     * 将视频文件保存到指定文件夹
     * @param videoTargetFile
     * @param data
     * @throws IOException
     */
    String fetchVideoToFile(String videoTargetFile, MultipartFile data) throws IOException;

    /**
     * 截取视频文件某一帧，将其保存到指定文件夹
     * @param videoFile
     * @param targetFile
     */
    String fetchFrameToFile(String videoFile, String targetFile);

    VideoDto[] getVideoListByVideoIds(List<Integer> videoIds, long id) throws Exception;


    void saveVideoMsg(long userId, String videoPath, String coverPath, String title);

    UserDto getUserByVideoId(long videoId,long userId);

}
