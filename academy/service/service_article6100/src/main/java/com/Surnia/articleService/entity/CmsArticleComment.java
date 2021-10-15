package com.Surnia.articleService.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author testjava
 * @since 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CmsArticleComment对象", description="文章评论")
public class CmsArticleComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章评论ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "评论主体种类，1为文章、2为课程、3为视频")
    private String commentNo;

    @ApiModelProperty(value = "评论主体ID")
    private String mainBodyId;

    @ApiModelProperty(value = "父评论ID")
    private String parentId;

    @ApiModelProperty(value = "评论序号")
    private Long commentIndex;

    @ApiModelProperty(value = "子评论序号")
    private Long childrenIndex;

    @ApiModelProperty(value = "回复的评论的id")
    private String answerId;

    @ApiModelProperty(value = "评论用户ID")
    private String userId;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "点赞数")
    private Long likeCount;

    @ApiModelProperty(value = "点踩数")
    private Long unlikeCount;

    @ApiModelProperty(value = "备注")
    private String remark;

    // 逻辑删除
    @TableLogic
    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;


}
