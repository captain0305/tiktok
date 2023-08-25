package org.example.interact.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.interact.mapper.FavorMapper;
import org.example.interact.entity.FavorEntity;
import org.example.interact.feign.BasicFeignService;
import org.example.interact.service.FavoriteService;
import org.example.interact.dto.VideoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author carey
 */
@Service("favoriteService")
public class FavoriteServiceImpl extends ServiceImpl<FavorMapper, FavorEntity> implements FavoriteService {

    @Autowired
    private BasicFeignService basicFeignService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @CacheEvict(value = "favorite", key = "#userId")
    @Override
    public boolean action(long userId, Long videoId, Integer actionType) {
        // 当前token对应的user不存在
        if (userId == 0) {
            return false;
        }
        if (actionType == 1) {
            // 点赞
            // 创建要存储的数据对象并填充数据
            FavorEntity favoriteEntity = new FavorEntity();
            favoriteEntity.setUserId(userId);
            favoriteEntity.setVideoId(videoId);

            // 查询是否已经点赞
            FavorEntity entity = baseMapper.selectOne(new QueryWrapper<FavorEntity>()
                    .eq("user_id", userId)
                    .eq("video_id", videoId));

            int count = 0;
            // 没点过赞才插入数据
            if (entity == null){
                count = baseMapper.insert(favoriteEntity);
                kafkaTemplate.send("favoriteQueue", JSON.toJSONString(favoriteEntity));
                return true;
            } else {
                return false;
            }
        } else if (actionType == 2) {
            // 取消点赞
            int count = baseMapper.delete(new QueryWrapper<FavorEntity>().eq("user_id", userId).eq("video_id", videoId));
            kafkaTemplate.send("removeFavoriteQueue", JSON.toJSONString(videoId+":"+userId));
            return true;
        }
        // actionType的值不合法
        return false;
    }

    @Cacheable(value = "favorite", key = "#userId", sync = true)
    @Override
    public VideoDto[] favoriteList(Long userId, Long id) {
        // 获取该用户所有喜欢的视频的videoId
        List<FavorEntity> favoriteEntities = this.list(new QueryWrapper<FavorEntity>().eq("user_id", userId));
        List<Long> videoIds = favoriteEntities.stream().map(FavorEntity::getVideoId).collect(Collectors.toList());

        return basicFeignService.videoList(videoIds, id);
    }

    @Override
    public Integer countByVideoId(Long videoId) {
        return this.count(new QueryWrapper<FavorEntity>().eq("video_id", videoId));
    }

    @Override
    public boolean isFavorite(Long userId, Long videoId) {
        FavorEntity favoriteEntity = baseMapper.selectOne(new QueryWrapper<FavorEntity>()
                .eq("user_id", userId)
                .eq("video_id", videoId));

        return favoriteEntity != null;
    }
}
