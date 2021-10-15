package com.Surnia.userService.service.impl;

import com.Surnia.commons.utils.JWTUtils;
import com.Surnia.commons.utils.MD5;
import com.Surnia.userService.entity.UcenterMember;
import com.Surnia.userService.mapper.UcenterMemberMapper;
import com.Surnia.userService.service.UcenterMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-07-26
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Resource
    private UcenterMemberMapper ucenterMemberMapper;

    @Override
    public boolean register(UcenterMember ucenterMember) {
        QueryWrapper<UcenterMember> ucenterMemberQueryWrapper = new QueryWrapper<>();
        ucenterMemberQueryWrapper.eq("mobile",ucenterMember.getMobile());
        Integer selectCount = ucenterMemberMapper.selectCount(ucenterMemberQueryWrapper);
        if (selectCount > 0){
            return false;
        }
        int result = ucenterMemberMapper.insert(ucenterMember);
        if (result > 0){
            return true;
        }
        return false;
    }

    @Override
    public String login(String mobile, String password) {
        // 手机号或密码为空则返回error
        if(mobile.isEmpty() || password.isEmpty()){
            return "error";
        }

        QueryWrapper<UcenterMember> userWrapper = new QueryWrapper<>();
        userWrapper.eq("mobile", mobile);
        userWrapper.eq("password",password);
        userWrapper.eq("is_disabled",0);
        UcenterMember user = ucenterMemberMapper.selectOne(userWrapper);

        System.out.println(user);

        if (user != null){
            return JWTUtils.getJwtToken(user.getId(), user.getNickname(),user.getAvatar());
        }
        return "error";
    }

    @Override
    public String login(HashMap<String,String> userInfoMap) {
        System.out.println("wechatlogin....");

        System.out.println("query....");
        // 根据openid查询数据库是否已有数据
        String openid = userInfoMap.get("openid");
        QueryWrapper<UcenterMember> ucenterMemberQueryWrapper = new QueryWrapper<>();
        ucenterMemberQueryWrapper.eq("openid",openid);
        UcenterMember resultUser = ucenterMemberMapper.selectOne(ucenterMemberQueryWrapper);

        // 如果没有该openid的用户数据，则新建用户信息
        if (resultUser == null){
            System.out.println("add....");
            String nickname = userInfoMap.get("nickname");
            String headimgurl = userInfoMap.get("headimgurl");
            UcenterMember user = new UcenterMember();
            user.setOpenid(openid);
            user.setNickname(nickname);
            user.setAvatar(headimgurl);
            int insertResult = ucenterMemberMapper.insert(user);

            // 新建成功后返回用户token
            if (insertResult == 1){
                System.out.println("tokening....");
                return JWTUtils.getJwtToken(user.getId(),user.getNickname(),user.getAvatar());
            } else {
                System.out.println("insert error！");
                return "insert error";
            }
        }
        // 判断用户是否被禁用
        Boolean isDisabled = resultUser.getIsDisabled();
        if (isDisabled){
            System.out.println("disabled error！");
            return "disabled error";
        } else {
            // 已有客户信息且未被禁用则直接返回token
            return JWTUtils.getJwtToken(resultUser.getId(), resultUser.getNickname(),resultUser.getAvatar());
        }
    }


}
