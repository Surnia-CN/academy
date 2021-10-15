package com.Surnia.articleService.feign;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;

@FeignClient(value = "service-user")
public interface UserFeignService {

    // 内部接口，返回值不为Result
    // HashMap包含了"userNickname","userAvatar","userIsDisabled"
    @ApiOperation(value = "根据token获取用户信息")
    @GetMapping("/userService/userCenter/getUserById/{userId}")
    public HashMap<String,Object> getUserById(@PathVariable("userId") String userId);

}
