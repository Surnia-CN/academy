package com.Surnia.wechatLoginService.feign;


import com.Surnia.commons.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@FeignClient(value = "service-user")
public interface UcenterMemberFeignService {

    // 内部接口，故返回值为String
    @ApiOperation(value = "微信扫码登录成功后注册并生成用户token")
    @GetMapping("/userService/userCenter/wechatLoginSuccess")
    public String wechatLoginSuccess(@RequestParam("userInfo")String userInfoMap);
}
