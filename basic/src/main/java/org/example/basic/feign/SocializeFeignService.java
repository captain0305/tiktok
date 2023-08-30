package org.example.basic.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("douyin-socialize")
public interface SocializeFeignService {

    /**
     * userId是被关注者
     * followId是关注着
     * @param userId
     * @param followId
     * @return
     */
    @GetMapping("/relation/follow/isFollow")
    Boolean isFollow(@RequestParam("user_id") Long userId, @RequestParam("follow_id") Long followId);
}
