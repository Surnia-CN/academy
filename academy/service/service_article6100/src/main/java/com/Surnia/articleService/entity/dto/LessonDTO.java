package com.Surnia.articleService.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName LessonDTO
 * @Description TODO
 * @Author Surnia
 * @Date 2021/10/3
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="lesson对象", description="用于接收前端传递的视频所属学科分类信息")
public class LessonDTO {
    @ApiModelProperty(value = "0为没有此类id，1为courseId，2为chapterId，3为videoId，4为sortId")
    String lessonNo;
    @ApiModelProperty(value = "视频具体id")
    String lessonId;
}
