package com.Surnia.eduService.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName EduSubjectList
 * @Description TODO
 * @Author Surnia
 * @Date 2021/7/3
 * @Version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="EduSubject列表", description="EduSubject列表")
public class EduSubjectList {
    @ApiModelProperty(value = "课程类别ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    @ApiModelProperty(value = "类别名称")
    private String title;
    @ApiModelProperty(value = "所含子元素")
    private List<EduSubjectChildren> childrenList = new ArrayList<>();
}
