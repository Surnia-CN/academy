package com.Surnia.articleService.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName ArticleWrapper
 * @Description TODO
 * @Author Surnia
 * @Date 2021/9/15
 * @Version 1.0
 */

// 根据标题、分类、内容查询关键字，查询结果分页，可根据点赞数、评论数、更新时间排序
@Data
@ApiModel(value="ArticleWrapper对象", description="前端查询文章的条件")
public class ArticleWrapper {

    @ApiModelProperty(value = "查询条件关键字")
    private String wrapperKeyword;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章分类名")
    private String sortName;

    @ApiModelProperty(value = "博文内容")
    private String content;

    @ApiModelProperty(value = "特殊备注项")
    private String remake;

}
