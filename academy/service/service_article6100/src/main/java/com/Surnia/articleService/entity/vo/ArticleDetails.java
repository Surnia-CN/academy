package com.Surnia.articleService.entity.vo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName ArticleDetails
 * @Description TODO
 * @Author Surnia
 * @Date 2021/9/18
 * @Version 1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CmsArticle对象", description="文章的所有信息，用于传递前端展示")
public class ArticleDetails {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "博文内容")
    private String content;

    @ApiModelProperty(value = "作者ID")
    private String userId;

    @ApiModelProperty(value = "作者姓名")
    private String userName;

    @ApiModelProperty(value = "作者头像")
    private String userAvatar;

    @ApiModelProperty(value = "文章分类ID")
    private String sortId;

    @ApiModelProperty(value = "自定义文章分类名")
    private String sortName;

    @ApiModelProperty(value = "所属学科ID")
    private String subjectId;

    @ApiModelProperty(value = "所属学科名称")
    private String subjectTitle;

    @ApiModelProperty(value = "课程id")
    private String courseId;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "章节id")
    private String chapterId;

    @ApiModelProperty(value = "章节名称")
    private String chapterName;

    @ApiModelProperty(value = "视频id")
    private String videoId;

    @ApiModelProperty(value = "视频名称")
    private String videoName;

    @ApiModelProperty(value = "点赞数")
    private Long likeCount;

    @ApiModelProperty(value = "评论总数")
    private Long commentCount;

    @ApiModelProperty(value = "浏览量")
    private Long viewsCount;

    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;

    @ApiModelProperty(value = "是否发布 1（已发布）0（未发布）")
    private Integer isPublish;
}
