package org.example.basic.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("douyin-socialize")
public interface SocializeFeignService {
    @GetMapping("/relation/follow/isFollow")
    Boolean isFollow(@RequestParam("userId") Long userId, @RequestParam("followId") Long followId);
}
