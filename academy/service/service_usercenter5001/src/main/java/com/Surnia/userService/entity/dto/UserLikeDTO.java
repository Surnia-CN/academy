package com.Surnia.userService.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName UserLikeDTO
 * @Description TODO
 * @Author Surnia
 * @Date 2021/10/8
 * @Version 1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserLikeDTO对象", description="包装前端提供的点赞数据")
public class UserLikeDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    String userId;

    @ApiModelProperty(value = "点赞种类，1为课程，2为视频，3为文章，4为评论")
    Integer likeNo;

    @ApiModelProperty(value = "被点赞的对象的id")
    String likeId;

}
