package org.example.basic.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.basic.entity.User;

/**
* @author xuchengrui
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-08-09 20:11:01
* @Entity org.example.basic.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




