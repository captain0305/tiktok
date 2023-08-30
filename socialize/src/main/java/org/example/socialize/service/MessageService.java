package org.example.socialize.service;

import org.example.socialize.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author xuchengrui
* @description 针对表【message】的数据库操作Service
* @createDate 2023-08-28 19:38:38
*/
public interface MessageService extends IService<Message> {
    public boolean action(long fromUserId, long toUserId, int actionType, String content);

    public Message[] getMessageList(long fromUserId, long toUserId) throws Exception;

    List<Message> getMessageFromRedis(long fromUserId, long toUserId);
}
