package org.example.socialize.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.socialize.dto.UserDto;
import org.example.socialize.entity.Follow;
import org.example.socialize.feign.BasicFeignService;
import org.example.socialize.service.FollowService;
import org.example.socialize.mapper.FollowMapper;
import org.example.socialize.util.JwtUtils;
import org.example.socialize.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rx.Completable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
* @author xuchengrui
* @description 针对表【follow(关注表)】的数据库操作Service实现
* @createDate 2023-08-28 19:38:38
*/
@Service
@Slf4j
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
    implements FollowService {
    @Autowired
    private  BasicFeignService basicFeignService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


//    @Caching(evict = {
//            @CacheEvict(value = "follow", key = "#userId"),
//            @CacheEvict(value = "follower", key = "#toUserId"),
//            @CacheEvict(value = "friend", key = "#userId"),
//            @CacheEvict(value = "friend", key = "#toUserId")
//    })
    @Override
    @Transactional
    public boolean action(long userId, long toUserId, Integer actionType,String token) {
        // 当前userId对应的user不存在
        if (userId == 0)
            return false;
        // 数据库变化行数
        int count = 0;
        if (actionType == 1) {
            Follow followEntity = new Follow();
            followEntity.setUserId(userId);
            followEntity.setToUserId(toUserId);
            LambdaQueryWrapper<Follow> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(Follow::getToUserId,toUserId).eq(Follow::getUserId,userId);


            // 查询当前数据，不存在才添加
            int num = baseMapper.selectCount(queryWrapper);
            if (num == 0)
                count = baseMapper.insert(followEntity);

            if (count==1) {
                kafkaTemplate.send("FollowQueue", JSON.toJSONString(0+":"+userId+":"+token));
                kafkaTemplate.send("FollowerQueue", JSON.toJSONString(0+":"+toUserId+":"+token));
            }
        } else if (actionType == 2) {
            LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Follow::getToUserId, toUserId).eq(Follow::getUserId, userId);
            count = baseMapper.delete(queryWrapper);
            if (count == 1) {
                kafkaTemplate.send("FollowQueue", JSON.toJSONString(1 + ":" + userId + ":" + token));
                kafkaTemplate.send("FollowerQueue", JSON.toJSONString(1 + ":" + toUserId + ":" + token));
            }
        }
        // actionType若不等于1或2，则count为初始值0，return false
        return count == 1;
    }

    /**
     * 本来想要使用redis缓存发现缓存会造成很严重的不同步问题，可以对user表的数据在basic中进行缓存
     * 查询关注列表
     * @param userId
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public UserDto[] getFollowList(Long userId, String token)throws Exception {


        LambdaQueryWrapper<Follow> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getUserId,userId);
        List<Follow> followList=baseMapper.selectList(queryWrapper);
        List<Long> toUserIds=followList.stream().map(item->{
            return item.getToUserId();
        }).collect(Collectors.toList());


        UserDto[] userList = getUserList(token, toUserIds);
        return userList;
    }

    /**
     * 查询粉丝列表
     * @param userId
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public UserDto[] getFollowerList(Long userId, String token) throws Exception{

        LambdaQueryWrapper<Follow> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getToUserId,userId);
        List<Follow> followList=baseMapper.selectList(queryWrapper);
        List<Long> toUserIds=followList.stream().map(item->{
            return item.getUserId();
        }).collect(Collectors.toList());


        UserDto[] userList = getUserList(token, toUserIds);
        return userList;
    }

    @Override
    public UserDto[] getFriendList(Long userId, String token)throws Exception {
        LambdaQueryWrapper<Follow> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getUserId,userId);
        List<Follow> followList=baseMapper.selectList(queryWrapper);
        List<Long> userIds = followList.stream().filter(followEntity -> {
            int count = baseMapper.selectCount(new LambdaQueryWrapper<Follow>()
                    .eq(Follow::getUserId, followEntity.getToUserId())
                    .eq(Follow::getToUserId, userId));
            return count == 1;
        }).map(followEntity -> {
            return followEntity.getToUserId();
        }).collect(Collectors.toList());

        UserDto[] userList = getUserList(token, userIds);
        return userList;

    }

    @Override
    public boolean isFollow(Long user_id, Long follow_id) {


        LambdaQueryWrapper<Follow> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getUserId,user_id).eq(Follow::getToUserId,follow_id);
        Follow follow=baseMapper.selectOne(queryWrapper);
        if (follow==null)
            return false;
        return true;

    }


    private UserDto[] getUserList(String token, List<Long> toUserIds) throws InterruptedException, ExecutionException {
        UserDto[] userList=new UserDto[toUserIds.size()];


        List<CompletableFuture> futureList=new ArrayList<>();
        for (int i = 0; i < toUserIds.size() ; i++) {
            int finalI=i;
            CompletableFuture<Void> future=CompletableFuture.runAsync(()->{
                UserInfoVo userInfoVo=basicFeignService.getUserDtoById(toUserIds.get(finalI), token);
                userList[finalI]=userInfoVo.getUserDto();

            });
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).get();
        return userList;
    }


}




