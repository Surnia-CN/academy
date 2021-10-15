package com.Surnia.userService.feign;


import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;


@FeignClient(value = "service-wechat-login")
public interface WechatLoginFeignService {

    // 内部调用端口，不直接提供给前端调用，故返回值不是Result类型
    @ApiOperation(value = "微信二维码登录")
    @GetMapping("/api/ucenter/wx/wechatLogin")
    public String wechatLogin();

    // 内部调用端口，不直接提供给前端调用，故返回值不是Result类型
    @ApiOperation(value = "响应微信登录跳转")
    @GetMapping("/api/ucenter/wx/callback")
    public HashMap<String,String> callback(@RequestParam("code") String code, @RequestParam("state") String state);

}
