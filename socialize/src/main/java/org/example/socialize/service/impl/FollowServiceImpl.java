package org.example.socialize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.socialize.entity.Follow;
import org.example.socialize.feign.BasicFeignService;
import org.example.socialize.service.FollowService;
import org.example.socialize.mapper.FollowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

/**
* @author xuchengrui
* @description 针对表【follow(关注表)】的数据库操作Service实现
* @createDate 2023-08-28 19:38:38
*/
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
    implements FollowService {
    @Autowired
    private  BasicFeignService basicFeignService;


    @Caching(evict = {
            @CacheEvict(value = "follow", key = "#userId"),
            @CacheEvict(value = "follower", key = "#toUserId"),
            @CacheEvict(value = "friend", key = "#userId"),
            @CacheEvict(value = "friend", key = "#toUserId")
    })
    @Override
    public boolean action(long userId, long toUserId, Integer actionType) {
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
        } else if (actionType == 2) {
            LambdaQueryWrapper<Follow> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(Follow::getToUserId,toUserId).eq(Follow::getUserId,userId);
            count = baseMapper.delete(queryWrapper);
        }
        // actionType若不等于1或2，则count为初始值0，return false
        return count == 1;
    }
}




