package org.example.socialize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.socialize.entity.Message;
import org.example.socialize.service.MessageService;
import org.example.socialize.mapper.MessageMapper;
import org.example.socialize.util.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
* @author xuchengrui
* @description 针对表【message】的数据库操作Service实现
* @createDate 2023-08-28 19:38:38
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService {

    @Autowired
    private StringRedisTemplate  stringRedisTemplate;

    @Autowired
    MessageService messageService;

    /**
     * 只要发消息同时失效每个用户的队列
     * @param fromUserId
     * @param toUserId
     * @param actionType
     * @param content
     * @return
     */
    @Caching(evict = {
            @CacheEvict(value = "message", key = "#fromUserId+':'+#toUserId"),
            @CacheEvict(value = "message", key = "#toUserId+':'+#fromUserId")
    })
    @Override
    public boolean action(long fromUserId, long toUserId, int actionType, String content) {
        // 当前token对应的user不存在
        if (fromUserId == -1)
            return false;
        if (actionType == 1) {
            // 发送消息
            // 创建要存储的数据对象并填充数据
            Message messageEntity = new Message();
            messageEntity.setFromUserId(fromUserId);
            messageEntity.setToUserId(toUserId);
            messageEntity.setCreateTime(new Date());
            messageEntity.setContent(content);

            int count = baseMapper.insert(messageEntity);

            // 修改redis当前用户最后访问时间
            Date currentTime = new Date();
            stringRedisTemplate.opsForValue().set("visitTime:"+fromUserId, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime));

            return count == 1;
        }
        // actionType的值不合法
        return false;
    }

    @Override
    public Message[] getMessageList(long fromUserId, long toUserId) throws Exception {
        // 通过当前service调用使用springCache的方法，避免不通过AOP导致注释不生效
        //get得到双方的数据
        List<Message> messageEntities = messageService.getMessageFromRedis(fromUserId, toUserId);

        // 对比访问时间，避免返回重复数据
        Date currentTime = new Date();
        String s = stringRedisTemplate.opsForValue().get("visitTime:"+fromUserId);//获取上次访问时间
        stringRedisTemplate.opsForValue().set("visitTime:"+fromUserId, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime));



        if (s != null) {
            Date lastTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);

            // 两次访问不超过3秒认为是同一个会话框只获取新消息
            if (currentTime.getTime() - lastTime.getTime() < 3000) {
                //从redis拿到的的数据中取到在上次访问一秒后发送的消息
                messageEntities = messageEntities.stream().filter(messageEntity -> {
                    lastTime.setTime(lastTime.getTime()+1000);
                    return messageEntity.getCreateTime().after(lastTime);
                }).collect(Collectors.toList());
            }
        }

        int size = messageEntities.size();
        Message[] messageList = new Message[size];

        List<CompletableFuture> futureList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int finalI = i;
            Message messageEntity = messageEntities.get(finalI);
            // 放入线程池中运行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                Message message = new Message();
                message.setId(messageEntity.getId());
                message.setFromUserId(messageEntity.getFromUserId());
                message.setToUserId(messageEntity.getToUserId());
                message.setCreateTime(messageEntity.getCreateTime());
                message.setContent(messageEntity.getContent());
                messageList[finalI] = message;
            }, ThreadPool.executor);
            futureList.add(future);
        }

        // 阻塞主线程等待，避免主线程提前返回结果，导致数据错误
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).get();
        return messageList;
    }


    @Cacheable(value = "message", key = "#fromUserId+':'+#toUserId", sync = true)
    public List<Message> getMessageFromRedis(long fromUserId, long toUserId) {
        LambdaQueryWrapper<Message> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .or(i->i.eq(Message::getToUserId,toUserId).eq(Message::getFromUserId,fromUserId))
                .or(i->i.eq(Message::getToUserId,fromUserId).eq(Message::getFromUserId,toUserId));
        List<Message> messageEntities = baseMapper.selectList(lambdaQueryWrapper);
        return messageEntities;
    }

}




