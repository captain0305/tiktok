package org.example.socialize.feign;


import org.example.socialize.dto.UserDto;
import org.example.socialize.vo.FeignVo;
import org.example.socialize.vo.UserInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("douyin-basic")
public interface BasicFeignService {
    @GetMapping("/user/getUserById")
    UserInfoVo getUserDtoById(@RequestParam("user_id")long userId, @RequestParam("token")String token);

    @PutMapping("/user/operateFollowCount")
    FeignVo operateFollowCount(@RequestParam("user_id")int userId, @RequestParam("add_or_sub")int addOrSub,@RequestParam("token")String token);

    @PutMapping("/user/operateFollowerCount")
    FeignVo operateFollowerCount(@RequestParam("user_id")int userId, @RequestParam("add_or_sub")int addOrSub,@RequestParam("token")String token);


}
