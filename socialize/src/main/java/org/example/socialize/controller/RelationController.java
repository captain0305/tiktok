package org.example.socialize.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.socialize.dto.UserDto;
import org.example.socialize.entity.Follow;
import org.example.socialize.service.FollowService;
import org.example.socialize.util.JwtUtils;
import org.example.socialize.util.aop.auth;
import org.example.socialize.vo.RelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/relation")
@Slf4j
public class RelationController {

    @Autowired
    private FollowService followService;

    @auth
    @PostMapping("/action")
    @Transactional
    public RelationVo action(@RequestParam("token") String token, @RequestParam("to_user_id") Integer toUserId, @RequestParam("action_type") Integer actionType) {
        // 获取当前用户id

        int userId = JwtUtils.getUserId(token);
        boolean flag = followService.action(userId, toUserId, actionType,token);
        if (flag){
            return RelationVo.success();
        }
        return RelationVo.fail();

    }

    @auth
    @GetMapping("/follow/list")
    public RelationVo getFollowList(@RequestParam("user_id") Long userId, @RequestParam("token") String token) {
        RelationVo fail = matchUserAndSearch(userId, token);
        if (fail != null) return fail;
        UserDto[] userList = null;
        try {
            userList = followService.getFollowList(userId, token);

        } catch (Exception e) {
            RelationVo newfail = RelationVo.fail();
            newfail.setStatusMsg("获取关注列表失败");
            return newfail;
        }


        RelationVo success = RelationVo.success();
        success.setStatusMsg("获取列表成功");
        success.setUserList(userList);
        return success;
    }

    @auth
    @GetMapping("/follower/list")
    public RelationVo getFollowerList(@RequestParam("user_id") Long userId,@RequestParam("token")String token){
        //用户只能查询自己的
        RelationVo fail = matchUserAndSearch(userId, token);
        if (fail != null) return fail;
        UserDto[] userList = new UserDto[0];
        try {
            userList = followService.getFollowerList(userId, token);
        } catch (Exception e) {
            RelationVo newfail = RelationVo.fail();
            newfail.setStatusMsg("获取粉丝列表失败");
            return newfail;
        }


        RelationVo success = RelationVo.success();
        success.setStatusMsg("获取粉丝列表成功");
        success.setUserList(userList);
        return success;
    }

    @auth
    @GetMapping("/friend/list")
    public RelationVo getFriendList(@RequestParam("user_id") Long userId, @RequestParam("token") String token) {

        RelationVo fail = matchUserAndSearch(userId, token);
        if (fail != null) return fail;


        UserDto[] userList = new UserDto[0];
        try {
            userList = followService.getFriendList(userId, token);
        } catch (Exception e) {
            RelationVo newfail = RelationVo.fail();
            newfail.setStatusMsg("获取好友列表失败");
            return newfail;
        }

        RelationVo success = RelationVo.success();
        success.setStatusMsg("获取好友列表成功");
        success.setUserList(userList);
        return success;
    }

    @GetMapping("/follow/isFollow")
    Boolean isFollow(@RequestParam("user_id")Long userId,@RequestParam("follow_id")Long followId){


        return followService.isFollow(userId,followId);

    }

    private static RelationVo matchUserAndSearch(Long userId, String token) {
        int token_id=JwtUtils.getUserId(token);
        if (token_id!= userId){
            RelationVo fail = RelationVo.fail();
            fail.setStatusMsg("token和当前对应id不一致");
            return fail;
        }
        return null;
    }


}

