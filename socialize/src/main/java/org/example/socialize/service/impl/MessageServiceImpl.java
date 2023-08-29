package org.example.socialize.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.socialize.entity.Message;
import org.example.socialize.service.MessageService;
import org.example.socialize.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author xuchengrui
* @description 针对表【message】的数据库操作Service实现
* @createDate 2023-08-28 19:38:38
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService {

}




