package org.example.socialize.feign;


import org.example.socialize.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("douyin-basic")
public interface BasicFeignService {
    @GetMapping("/user/getUserById")
    UserDto getUserDtoById(@RequestParam("user_id")long userId,@RequestParam("token")String token);

}
