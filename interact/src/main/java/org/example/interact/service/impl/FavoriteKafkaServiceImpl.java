package org.example.interact.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.interact.dto.FeignFavorDto;
import org.example.interact.feign.BasicFeignService;
import org.example.interact.mapper.FavorMapper;
import org.example.interact.entity.FavorEntity;
import org.example.interact.service.FavoriteKafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.interact.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * @author carey
 */
@Service
public class FavoriteKafkaServiceImpl extends ServiceImpl<FavorMapper, FavorEntity> implements FavoriteKafkaService {

    @Autowired
    private BasicFeignService basicFeignService;

    @Transactional(rollbackFor = Exception.class)
    @KafkaListener(topics = "favoriteQueue")//用于标记一个方法作为 Kafka 消费者监听特定主题的消费者方法。在这里，它告诉 Spring Kafka 框架这个方法将会监听名为 "favoriteQueue" 的 Kafka 主题。
    public void favoriteQueueListener(ConsumerRecord<?, ?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        //consumerRecord.value() 包含从 Kafka 主题中接收到的实际消息内容
        if (optional.isPresent()) {
            System.out.println("接收到点赞的通知");
            //检查 Optional 对象是否包含非空的值
            Object kafkaMessage = optional.get();
//            //开始处理消息
            FeignFavorDto feignFavorDto=JSON.parseObject((String) kafkaMessage,FeignFavorDto.class);
            String token=feignFavorDto.getToken();
            FavorEntity favorEntity=feignFavorDto.getFavorEntity();
            //适配接口做的强制转换
            int userId=(int) favorEntity.getUserId().longValue();
            int videoId=(int) favorEntity.getVideoId().longValue();

            basicFeignService.operateFavorite(userId,0,token);
            basicFeignService.operateFavoriteCount(videoId,0,token);

            UserInfoVo userInfoVo=basicFeignService.getUserByVideoId(videoId,token);

            long toUserId=userInfoVo.getUserDto().getUserId();
            basicFeignService.operateIsFavorited((int) toUserId,0,token);

//            baseMapper.insert(favoriteEntity);
        }
    }
    @Transactional(rollbackFor = Exception.class)
    @KafkaListener(topics = "removeFavoriteQueue")
    public void removeFavoriteQueueListener(ConsumerRecord<?, ?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            Object kafkaMessage = optional.get();
            //开始处理消息
//            String message = JSON.parseObject((String) kafkaMessage, String.class);
//            String[] split = message.split(":");
//            Long videoId=Long.parseLong(split[0]);
//            Long userId=Long.parseLong(split[1]);
//            baseMapper.delete(new QueryWrapper<FavorEntity>().eq("user_id", userId).eq("video_id", videoId));
            System.out.println("接收到取消点赞的通知");
            FeignFavorDto feignFavorDto=JSON.parseObject((String) kafkaMessage,FeignFavorDto.class);
            String token=feignFavorDto.getToken();
            FavorEntity favorEntity=feignFavorDto.getFavorEntity();
            //适配接口做的强制转换
            int userId=(int) favorEntity.getUserId().longValue();
            int videoId=(int) favorEntity.getVideoId().longValue();

            basicFeignService.operateFavorite(userId,1,token);
            basicFeignService.operateFavoriteCount(videoId,1,token);

            UserInfoVo userInfoVo=basicFeignService.getUserByVideoId(videoId,token);

            long toUserId=userInfoVo.getUserDto().getUserId();
            basicFeignService.operateIsFavorited((int) toUserId,1,token);
        }
    }
}
