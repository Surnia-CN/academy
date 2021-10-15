package com.Surnia.wechatLoginService.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.Surnia.commons.utils.Result;
import com.Surnia.wechatLoginService.Utils.WechatUtils;
import com.Surnia.wechatLoginService.feign.UcenterMemberFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @ClassName WechatLoginController
 * @Description TODO
 * @Author Surnia
 * @Date 2021/8/15
 * @Version 1.0
 */

@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "微信登录管理")
@RestController
@RequestMapping("/api/ucenter/wx")

public class WechatLoginController {
    @Resource
    private UcenterMemberFeignService ucenterMemberFeignService;

    // 内部接口不对外开放，故返回值为String
    @ApiOperation(value = "微信二维码登录")
    @GetMapping("wechatLogin")
    public String wechatLogin(){
        // 微信提供的二维码扫描页面
        // 开发文档地址：https://developers.weixin.qq.com/doc/oplatform/Website_App/WeChat_Login/Wechat_Login.html
        String loginUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //	用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），
        //	建议第三方带上该参数，可设置为简单的随机数加session进行校验
        String state = "";

        loginUrl = String.format(loginUrl
                ,WechatUtils.WECHAT_APP_ID
                ,WechatUtils.WECHAT_REDIRECT_URL
                ,state);
        System.out.println(loginUrl);

        // 返回二维码登录页面地址
        return loginUrl;
        //return "redirect:" + loginUrl;
    }


    @ApiOperation(value = "响应微信登录跳转")
    @GetMapping("callback")
    public void callback(String code, String state,HttpServletResponse response){
        // 通过客户扫描二维码得到的code来获取access_token
        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        accessTokenUrl = String.format(accessTokenUrl
                ,WechatUtils.WECHAT_APP_ID
                ,WechatUtils.WECHAT_APP_SECRET
                ,code);

        // 使用hutool封装的httpclient调用accessTokenUrl，得到access_token
        // 得到的access_token是json格式的，其中有价值的为access_token值和客户的openid
        String access_token = HttpUtil.get(accessTokenUrl);

        // 讲access_token转换为map，提取其中的access_token值和openid值
        HashMap tokenMap = JSONUtil.toBean(access_token, HashMap.class);
        access_token = (String) tokenMap.get("access_token");
        String openid = (String) tokenMap.get("openid");

        // 获取用户个人信息（UnionID机制）
        // 文档地址：https://developers.weixin.qq.com/doc/oplatform/Website_App/WeChat_Login/Authorized_Interface_Calling_UnionID.html
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";
        userInfoUrl = String.format(userInfoUrl
                ,access_token
                ,openid);

        // 最终获得的用户个人信息userInfo是json格式的，其中有用的属性是openid，nickname，headimgurl
        // unionid：用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。如果是微信整个平台多个应用的话，应该记录unionid
        String userInfo = HttpUtil.get(userInfoUrl);

        // 微信扫码登录成功后注册并生成用户token
        String token = ucenterMemberFeignService.wechatLoginSuccess(userInfo);
        System.out.println(token);

        // token正常时跳转页面，token带error时抛出异常
        if (!token.equals("insert error") && !token.equals("disabled error")) {
            try {
                response.sendRedirect("http://localhost:3000?token=" + token);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("扫码登录失败");
        }



    }
}
