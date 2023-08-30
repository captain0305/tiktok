package org.example.socialize.controller;


import org.example.socialize.entity.Message;
import org.example.socialize.service.MessageService;
import org.example.socialize.util.JwtUtils;
import org.example.socialize.util.aop.auth;
import org.example.socialize.vo.MessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;


    @auth
    @PostMapping("/action")
    public MessageVo action(
            @RequestParam("token") String token,
            @RequestParam("to_user_id") long toUserId,
            @RequestParam("action_type") int actionType,
            @RequestParam("content") String content) {

        long fromUserId = JwtUtils.getUserId(token);
        boolean flag = messageService.action(fromUserId, toUserId, actionType, content);
        if (flag)
            return MessageVo.success();
        return MessageVo.fail();
    }

    @auth
    @GetMapping("/chat")
    public MessageVo chat(
            @RequestParam("token") String token,
            @RequestParam("to_user_id") long toUserId) throws ParseException {

        long fromUserId = JwtUtils.getUserId(token);
        Message[] messageList = new Message[0];
        try {
            messageList = messageService.getMessageList(fromUserId, toUserId);
        } catch (Exception e) {
            e.printStackTrace();
            MessageVo fail = MessageVo.fail();
            fail.setStatusMsg("获取消息失败");
            return fail;
        }
        MessageVo success = MessageVo.success();
        success.setStatusMsg("获取消息成功");
        success.setMessageList(messageList);
        return success;
    }

}
