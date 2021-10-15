package com.Surnia.articleService.entity.vo;

import com.Surnia.articleService.entity.CmsArticleComment;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @ClassName ArticleCommentVO
 * @Description TODO
 * @Author Surnia
 * @Date 2021/9/22
 * @Version 1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CmsArticleComment对象", description="返回给前端的，某文章下的所有评论封装对象")
public class ArticleCommentVO {

    @ApiModelProperty(value = "文章评论ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "文章ID")
    private String articleId;

    @ApiModelProperty(value = "父评论ID")
    private String parentId;

    @ApiModelProperty(value = "子评论列表")
    private List<ArticleCommentVO> childrenCommentList;

    @ApiModelProperty(value = "评论序号")
    private Long commentIndex;

    @ApiModelProperty(value = "子评论序号")
    private Long childrenIndex;

    @ApiModelProperty(value = "回复的评论的id")
    private String answerId;

    @ApiModelProperty(value = "回复的评论的昵称")
    private String answerName;

    @ApiModelProperty(value = "评论用户ID")
    private String userId;

    @ApiModelProperty(value = "昵称")
    private String userNickname;

    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    @ApiModelProperty(value = "用户是否禁用 1（true）已禁用，  0（false）未禁用")
    private Boolean userIsDisabled;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "点赞数")
    private Long likeCount;

    @ApiModelProperty(value = "点踩数")
    private Long unlikeCount;

    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;
}
