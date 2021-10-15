package com.Surnia.articleService.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName ArticleCommentDTO
 * @Description TODO
 * @Author Surnia
 * @Date 2021/9/22
 * @Version 1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CmsArticleComment对象", description="评论对象，接收前端包装的评论数据")
public class CommentDTO {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "评论主体种类，1为文章、2为课程、3为视频")
    private String commentNo;

    @ApiModelProperty(value = "评论主体ID")
    private String mainBodyId;

    @ApiModelProperty(value = "父评论ID")
    private String parentId;

    @ApiModelProperty(value = "评论用户ID")
    private String userId;

    @ApiModelProperty(value = "回复的评论的id")
    private String answerId;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "备注")
    private String remark;
}
