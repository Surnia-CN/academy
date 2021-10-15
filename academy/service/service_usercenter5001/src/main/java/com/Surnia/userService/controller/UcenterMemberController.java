package com.Surnia.userService.controller;


import cn.hutool.Hutool;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.Surnia.commons.exception.MyException;
import com.Surnia.commons.utils.JWTUtils;
import com.Surnia.commons.utils.MD5;
import com.Surnia.commons.utils.Result;
import com.Surnia.userService.entity.UcenterMember;
import com.Surnia.userService.entity.dto.UserLikeDTO;
import com.Surnia.userService.entity.vo.RegisterVo;
import com.Surnia.userService.feign.ArticleLikeFeignService;
import com.Surnia.userService.feign.EduLikeFeignService;
import com.Surnia.userService.feign.WechatLoginFeignService;
import com.Surnia.userService.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Wrapper;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-07-26
 */

@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "用户管理")
@RestController
@RequestMapping("/userService/userCenter")
public class UcenterMemberController {
    @Resource
    private UcenterMemberService ucenterMemberService;
    @Resource
    private MailSender mailSender;
    @Value("${spring.mail.username}")
    private String emailSendFrom;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private WechatLoginFeignService wechatLoginFeignService;
    @Resource
    private EduLikeFeignService eduLikeFeignService;
    @Resource
    private ArticleLikeFeignService articleLikeFeignService;

    @ApiOperation(value = "测试redis连接")
    @PostMapping("ping")
    public Result ping() {
        redisTemplate.opsForValue().set("key2", "test");
        redisTemplate.opsForValue().set("key3", "value3", 5, TimeUnit.MINUTES);
        String key2 = redisTemplate.opsForValue().get("key2");
        return Result.ok().data("key2", key2);
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("register")
    public Result register(@RequestBody @Validated RegisterVo user) {
        System.out.println(user);

        String code = redisTemplate.opsForValue().get(user.getEmail());
        if (code == null || !user.getCode().equals(code)) {
            return Result.error().message("验证码错误");
        }

        user.setPassword(MD5.encrypt(user.getPassword()));
        UcenterMember ucenterMember = new UcenterMember();
        BeanUtils.copyProperties(user, ucenterMember);
        System.out.println(ucenterMember);
        boolean result = ucenterMemberService.register(ucenterMember);
        if (result) {
            return Result.ok();
        } else {
            return Result.error().message("注册失败");
        }
    }

    @ApiOperation(value = "根据邮箱发送验证码")
    @PostMapping("sendEmail/{email}")
    public Result sendEmail(@PathVariable("email") String email) {

        // SimpleMailMessage
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(emailSendFrom);
        msg.setTo(email);//接收人
        String code = RandomUtil.randomString(4);
        msg.setSubject("注册-验证码");
        msg.setText("您正在注册，验证码为：" + code + "。5分钟内有效。本邮件由系统自动发送，请勿回复。");  //这里的邮件内容是 文本类型

        try {
            this.mailSender.send(msg);
            System.out.println("success:" + code);
            redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    // 验证码未设置 todo
    @ApiOperation(value = "用户手机号密码登录")
    @PostMapping("login")
    public Result login(@RequestBody RegisterVo user) {
        String token = ucenterMemberService.login(user.getMobile(), MD5.encrypt(user.getPassword()));
        if (!token.equals("error")) {
            System.out.println("loginSuccess...");
            return Result.ok().data("token", token);
        } else {
            System.out.println("loginFail...");
            //throw new MyException(444,"登录失败");
            return Result.error().message("登录失败");
        }
    }


    @ApiOperation(value = "用户微信扫码登录")
    @GetMapping("wechatLogin")
    public Result wechatLogin() {
        String wechatLoginUrl = wechatLoginFeignService.wechatLogin();
        System.out.println(wechatLoginUrl);

        return Result.ok().data("wechatLoginUrl", wechatLoginUrl);
    }


    // 内部接口，故返回值为String
    @ApiOperation(value = "微信扫码登录成功后注册并生成用户token")
    @GetMapping("wechatLoginSuccess")
    public String wechatLoginSuccess(String userInfo) {
        HashMap<String, String> userInfoMap = JSONUtil.toBean(userInfo, HashMap.class);
        System.out.println("wechatLoginSuccess...");


        // 若用户未注册则注册后生成token返回，若注册则直接返回token
        return ucenterMemberService.login(userInfoMap);
    }

    @ApiOperation(value = "根据token获取用户信息")
    @GetMapping("getUserInfo")
    public Result getUserInfo(HttpServletRequest request) {
        String memberIdByJwtToken = JWTUtils.getMemberIdByJwtToken(request);
        System.out.println("!member:" + memberIdByJwtToken);
        if (memberIdByJwtToken != null) {
            UcenterMember ucenterMember = ucenterMemberService.getById(memberIdByJwtToken);
            System.out.println(ucenterMember.getNickname());
            System.out.println(ucenterMember.getAvatar());
            return Result.ok().data("nickname", ucenterMember.getNickname()).data("avatar", ucenterMember.getAvatar());
        }
        return Result.error().message("根据token获取用户信息失败");
    }

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据token获取用户信息")
    @GetMapping("getUserById/{userId}")
    public HashMap<String, Object> getUserById(@PathVariable("userId") String userId) {
        UcenterMember ucenterMember = ucenterMemberService.getById(userId);
        if (ucenterMember == null) {
            throw new MyException(404, "该用户id不存在");
        }
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userNickname", ucenterMember.getNickname());
        userMap.put("userAvatar", ucenterMember.getAvatar());
        userMap.put("userIsDisabled", ucenterMember.getIsDisabled());
        return userMap;
    }

    @ApiOperation(value = "查询点赞列表")
    @GetMapping("getLikeList/{userId}")
    public Result getLikeList(@PathVariable("userId") String userId) {
        UcenterMember ucenterMember = ucenterMemberService.getById(userId);
        if (ucenterMember != null) {
            return Result.ok().data("likeList", ucenterMember.getLikeList());
        } else {
            return Result.error().message("用户信息有误");
        }
    }

    @ApiOperation(value = "增加点赞")
    @PostMapping("addLikeList")
    public Result addLikeList(@RequestBody UserLikeDTO userLikeDTO) {
        UcenterMember ucenterMember = ucenterMemberService.getById(userLikeDTO.getUserId());
        if (ucenterMember == null) {
            return Result.error().message("用户信息有误");
        }
        String likeList = ucenterMember.getLikeList();
        List<String> likes = Convert.toList(String.class, likeList);
        likes.add(userLikeDTO.getLikeId());
        ucenterMember.setLikeList(Convert.toStr(likes));
        boolean result = ucenterMemberService.updateById(ucenterMember);
        if (!result) {
            return Result.error().message("更新用户信息有误");
        }

        // 点赞种类，1为课程，2为视频，3为文章，4为评论
        if (1 == userLikeDTO.getLikeNo()) {
            result = eduLikeFeignService.addCourseLike(userLikeDTO.getLikeId());
            if (!result) {
                return Result.error().message("为课程点赞失败");
            }
        }
        if (2 == userLikeDTO.getLikeNo()) {
            result = eduLikeFeignService.addVideoLike(userLikeDTO.getLikeId());
            if (!result) {
                return Result.error().message("为视频点赞失败");
            }
        }
        if (3 == userLikeDTO.getLikeNo()) {
            result = articleLikeFeignService.addArticleLike(userLikeDTO.getLikeId());
            if (!result) {
                return Result.error().message("为文章点赞失败");
            }
        }
        if (4 == userLikeDTO.getLikeNo()) {
            result = articleLikeFeignService.addCommentLike(userLikeDTO.getLikeId());
            if (!result) {
                return Result.error().message("为评论点赞失败");
            }
        }
        return Result.ok().data("likeList", ucenterMember.getLikeList());
    }

    @ApiOperation(value = "删除点赞")
    @DeleteMapping("deleteLikeList")
    public Result deleteLikeList(@RequestBody UserLikeDTO userLikeDTO) {
        UcenterMember ucenterMember = ucenterMemberService.getById(userLikeDTO.getUserId());
        if (ucenterMember == null) {
            return Result.error().message("用户信息有误");
        }
        String likeList = ucenterMember.getLikeList();
        List<String> likes = Convert.toList(String.class, likeList);
        boolean result = likes.remove(userLikeDTO.getLikeId());
        if (!result) {
            return Result.error().message("用户信息有误");
        }
        ucenterMember.setLikeList(Convert.toStr(likes));
        result = ucenterMemberService.updateById(ucenterMember);
        if (!result) {
            return Result.error().message("更新用户信息有误");
        }

        // 点赞种类，1为课程，2为视频，3为文章，4为评论
        if (1 == userLikeDTO.getLikeNo()) {
            result = eduLikeFeignService.deleteCourseLike(userLikeDTO.getLikeId());
            if (!result) {
                return Result.error().message("取消课程点赞失败");
            }
        }
        if (2 == userLikeDTO.getLikeNo()) {
            result = eduLikeFeignService.deleteVideoLike(userLikeDTO.getLikeId());
            if (!result) {
                return Result.error().message("取消视频点赞失败");
            }
        }
        if (3 == userLikeDTO.getLikeNo()) {
            result = articleLikeFeignService.deleteArticleLike(userLikeDTO.getLikeId());
            if (!result) {
                return Result.error().message("取消文章点赞失败");
            }
        }
        if (4 == userLikeDTO.getLikeNo()) {
            result = articleLikeFeignService.deleteCommentLike(userLikeDTO.getLikeId());
            if (!result) {
                return Result.error().message("取消评论点赞失败");
            }
        }

        return Result.ok().data("likeList", ucenterMember.getLikeList());
    }
}

