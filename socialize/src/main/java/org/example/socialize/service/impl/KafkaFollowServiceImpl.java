package org.example.socialize.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.socialize.entity.Follow;
import org.example.socialize.feign.BasicFeignService;
import org.example.socialize.mapper.FollowMapper;
import org.example.socialize.service.FollowService;
import org.example.socialize.service.KafkaFollowService;
import org.example.socialize.vo.FeignVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Slf4j
public class KafkaFollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements KafkaFollowService {
    @Autowired
    FollowService followService;

    @Autowired
    BasicFeignService basicFeignService;

    @KafkaListener(topics = "FollowQueue")
    @Transactional
    public void followQueueListener(ConsumerRecord<?,?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            Object kafkaMessage = optional.get();
            //开始处理消息
            String message = JSON.parseObject((String) kafkaMessage, String.class);
            String[] split = message.split(":");
            Integer addOrSub=Integer.parseInt(split[0]);
            Integer userId=Integer.parseInt(split[1]);
            String token=split[2];
            FeignVo feignVo = basicFeignService.operateFollowCount(userId, addOrSub, token);
            log.info("关注者操作");
            log.info(feignVo.getStatusMsg());

        }
    }


    @KafkaListener(topics = "FollowerQueue")
    @Transactional
    public void followerQueueListener(ConsumerRecord<?,?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            Object kafkaMessage = optional.get();
            //开始处理消息
            String message = JSON.parseObject((String) kafkaMessage, String.class);
            String[] split = message.split(":");
            Integer addOrSub=Integer.parseInt(split[0]);
            Integer userId=Integer.parseInt(split[1]);
            String token=split[2];
            FeignVo feignVo = basicFeignService.operateFollowerCount(userId, addOrSub, token);
            log.info("被关注者操作");
            log.info(feignVo.getStatusMsg());

        }
    }


//    @KafkaListener(topics = "MessageQueue")
//    public void messageQueueListener(ConsumerRecord<?,?> consumerRecord){
//        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
//        if (optional.isPresent()) {
//            Object kafkaMessage = optional.get();
//            //开始处理消息
//            String message = JSON.parseObject((String) kafkaMessage, String.class);
//            String[] split = message.split(":");
//            Integer addOrSub=Integer.parseInt(split[0]);
//            Integer userId=Integer.parseInt(split[1]);
//            String token=split[2];
//            FeignVo feignVo = basicFeignService.operateFollowerCount(userId, addOrSub, token);
//            log.info("被关注者操作");
//            log.info(feignVo.getStatusMsg());
//
//        }
//    }








}
