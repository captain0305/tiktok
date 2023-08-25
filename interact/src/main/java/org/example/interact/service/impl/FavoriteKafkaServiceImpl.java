package org.example.interact.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.interact.mapper.FavorMapper;
import org.example.interact.entity.FavorEntity;
import org.example.interact.service.FavoriteKafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * @author carey
 */
@Service
public class FavoriteKafkaServiceImpl extends ServiceImpl<FavorMapper, FavorEntity> implements FavoriteKafkaService {

    @KafkaListener(topics = "favoriteQueue")//用于标记一个方法作为 Kafka 消费者监听特定主题的消费者方法。在这里，它告诉 Spring Kafka 框架这个方法将会监听名为 "favoriteQueue" 的 Kafka 主题。
    public void favoriteQueueListener(ConsumerRecord<?, ?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        //consumerRecord.value() 包含从 Kafka 主题中接收到的实际消息内容
        if (optional.isPresent()) {
            //检查 Optional 对象是否包含非空的值
            Object kafkaMessage = optional.get();
            //开始处理消息
            FavorEntity favoriteEntity = JSON.parseObject((String) kafkaMessage, FavorEntity.class);
//            baseMapper.insert(favoriteEntity);
            System.out.println("接收到点赞的通知");
        }
    }

    @KafkaListener(topics = "removeFavoriteQueue")
    public void removeFavoriteQueueListener(ConsumerRecord<?, ?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            Object kafkaMessage = optional.get();
            //开始处理消息
            String message = JSON.parseObject((String) kafkaMessage, String.class);
            String[] split = message.split(":");
            Long videoId=Long.parseLong(split[0]);
            Long userId=Long.parseLong(split[1]);
//            baseMapper.delete(new QueryWrapper<FavorEntity>().eq("user_id", userId).eq("video_id", videoId));
            System.out.println("接收到取消点赞的通知");
        }
    }
}
