package org.example.socialize.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.socialize.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author xuchengrui
* @description 针对表【message】的数据库操作Mapper
* @createDate 2023-08-28 19:38:38
* @Entity org.example.socialize.entity.Message
*/
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}




