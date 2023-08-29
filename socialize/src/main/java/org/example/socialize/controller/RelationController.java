package org.example.socialize.controller;

import org.example.socialize.entity.Follow;
import org.example.socialize.service.FollowService;
import org.example.socialize.util.JwtUtils;
import org.example.socialize.util.aop.auth;
import org.example.socialize.vo.RelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relation")
public class RelationController {

    @Autowired
    private FollowService followService;

    @auth
    @PostMapping("/action")
    public RelationVo action(@RequestParam("token") String token, @RequestParam("to_user_id") Integer toUserId, @RequestParam("action_type") Integer actionType) {
        // 获取当前用户id

        int userId = JwtUtils.getUserId(token);
        boolean flag = followService.action(userId, toUserId, actionType);
        if (flag){
            return RelationVo.success();
        }
        return RelationVo.fail();

    }
}

