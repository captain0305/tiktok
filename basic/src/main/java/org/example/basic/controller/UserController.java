package org.example.basic.controller;

import org.example.basic.dto.UserDto;
import org.example.basic.entity.User;
import org.example.basic.feign.SocializeFeignService;
import org.example.basic.service.UserService;
import org.example.basic.utils.JwtUtils;
import org.example.basic.utils.ServiceEnum;
import org.example.basic.utils.aop.auth;
import org.example.basic.vo.FeignVo;
import org.example.basic.vo.UserInfoVo;
import org.example.basic.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            return UserVo.fail(ServiceEnum.REGISTER.getType());
        }

        // 注册成功
        UserVo userVo = UserVo.success(ServiceEnum.REGISTER.getType());
        userVo.setUserid(user.getUserId());
        userVo.setToken(JwtUtils.createJwtTokenByUser(user.getUserId()));
        return userVo;
    }

    @PostMapping("/login")
    public UserVo userLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userService.Login(username, password);
        //登陆失败
        if (user == null)
            return UserVo.fail(ServiceEnum.LOGIN.getType());

        //登陆成功
        UserVo userVo = UserVo.success(ServiceEnum.LOGIN.getType());
        userVo.setUserid(user.getUserId());
        userVo.setToken(JwtUtils.createJwtTokenByUser(user.getUserId()));
        return userVo;
    }

    @auth
    @GetMapping("/")
    public UserInfoVo userInfo(@RequestParam("user_id") Integer userId, @RequestParam("token") String token) {

        //通过userId获得对应用户数据
        User user = userService.getById(userId);

        if (user != null) {
            UserInfoVo success = UserInfoVo.success();
            //boolean isFollow=SocializeFeignService.isFollow(userId, JwtUtils.getUserId(token));
            UserDto userDto=new UserDto(user);
            userDto.setFollow(true);
            success.setUserDto(userDto);


            return success;
        } else {
            return UserInfoVo.fail();
        }
    }

    @GetMapping("/getUserById")
    public UserDto getUserById(@RequestParam("user_id") int userId, @RequestParam("follow_id") int followId) {
        UserDto user = userService.getUserById(userId, followId);
        return user;
    }

    /**
     * 所有作品获赞数量统计
     * @param userId
     * @param addOrSub
     * @param token
     * @return
     */
    @auth
    @PutMapping("/operateTotalFavorited")
    public FeignVo operateIsFavorited(@RequestParam("user_id")int userId, @RequestParam("add_or_sub")int addOrSub,@RequestParam("token")String token){


        User user = userService.getById(userId);
        if (user==null){
            return FeignVo.fail();
        }
        if (addOrSub==0) {
            Integer totalFavorited = user.getTotalFavorited();
            user.setTotalFavorited(totalFavorited + 1);
        }else {
            Integer totalFavorited = user.getTotalFavorited();
            user.setTotalFavorited(totalFavorited -1);
        }
        userService.updateById(user);
        return FeignVo.success();

    }

    /**
     * 喜欢别人作品数量的统计
     * @param userId
     * @param addOrSub
     * @param token
     * @return
     */
    @auth
    @PutMapping("/operateFavorite")
    public FeignVo operateFavorite(@RequestParam("user_id")int userId, @RequestParam("add_or_sub")int addOrSub,@RequestParam("token")String token){


        User user = userService.getById(userId);
        if (user==null){
            return FeignVo.fail();
        }
        if (addOrSub==0) {
            Integer favoriteCount = user.getFavoriteCount();
            user.setFavoriteCount(favoriteCount + 1);
        }else {
            Integer favoriteCount = user.getFavoriteCount();
            user.setFavoriteCount(favoriteCount -1);
        }
        userService.updateById(user);
        return FeignVo.success();

    }


    @auth
    @PutMapping("/operateFollowerCount")
    public FeignVo operateFollowerCount(@RequestParam("user_id")int userId, @RequestParam("add_or_sub")int addOrSub,@RequestParam("token")String token){

        User user = userService.getById(userId);
        if (user==null){
            return FeignVo.fail();
        }
        if (addOrSub==0) {
            Integer followerCount = user.getFollowerCount();
            user.setFollowerCount(followerCount + 1);
        }else {
            Integer followerCount = user.getFollowerCount();
            user.setFollowerCount(followerCount - 1);
        }
        userService.updateById(user);
        return FeignVo.success();

    }

    @auth
    @PutMapping("/operateFollowCount")
    public FeignVo operateFollowCount(@RequestParam("user_id")int userId, @RequestParam("add_or_sub")int addOrSub,@RequestParam("token")String token){

        User user = userService.getById(userId);
        if (user==null){
            return FeignVo.fail();
        }
        if (addOrSub==0) {
            Integer followCount = user.getFollowCount();
            user.setFollowCount(followCount + 1);
        }else {
            Integer followCount = user.getFollowCount();
            user.setFollowCount(followCount - 1);
        }
        userService.updateById(user);
        return FeignVo.success();

    }

}
