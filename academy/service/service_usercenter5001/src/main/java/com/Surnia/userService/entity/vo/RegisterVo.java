package com.Surnia.userService.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName RegisterVo
 * @Description TODO
 * @Author Surnia
 * @Date 2021/7/29
 * @Version 1.0
 */

@Data
@ApiModel(value="注册对象", description="注册用户使用的vo对象")
public class RegisterVo {
    @NotNull(message = "用户昵称不得为空")
    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @NotNull(message = "用户手机号不得为空")
    @ApiModelProperty(value = "用户手机号")
    private String mobile;

    @NotNull(message = "用户邮箱不得为空")
    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @NotNull(message = "用户密码不得为空")
    @ApiModelProperty(value = "用户密码")
    private String password;

    @NotNull(message = "验证码不得为空")
    @ApiModelProperty(value = "验证码")
    private String code;
}
