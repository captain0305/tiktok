package org.example.socialize.service;

import org.example.socialize.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author xuchengrui
* @description 针对表【follow(关注表)】的数据库操作Service
* @createDate 2023-08-28 19:38:38
*/
public interface FollowService extends IService<Follow> {
    boolean action(long userId, long toUserId, Integer actionType);
}
