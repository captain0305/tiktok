package org.example.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.basic.entity.User;

/**
* @author xuchengrui
* @description 针对表【user】的数据库操作Service
* @createDate 2023-08-09 20:11:01
*/
public interface UserService extends IService<User> {
    User Register(String username, String password);

    User Login(String username, String password);
}
