package org.example.basic.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("douyin-interact")
public interface InteractFeignService {


    @GetMapping("/favorite/isFavorite")
    boolean isFavorite(@RequestParam("userId") Long userId, @RequestParam("videoId") Long videoId);


}
