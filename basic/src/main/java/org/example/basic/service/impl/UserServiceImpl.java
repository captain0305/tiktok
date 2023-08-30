package org.example.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.basic.dto.UserDto;
import org.example.basic.entity.User;
import org.example.basic.feign.SocializeFeignService;
import org.example.basic.service.UserService;
import org.example.basic.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author xuchengrui
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-08-09 20:11:01
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    @Autowired
    SocializeFeignService socializeFeignService;

    @Override
    public User Register(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
            return null;
        User userEntity = new User();

        //判断当前用户是否存在
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getName,username);
        User user = this.getOne(queryWrapper);

        if (user != null)
            return null;

        //设置用户名
        userEntity.setName(username);

        //密码加密存储
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userEntity.setPassword(passwordEncoder.encode(password));

        //初始化部分数据
        userEntity.setFollowCount(0);
        userEntity.setFollowerCount(0);
        userEntity.setTotalFavorited(0);
        userEntity.setWorkCount(0);
        userEntity.setFavoriteCount(0);


        baseMapper.insert(userEntity);
        return userEntity;
    }

    @Override
    public User Login(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
            return null;
        //查询当前用户名对应的用户
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getName,username);
        User user = this.getOne(queryWrapper);
        // 不存在此用户名的用户
        if (user == null)
            return null;

        //数据库中加密的密码
        String passwordDB = user.getPassword();

        //判断用户输入的密码和数据库中密码是否匹配
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(password, passwordDB))
            //密码正确则返回用户
            return user;
        //密码错误返回null
        return null;
    }

    @Override
    public UserDto getUserById(long userId, long followId) {
        User user = this.getById(userId);
        UserDto userDto=new UserDto();
        // 当前id对应的user存在
        if (user != null) {
            //todo isfolloe已经解决
            boolean isFollow=socializeFeignService.isFollow(userId, followId);
            userDto = new UserDto(user);
            userDto.setFollow(isFollow);

        }
        return userDto;
    }



}




