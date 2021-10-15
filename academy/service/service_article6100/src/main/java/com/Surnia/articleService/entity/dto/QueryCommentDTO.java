package com.Surnia.articleService.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName CommentQueryDTO
 * @Description TODO
 * @Author Surnia
 * @Date 2021/10/4
 * @Version 1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="封装查询评论的对象", description="封装 根据主体种类及id查询该主体的所有评论功能 的查询条件")
public class QueryCommentDTO {

    @ApiModelProperty(value = "评论主体种类，1为文章、2为课程、3为视频")
    private String commentNo;

    @ApiModelProperty(value = "评论主体ID")
    private String mainBodyId;
}
