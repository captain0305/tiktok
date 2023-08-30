package org.example.socialize.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.socialize.entity.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author xuchengrui
* @description 针对表【follow(关注表)】的数据库操作Mapper
* @createDate 2023-08-28 19:38:38
* @Entity org.example.socialize.entity.Follow
*/
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

}




