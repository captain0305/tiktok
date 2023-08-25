package org.example.interact.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.interact.mapper.CommentMapper;
import org.example.interact.entity.CommentEntity;
import org.example.interact.service.CommentKafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * @author carey
 */
@Service
public class CommentKafkaServiceImpl extends ServiceImpl<CommentMapper, CommentEntity> implements CommentKafkaService {

    @KafkaListener(topics = "commentQueue")
    public void commentQueueListener(ConsumerRecord<?, ?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            Object kafkaMessage = optional.get();
            //开始处理消息
            CommentEntity commentEntity = JSON.parseObject((String) kafkaMessage, CommentEntity.class);
//            this.save(commentEntity);
            System.out.println("接收到添加评论的通知");
        }
    }

    @KafkaListener(topics = "removeCommentQueue")
    public void removeCommentQueueListener(ConsumerRecord<?, ?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            Object kafkaMessage = optional.get();
            //开始处理消息
            String commentId = JSON.parseObject((String) kafkaMessage, String.class);
//            this.removeById(commentId);
            System.out.println("接收到删除评论到通知");
        }
    }
}
