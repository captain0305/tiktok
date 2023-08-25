package org.example.basic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.basic.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author xuchengrui
* @description 针对表【video】的数据库操作Mapper
* @createDate 2023-08-10 19:15:16
* @Entity org.example.basic.entity.Video
*/
@Mapper
public interface VideoMapper extends BaseMapper<Video> {

}




