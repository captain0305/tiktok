package org.example.basic.controller;

import org.example.basic.entity.User;
import org.example.basic.service.UserService;
import org.example.basic.utils.JwtUtils;
import org.example.basic.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserVo userRegister(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userService.Register(username, password);

        // 已经注册过，注册失败
        if (user == null) {
            return UserVo.fail();
        }

        // 注册成功
        UserVo userVo = UserVo.success();
        userVo.setUserid(user.getUserId());
        userVo.setToken(JwtUtils.createJwtTokenByUser(user.getUserId()));
        return userVo;
    }

    @PostMapping("/login")
    public UserVo userLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userService.Login(username, password);
        //登陆失败
        if (user == null)
            return UserVo.fail();

        //登陆成功
        UserVo userVo = UserVo.success();
        userVo.setUserid(user.getUserId());
        userVo.setToken(JwtUtils.createJwtTokenByUser(user.getUserId()));
        return userVo;
    }


}
