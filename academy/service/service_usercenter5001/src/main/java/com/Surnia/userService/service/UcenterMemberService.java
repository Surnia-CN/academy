package com.Surnia.userService.service;

import com.Surnia.userService.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-07-26
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(String mobile, String password);

    String login(HashMap<String,String> userInfoMap);

    boolean register(UcenterMember ucenterMember);

}
